package by.ve.omdbapiandroid.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import by.ve.omdbapiandroid.domain.MoviesDataSourceFactory
import by.ve.omdbapiandroid.view.MovieAdapterItem
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoviesListViewModel @Inject constructor(
    private val moviesDataSourceFactory: MoviesDataSourceFactory
) : ViewModel() {

    val movies: LiveData<PagedList<MovieAdapterItem>>

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable += moviesDataSourceFactory

        val moviesListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()

        moviesDataSourceFactory.query = "Home Alone"
        movies = LivePagedListBuilder<Int, MovieAdapterItem>(moviesDataSourceFactory.map {
            MovieAdapterItem(it.title, it.year, it.posterUrl)
        }, moviesListConfig).build()

        Single.just("Spider-man").delay(5, TimeUnit.SECONDS).subscribe { query ->
            moviesDataSourceFactory.query = query
        }
    }
}