package by.ve.omdbapiandroid.repositories

import by.ve.omdbapiandroid.network.SearchService
import by.ve.omdbapiandroid.parsing.model.network.MoviesPageJson
import by.ve.omdbapiandroid.repositories.model.MediaContentType.*
import by.ve.omdbapiandroid.repositories.model.MoviesPageDto
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.repositories.model.SearchResultsMapper
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_SEARCH_QUERY = "testSearchQuery"

@RunWith(JUnitParamsRunner::class)
class MoviesRepositoryImplTest {

    private val moviesPageJson: MoviesPageJson = mock()
    private val moviesPageDto: MoviesPageDto = mock()

    private val searchService: SearchService = mock {
        on { search(any(), anyOrNull(), any(), anyOrNull()) } doReturn Single.just(moviesPageJson)
    }
    private val searchResultsMapper: SearchResultsMapper = mock {
        on { mapPage(moviesPageJson) } doReturn moviesPageDto
    }

    private val tested = MoviesRepositoryImpl(searchService, searchResultsMapper)

    @Test
    fun `given movies search successful when find movies then map result to dto`() {
        // when
        val observer = tested.findMovies(SearchQueryDto.EMPTY, 1).test()

        // then
        observer.assertValue(moviesPageDto)
    }

    @Test
    @Parameters(method = "paramsForTest")
    fun `given search query dto and page when find movies then searches using service`(
        givenSearchQueryDto: SearchQueryDto,
        givenPageNumber: Int,
        expectedSearchQuery: String,
        expectedSearchYear: Int?,
        expectedSearchType: String?,
        expectedPageNumber: Int
    ) {
        // when
        tested.findMovies(givenSearchQueryDto, givenPageNumber)

        // then
        verify(searchService).search(expectedSearchQuery, expectedSearchYear, expectedPageNumber, expectedSearchType)
    }

    private fun paramsForTest() = arrayOf(
        arrayOf(SearchQueryDto(TEST_SEARCH_QUERY, null, null), 1, TEST_SEARCH_QUERY, null, null, 1),
        arrayOf(SearchQueryDto(TEST_SEARCH_QUERY, 1901, MOVIE), 2, TEST_SEARCH_QUERY, 1901, "movie", 2),
        arrayOf(SearchQueryDto(TEST_SEARCH_QUERY, 1902, SERIES), 3, TEST_SEARCH_QUERY, 1902, "series", 3),
        arrayOf(SearchQueryDto(TEST_SEARCH_QUERY, 1903, GAME), 4, TEST_SEARCH_QUERY, 1903, "game", 4)
    )
}