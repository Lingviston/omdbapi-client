package by.ve.omdbapiandroid.domain

import androidx.paging.PositionalDataSource
import by.ve.omdbapiandroid.repositories.MoviesRepository
import by.ve.omdbapiandroid.repositories.RecentSearchesRepository
import by.ve.omdbapiandroid.repositories.model.MovieDto
import by.ve.omdbapiandroid.repositories.model.RecentSearchParamsDto
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

private const val INITIAL_PAGE_NUMBER = 1
private const val PAGE_SIZE = 10

class MoviesDataSource(
    private val query: String,
    private val moviesRepository: MoviesRepository,
    private val recentSearchesRepository: RecentSearchesRepository,
    private val compositeDisposable: CompositeDisposable
) : PositionalDataSource<MovieDto>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<MovieDto>) {
        compositeDisposable += moviesRepository.findMovies(query, INITIAL_PAGE_NUMBER)
            .doOnSubscribe { recentSearchesRepository.addRecentSearch(RecentSearchParamsDto(query = query)) }
            .subscribeBy(
                onSuccess = { callback.onResult(it.movies, 0, it.totalCount) },
                onError = { callback.onResult(emptyList(), 0, 0) }
            )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MovieDto>) {
        compositeDisposable += moviesRepository.findMovies(query, params.pageNumber).subscribeBy(
            onSuccess = { callback.onResult(it.movies) },
            onError = { callback.onResult(emptyList()) }
        )
    }

    private val LoadRangeParams.pageNumber get() = INITIAL_PAGE_NUMBER + startPosition / PAGE_SIZE
}