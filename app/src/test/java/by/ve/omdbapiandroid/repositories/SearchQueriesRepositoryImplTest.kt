package by.ve.omdbapiandroid.repositories

import by.ve.omdbapiandroid.db.SearchQueriesDao
import by.ve.omdbapiandroid.db.SearchQueryEntity
import by.ve.omdbapiandroid.repositories.model.MediaContentType.*
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.repositories.utils.CurrentTimeProvider
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith

private const val CURRENT_TIME_MS = 1883L

@RunWith(JUnitParamsRunner::class)
class SearchQueriesRepositoryImplTest {

    private val searchQueriesDao: SearchQueriesDao = mock()
    private val currentTimeProvider: CurrentTimeProvider = mock {
        on { getCurrentTimeMs() } doReturn CURRENT_TIME_MS
    }

    private val tested = SearchQueriesRepositoryImpl(searchQueriesDao, currentTimeProvider)

    @Test
    @Parameters(method = "paramsForTest")
    fun `given search query when add recent search then saves correctly converted data`(
        givenSearchQueryDto: SearchQueryDto,
        expectedSearchQueryEntity: SearchQueryEntity
    ) {
        //given/when
        tested.addRecentSearch(givenSearchQueryDto)

        // then
        verify(searchQueriesDao).addRecentSearch(expectedSearchQueryEntity)
    }

    private fun paramsForTest() = arrayOf(
        arrayOf(SearchQueryDto("Title", null, null), SearchQueryEntity("Title", -1, "", CURRENT_TIME_MS)),
        arrayOf(SearchQueryDto("Title", 1901, MOVIE), SearchQueryEntity("Title", 1901, "MOVIE", CURRENT_TIME_MS)),
        arrayOf(SearchQueryDto("Title", 1911, SERIES), SearchQueryEntity("Title", 1911, "SERIES", CURRENT_TIME_MS)),
        arrayOf(SearchQueryDto("Title", 2000, GAME), SearchQueryEntity("Title", 2000, "GAME", CURRENT_TIME_MS))
    )
}