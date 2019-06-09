package by.ve.omdbapiandroid.presentation.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import by.ve.omdbapiandroid.presentation.lifecycle.SingleLiveEvent
import by.ve.omdbapiandroid.presentation.paging.MovieAdapterItemPagedListBuilder
import by.ve.omdbapiandroid.presentation.paging.SearchQueryAdapterItemPagedListBuilder
import by.ve.omdbapiandroid.presentation.viewmodel.model.FilterParams
import by.ve.omdbapiandroid.presentation.viewmodel.model.SearchQueryUpdate
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.view.model.MovieAdapterItem
import by.ve.omdbapiandroid.view.model.SearchQueryAdapterItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val USER_INPUT_DEBOUNCE = 500L

class MoviesListViewModel @Inject constructor(
    val filterViewModel: FilterViewModel,
    private val movieAdapterItemPagedListBuilder: MovieAdapterItemPagedListBuilder,
    searchQueryAdapterItemPagedListBuilder: SearchQueryAdapterItemPagedListBuilder
) : ViewModel() {

    val movies: LiveData<PagedList<MovieAdapterItem>> = movieAdapterItemPagedListBuilder.build()

    val recentQueries: LiveData<PagedList<SearchQueryAdapterItem>> =
        searchQueryAdapterItemPagedListBuilder.build(::onRecentQuerySelected)

    val query = MutableLiveData<String>()

    val recentSearchesOpen = MutableLiveData<Boolean>(false)

    val showFilterSettings = SingleLiveEvent<Void>()

    private val querySubject = PublishSubject.create<String>()

    private val recentQuerySubject = PublishSubject.create<SearchQueryDto>()

    private val compositeDisposable = CompositeDisposable()

    init {
        startListeningSearchQuery()
    }

    @VisibleForTesting
    public override fun onCleared() {
        filterViewModel.onCleared()
        movieAdapterItemPagedListBuilder.dispose()
        compositeDisposable.dispose()
    }

    fun onSearchQueryChange(newQuery: String) {
        querySubject.onNext(newQuery)
    }

    fun onRecentSearchesClick() {
        recentSearchesOpen.value = true
    }

    fun onShowFilterClick() {
        showFilterSettings.call()
    }

    private fun onRecentQuerySelected(searchQuery: SearchQueryDto) {
        recentSearchesOpen.value = false
        recentQuerySubject.onNext(searchQuery)
    }

    private fun startListeningSearchQuery() {
        val queryUpdates = querySubject
            .distinctUntilChanged()
            .debounce(USER_INPUT_DEBOUNCE, TimeUnit.MILLISECONDS)
            .map { SearchQueryUpdate.QueryUpdate(it) }

        val filterParamsUpdates = filterViewModel.appliedFilterParams
            .map { SearchQueryUpdate.FilterParamsUpdate(it) }

        val recentQueryUpdates = recentQuerySubject.map { SearchQueryUpdate.FullQueryUpdate(it) }

        compositeDisposable += Observable.merge(queryUpdates, filterParamsUpdates, recentQueryUpdates)
            .scan(SearchQueryDto.EMPTY, ::reduceUpdatesToSearchQueryDto)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { queryDto ->
                filterViewModel.onFilterParamsRestored(
                    FilterParams(
                        year = queryDto.year,
                        type = queryDto.type
                    )
                )
                query.value = queryDto.query
                movieAdapterItemPagedListBuilder.updateQuery(queryDto)
            }
    }

    private fun reduceUpdatesToSearchQueryDto(oldDto: SearchQueryDto, update: SearchQueryUpdate): SearchQueryDto {
        return when (update) {
            is SearchQueryUpdate.QueryUpdate -> oldDto.copy(
                query = update.query
            )
            is SearchQueryUpdate.FilterParamsUpdate -> oldDto.copy(
                year = update.filterParams.year,
                type = update.filterParams.type
            )
            is SearchQueryUpdate.FullQueryUpdate -> update.searchQueryDto
        }
    }
}
