package by.ve.omdbapiandroid.repositories.model

import by.ve.omdbapiandroid.parsing.model.network.MovieJson
import by.ve.omdbapiandroid.parsing.model.network.MoviesPageJson

private const val SUPPORTED_TOTAL_COUNT = 1000

class SearchResultsMapperImpl : SearchResultsMapper {

    override fun mapPage(moviesPageJson: MoviesPageJson): MoviesPageDto =
        MoviesPageDto(
            movies = moviesPageJson.results.map(::mapMovie),
            totalCount = if (moviesPageJson.totalCount > SUPPORTED_TOTAL_COUNT) {
                SUPPORTED_TOTAL_COUNT
            } else {
                moviesPageJson.totalCount
            }
        )

    override fun mapMovie(movieJson: MovieJson): MovieDto =
        MovieDto(
            title = movieJson.title,
            year = movieJson.year,
            posterUrl = movieJson.posterUrl
        )
}