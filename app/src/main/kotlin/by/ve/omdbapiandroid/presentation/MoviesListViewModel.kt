package by.ve.omdbapiandroid.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import by.ve.omdbapiandroid.domain.MoviesDataSourceFactory
import by.ve.omdbapiandroid.domain.RecentSearchesDataSourceFactory
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.view.MovieAdapterItem
import by.ve.omdbapiandroid.view.SearchQueryAdapterItem
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val USER_INPUT_DEBOUNCE = 500L

class MoviesListViewModel @Inject constructor(
    private val moviesDataSourceFactory: MoviesDataSourceFactory,
    recentSearchesDataSourceFactory: RecentSearchesDataSourceFactory
) : ViewModel() {

    val movies: LiveData<PagedList<MovieAdapterItem>>

    val searchesQuery: LiveData<PagedList<SearchQueryAdapterItem>>

    val query = MutableLiveData<String>("")

    val recentSearchesOpen = MutableLiveData<Boolean>(false)

    val showFilterSettings = SingleLiveEvent<Void>()

    val filterViewModel = FilterViewModel()

    private val querySubject = PublishSubject.create<String>()

    private val recentQuerySubject = PublishSubject.create<SearchQueryDto>()

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += moviesDataSourceFactory

        val moviesListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()

        movies = LivePagedListBuilder<Int, MovieAdapterItem>(moviesDataSourceFactory.map {
            MovieAdapterItem(
                title = it.title,
                year = it.year,
                posterUrl = it.posterUrl
            )
        }, moviesListConfig).build()

        searchesQuery = LivePagedListBuilder<Int, SearchQueryAdapterItem>(recentSearchesDataSourceFactory.map {
            SearchQueryAdapterItem(
                query = it.query,
                year = it.year.toString(),
                onClick = { onRecentQuerySelected(it) }
            )
        }, 10).build()

        startListeningSearchQuery()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun onSearchQuerySubmit(newQuery: String) {
        query.value = newQuery
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
        val queryObservable = querySubject
            .debounce(USER_INPUT_DEBOUNCE, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
        val filterParamsObservable = filterViewModel.filterParams.startWith(FilterParams())

        val newQueryObservable = Observables.combineLatest(
            queryObservable,
            filterParamsObservable
        ) { query, params ->
            Log.d("SearchViewModel", "Query: $query, params: ${params.year}")
            SearchQueryDto(query = query, year = params.year)
        }

        val recentQueryObservable = recentQuerySubject.doOnNext {
            query.value = it.query
            filterViewModel.onFilterParamsRestored(FilterParams(year = it.year))
        }

        compositeDisposable += Observable.merge(newQueryObservable, recentQueryObservable)
            .distinctUntilChanged()
            .subscribe { query ->
                moviesDataSourceFactory.query = query
            }
    }
}