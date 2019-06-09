package by.ve.omdbapiandroid.domain

import androidx.paging.PositionalDataSource
import by.ve.omdbapiandroid.repositories.MoviesRepository
import by.ve.omdbapiandroid.repositories.SearchQueriesRepository
import by.ve.omdbapiandroid.repositories.model.MovieDto
import by.ve.omdbapiandroid.repositories.model.MoviesPageDto
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import com.nhaarman.mockitokotlin2.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.SingleSubject
import org.junit.Test

private const val TEST_TOTAL_COUNT = 362

class MoviesDataSourceTest {

    private val initialMoviesList: List<MovieDto> = listOf(mock(), mock())
    private val initialPage = MoviesPageDto(
        movies = initialMoviesList,
        totalCount = TEST_TOTAL_COUNT
    )
    private val newMoviesList: List<MovieDto> = listOf(mock(), mock(), mock())
    private val newPage = MoviesPageDto(
        movies = newMoviesList,
        totalCount = TEST_TOTAL_COUNT
    )

    private val anyQueryDto: SearchQueryDto = mock()
    private val validQueryDto = SearchQueryDto(
        query = "Some query",
        year = null,
        type = null
    )
    private val emptyQueryDto = SearchQueryDto(
        query = "",
        year = null,
        type = null
    )
    private val blankQueryDto = SearchQueryDto(
        query = "     ",
        year = null,
        type = null
    )

    private val loadInitialParams: PositionalDataSource.LoadInitialParams = mock()
    private val invalidLoadRangeParams = PositionalDataSource.LoadRangeParams(16, 10)
    private val validLoadRangeParams = PositionalDataSource.LoadRangeParams(20, 10)

    private val loadInitialCallback: PositionalDataSource.LoadInitialCallback<MovieDto> = mock()
    private val loadRangeCallback: PositionalDataSource.LoadRangeCallback<MovieDto> = mock()

    private val moviesSubject = SingleSubject.create<MoviesPageDto>()
    private val moviesRepository: MoviesRepository = mock {
        on { findMovies(any(), any()) } doReturn moviesSubject
    }
    private val searchQueriesRepository: SearchQueriesRepository = mock()
    private val compositeDisposable: CompositeDisposable = mock()

    @Test
    fun `given any params when load initial page then finds first page by query in repository`() {
        // given
        val tested = createTested(anyQueryDto)

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(moviesRepository).findMovies(anyQueryDto, 1)
    }

    @Test
    fun `given any params when load initial page then adds disposable to composite`() {
        // given
        val tested = createTested(anyQueryDto)

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(compositeDisposable).add(any())
    }

    @Test
    fun `given any params and search query with query when load initial page then save query into recent searches`() {
        // given
        val tested = createTested(validQueryDto)

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(searchQueriesRepository).addRecentSearch(validQueryDto)
    }

    @Test
    fun `given any params and search query with empty query when load initial page then do not save query into recent searches`() {
        // given
        val tested = createTested(emptyQueryDto)

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(searchQueriesRepository, never()).addRecentSearch(emptyQueryDto)
    }

    @Test
    fun `given any params and search query with blank query when load initial page then do not save query into recent searches`() {
        // given
        val tested = createTested(blankQueryDto)

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(searchQueriesRepository, never()).addRecentSearch(blankQueryDto)
    }

    @Test
    fun `given initial page loading successful when load initial page then send received list of movies to result callback`() {
        // given
        val tested = createTested(validQueryDto)
        moviesSubject.onSuccess(initialPage)

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(loadInitialCallback).onResult(eq(initialMoviesList), any(), any())
    }

    @Test
    fun `given initial page loading successful when load initial page then notify result callback that data loaded from 0 position`() {
        // given
        val tested = createTested(validQueryDto)
        moviesSubject.onSuccess(initialPage)

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(loadInitialCallback).onResult(any(), eq(0), any())
    }

    @Test
    fun `given initial page loading successful when load initial page then notify result callback about the total count of items`() {
        // given
        val tested = createTested(validQueryDto)
        moviesSubject.onSuccess(initialPage)

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(loadInitialCallback).onResult(any(), any(), eq(TEST_TOTAL_COUNT))
    }

    @Test
    fun `given initial page loading failed when load initial page then send empty list of movies to result callback`() {
        // given
        val tested = createTested(validQueryDto)
        moviesSubject.onError(Throwable())

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(loadInitialCallback).onResult(eq(emptyList()), any(), any())
    }

    @Test
    fun `given initial page loading failed when load initial page then notify result callback that data loaded from 0 position`() {
        // given
        val tested = createTested(validQueryDto)
        moviesSubject.onError(Throwable())

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(loadInitialCallback).onResult(any(), eq(0), any())
    }

    @Test
    fun `given initial page loading failed when load initial page then notify result callback about zero total count of items`() {
        // given
        val tested = createTested(validQueryDto)
        moviesSubject.onError(Throwable())

        // when
        tested.loadInitial(loadInitialParams, loadInitialCallback)

        // then
        verify(loadInitialCallback).onResult(any(), any(), eq(0))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `given params with position not a multiple of 10 when load range then throw exception`() {
        // given
        val tested = createTested(anyQueryDto)

        // when
        tested.loadRange(invalidLoadRangeParams, loadRangeCallback)
    }

    @Test
    fun `given valid params when load range then add disposable to composite`() {
        // given
        val tested = createTested(anyQueryDto)

        // when
        tested.loadRange(validLoadRangeParams, loadRangeCallback)

        // then
        verify(compositeDisposable).add(any())
    }

    @Test
    fun `given params with position multiple of 10 when load range then find next page by query in repository`() {
        // given
        val tested = createTested(anyQueryDto)

        // when
        tested.loadRange(validLoadRangeParams, loadRangeCallback)

        // then
        verify(moviesRepository).findMovies(anyQueryDto, 3)
    }

    @Test
    fun `given range loading successful when load range then send received list of movies to result callback`() {
        // given
        val tested = createTested(validQueryDto)
        moviesSubject.onSuccess(newPage)

        // when
        tested.loadRange(validLoadRangeParams, loadRangeCallback)

        // then
        verify(loadRangeCallback).onResult(newMoviesList)
    }

    @Test
    fun `given range loading failed when load range then send empty list of movies to result callback`() {
        // given
        val tested = createTested(validQueryDto)
        moviesSubject.onError(Throwable())

        // when
        tested.loadRange(validLoadRangeParams, loadRangeCallback)

        // then
        verify(loadRangeCallback).onResult(emptyList())
    }

    private fun createTested(queryDto: SearchQueryDto) = MoviesDataSource(
        queryDto,
        moviesRepository,
        searchQueriesRepository,
        compositeDisposable
    )
}