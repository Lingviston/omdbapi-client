package by.ve.omdbapiandroid.repositories.model

import by.ve.omdbapiandroid.parsing.model.network.MovieJson
import by.ve.omdbapiandroid.parsing.model.network.MoviesPageJson


interface SearchResultsMapper {

    fun mapPage(moviesPageJson: MoviesPageJson): MoviesPageDto

    fun mapMovie(movieJson: MovieJson): MovieDto
}
