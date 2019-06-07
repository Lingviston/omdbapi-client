package by.ve.omdbapiandroid.repositories

import by.ve.omdbapiandroid.repositories.model.MoviesPageDto
import io.reactivex.Single


interface MoviesRepository {

    fun findMovies(query: String, pageNumber: Int): Single<MoviesPageDto>
}