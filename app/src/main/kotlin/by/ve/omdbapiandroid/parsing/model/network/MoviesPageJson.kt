package by.ve.omdbapiandroid.parsing.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesPageJson(
    @Json(name = "Search") val results: List<MovieJson>,
    @Json(name = "totalResults") val totalCount: Int
)
