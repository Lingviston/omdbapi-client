package by.ve.omdbapiandroid.repositories

import by.ve.omdbapiandroid.network.SearchService
import by.ve.omdbapiandroid.repositories.model.MediaContentType
import by.ve.omdbapiandroid.repositories.model.MoviesPageDto
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.repositories.model.SearchResultsMapper
import io.reactivex.Single

private const val TYPE_MOVIE = "movie"
private const val TYPE_SERIES = "series"
private const val TYPE_GAME = "game"

class MoviesRepositoryImpl(
    private val searchService: SearchService,
    private val searchResultsMapper: SearchResultsMapper
) : MoviesRepository {

    override fun findMovies(queryDto: SearchQueryDto, pageNumber: Int): Single<MoviesPageDto> =
        searchService.search(
            query = queryDto.query,
            year = queryDto.year,
            page = pageNumber,
            type = queryDto.type?.toNetworkType()
        ).map {
            searchResultsMapper.mapPage(it)
        }

    private fun MediaContentType.toNetworkType(): String {
        return when (this) {
            MediaContentType.MOVIE -> TYPE_MOVIE
            MediaContentType.SERIES -> TYPE_SERIES
            MediaContentType.GAME -> TYPE_GAME
        }
    }
}