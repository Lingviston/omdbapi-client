package by.ve.omdbapiandroid.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import by.ve.omdbapiandroid.domain.MoviesDataSourceFactory
import by.ve.omdbapiandroid.domain.RecentSearchesDataSourceFactory
import by.ve.omdbapiandroid.view.MovieAdapterItem
import by.ve.omdbapiandroid.view.RecentSearchAdapterItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoviesListViewModel @Inject constructor(
    private val moviesDataSourceFactory: MoviesDataSourceFactory,
    private val recentSearchesDataSourceFactory: RecentSearchesDataSourceFactory
) : ViewModel() {

    val movies: LiveData<PagedList<MovieAdapterItem>>

    val recentSearches: LiveData<PagedList<RecentSearchAdapterItem>>

    val query = MutableLiveData<String>("")

    val recentSearchesOpen = MutableLiveData<Boolean>(false)

    private val querySubject = PublishSubject.create<String>()

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

        recentSearches = LivePagedListBuilder<Int, RecentSearchAdapterItem>(recentSearchesDataSourceFactory.map {
            RecentSearchAdapterItem(
                uid = it.uid,
                query = it.params.query,
                onClick = ::onRecentQuerySelected
            )
        }, 10).build()

        startListeningSearchQuery()
    }

    fun onSearchQueryChange(newQuery: String) {
        query.value = newQuery
        querySubject.onNext(newQuery)
    }

    fun onRecentSearchesClick() {
        recentSearchesOpen.value = true
    }

    private fun onRecentQuerySelected(recentQuery: String) {
        query.value = recentQuery
        recentSearchesOpen.value = false
    }

    private fun startListeningSearchQuery() {
        compositeDisposable += querySubject
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribe { query ->
                moviesDataSourceFactory.query = query
            }
    }
}