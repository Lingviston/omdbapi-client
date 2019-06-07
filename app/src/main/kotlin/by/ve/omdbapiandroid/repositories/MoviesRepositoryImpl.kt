package by.ve.omdbapiandroid.repositories

import by.ve.omdbapiandroid.network.SearchService
import by.ve.omdbapiandroid.repositories.model.MoviesPageDto
import by.ve.omdbapiandroid.repositories.model.SearchResultsMapper
import io.reactivex.Single

private const val TYPE_MOVIE = "movie"

class MoviesRepositoryImpl(
    private val searchService: SearchService,
    private val searchResultsMapper: SearchResultsMapper
) : MoviesRepository {

    override fun findMovies(query: String, pageNumber: Int): Single<MoviesPageDto> =
        searchService.search(query, pageNumber, TYPE_MOVIE).map(searchResultsMapper::mapPage)
}