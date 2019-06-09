package by.ve.omdbapiandroid.repositories.model

import by.ve.omdbapiandroid.parsing.model.network.MovieJson
import by.ve.omdbapiandroid.parsing.model.network.MoviesPageJson
import org.junit.Assert.assertEquals
import org.junit.Test


class SearchResultsMapperImplTest {

    private val tested = SearchResultsMapperImpl()

    @Test
    fun `given movie json when map movie then return correct movie dto`() {
        // given
        val json = MovieJson(title = "Title", year = "Year", imdbId = "imdbId", type = "movie", posterUrl = "posterUrl")

        // when
        val dto = tested.mapMovie(json)

        // then
        assertEquals(MovieDto(title = "Title", year = "Year", posterUrl = "posterUrl"), dto)
    }

    @Test
    fun `given movie page json with less than 1000 total count when map movie page then returns dto with real total count`() {
        // given
        val json = MoviesPageJson(
            results = listOf(
                MovieJson(
                    title = "Title",
                    year = "Year",
                    imdbId = "imdbId",
                    type = "movie",
                    posterUrl = "posterUrl"
                )
            ),
            totalCount = 999
        )

        // when
        val dto = tested.mapPage(json)

        // then
        assertEquals(
            MoviesPageDto(
                movies = listOf(MovieDto(title = "Title", year = "Year", posterUrl = "posterUrl")),
                totalCount = 999
            ),
            dto
        )
    }

    @Test
    fun `given movie page json with 1000 total count when map movie page then returns dto with 1000 total count`() {
        // given
        val json = MoviesPageJson(
            results = listOf(
                MovieJson(
                    title = "Title",
                    year = "Year",
                    imdbId = "imdbId",
                    type = "movie",
                    posterUrl = "posterUrl"
                )
            ),
            totalCount = 1000
        )

        // when
        val dto = tested.mapPage(json)

        // then
        assertEquals(
            MoviesPageDto(
                movies = listOf(MovieDto(title = "Title", year = "Year", posterUrl = "posterUrl")),
                totalCount = 1000
            ),
            dto
        )
    }

    @Test
    fun `given movie page json with more than 1000 total count when map movie page then returns dto with 1000 total count`() {
        // given
        val json = MoviesPageJson(
            results = listOf(
                MovieJson(
                    title = "Title",
                    year = "Year",
                    imdbId = "imdbId",
                    type = "movie",
                    posterUrl = "posterUrl"
                )
            ),
            totalCount = 1001
        )

        // when
        val dto = tested.mapPage(json)

        // then
        assertEquals(
            MoviesPageDto(
                movies = listOf(MovieDto(title = "Title", year = "Year", posterUrl = "posterUrl")),
                totalCount = 1000
            ),
            dto
        )
    }
}