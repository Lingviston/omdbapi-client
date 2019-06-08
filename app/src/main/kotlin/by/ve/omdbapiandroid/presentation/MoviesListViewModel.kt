package by.ve.omdbapiandroid.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import by.ve.omdbapiandroid.domain.MoviesDataSourceFactory
import by.ve.omdbapiandroid.view.MovieAdapterItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoviesListViewModel @Inject constructor(
    private val moviesDataSourceFactory: MoviesDataSourceFactory
) : ViewModel() {

    val movies: LiveData<PagedList<MovieAdapterItem>>

    val query = MutableLiveData<String>("")

    private val querySubject = PublishSubject.create<String>()

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += moviesDataSourceFactory

        val moviesListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()

        movies = LivePagedListBuilder<Int, MovieAdapterItem>(moviesDataSourceFactory.map {
            MovieAdapterItem(it.title, it.year, it.posterUrl)
        }, moviesListConfig).build()

        startListeningSearchQuery()
    }

    fun onSearchQueryChange(newQuery: String) {
        query.value = newQuery
        querySubject.onNext(newQuery)
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