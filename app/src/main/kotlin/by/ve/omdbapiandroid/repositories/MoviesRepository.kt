package by.ve.omdbapiandroid.repositories

import by.ve.omdbapiandroid.repositories.model.MoviesPageDto
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import io.reactivex.Single


interface MoviesRepository {

    fun findMovies(queryDto: SearchQueryDto, pageNumber: Int): Single<MoviesPageDto>
}
