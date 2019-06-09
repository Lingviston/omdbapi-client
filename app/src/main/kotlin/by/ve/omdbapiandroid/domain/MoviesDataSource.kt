package by.ve.omdbapiandroid.domain

import androidx.paging.PositionalDataSource
import by.ve.omdbapiandroid.repositories.MoviesRepository
import by.ve.omdbapiandroid.repositories.SearchQueriesRepository
import by.ve.omdbapiandroid.repositories.model.MovieDto
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

private const val INITIAL_PAGE_NUMBER = 1
private const val PAGE_SIZE = 10

class MoviesDataSource(
    private val queryDto: SearchQueryDto,
    private val moviesRepository: MoviesRepository,
    private val searchQueriesRepository: SearchQueriesRepository,
    private val compositeDisposable: CompositeDisposable
) : PositionalDataSource<MovieDto>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<MovieDto>) {
        compositeDisposable += moviesRepository.findMovies(queryDto, INITIAL_PAGE_NUMBER)
            .doOnSubscribe { saveSearchQueryIfValid() }
            .subscribeBy(
                onSuccess = { callback.onResult(it.movies, 0, it.totalCount) },
                onError = { callback.onResult(emptyList(), 0, 0) }
            )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MovieDto>) {
        if (params.startPosition % PAGE_SIZE != 0) {
            throw IllegalArgumentException("Please, enable placeholders and set page size to 10.")
        }

        compositeDisposable += moviesRepository.findMovies(queryDto, params.pageNumber).subscribeBy(
            onSuccess = { callback.onResult(it.movies) },
            onError = { callback.onResult(emptyList()) }
        )
    }

    private fun saveSearchQueryIfValid() {
        if (queryDto.query.isNotBlank()) {
            searchQueriesRepository.addRecentSearch(queryDto)
        }
    }

    private val LoadRangeParams.pageNumber get() = INITIAL_PAGE_NUMBER + startPosition / PAGE_SIZE
}
