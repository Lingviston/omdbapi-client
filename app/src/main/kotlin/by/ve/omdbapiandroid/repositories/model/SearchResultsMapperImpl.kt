package by.ve.omdbapiandroid.repositories.model

import by.ve.omdbapiandroid.parsing.model.network.MovieJson
import by.ve.omdbapiandroid.parsing.model.network.MoviesPageJson
import kotlin.math.min

private const val SUPPORTED_TOTAL_COUNT = 1000

class SearchResultsMapperImpl : SearchResultsMapper {

    override fun mapPage(moviesPageJson: MoviesPageJson): MoviesPageDto =
        MoviesPageDto(
            movies = moviesPageJson.results.map(::mapMovie),
            totalCount = min(SUPPORTED_TOTAL_COUNT, moviesPageJson.totalCount)
        )

    override fun mapMovie(movieJson: MovieJson): MovieDto =
        MovieDto(
            title = movieJson.title,
            year = movieJson.year,
            posterUrl = movieJson.posterUrl
        )
}
