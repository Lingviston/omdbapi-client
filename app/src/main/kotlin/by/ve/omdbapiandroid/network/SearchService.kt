package by.ve.omdbapiandroid.network

import by.ve.omdbapiandroid.parsing.model.network.MoviesPageJson
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET(".")
    fun search(
        @Query("s") query: String,
        @Query("page") int: Int,
        @Query("type") type: String
    ): Single<MoviesPageJson>
}