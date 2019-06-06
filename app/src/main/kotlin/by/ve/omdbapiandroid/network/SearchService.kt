package by.ve.omdbapiandroid.network

import by.ve.omdbapiandroid.parsing.model.network.SearchResponseJson
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface SearchService {

    @GET(".")
    fun search(@Query("s") query: String): Single<SearchResponseJson>
}