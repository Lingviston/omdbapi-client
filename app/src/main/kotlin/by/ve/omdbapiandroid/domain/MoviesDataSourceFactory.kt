package by.ve.omdbapiandroid.domain

import androidx.paging.DataSource
import by.ve.omdbapiandroid.repositories.MoviesRepository
import by.ve.omdbapiandroid.repositories.RecentSearchesRepository
import by.ve.omdbapiandroid.repositories.model.MovieDto
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MoviesDataSourceFactory(
    private val moviesRepository: MoviesRepository,
    private val recentSearchesRepository: RecentSearchesRepository
) : DataSource.Factory<Int, MovieDto>(), Disposable {

    var query: String = ""
        set(value) {
            field = value
            queryScopeDisposable.clear()
            latestDataSource?.invalidate()
        }

    private val queryScopeDisposable = CompositeDisposable()

    private var latestDataSource: MoviesDataSource? = null
        set(value) {
            field = value
            queryScopeDisposable.clear()
        }

    override fun create(): DataSource<Int, MovieDto> =
        MoviesDataSource(query, moviesRepository, recentSearchesRepository, queryScopeDisposable).also {
            latestDataSource = it
        }

    override fun isDisposed(): Boolean = queryScopeDisposable.isDisposed

    override fun dispose() = queryScopeDisposable.dispose()

}