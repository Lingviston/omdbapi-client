package by.ve.omdbapiandroid.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import by.ve.omdbapiandroid.presentation.paging.MovieAdapterItemPagedListBuilder
import by.ve.omdbapiandroid.presentation.paging.SearchQueryAdapterItemPagedListBuilder
import by.ve.omdbapiandroid.presentation.viewmodel.model.FilterParams
import by.ve.omdbapiandroid.repositories.model.MediaContentType
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.rule.RxTestRule
import by.ve.omdbapiandroid.view.model.MovieAdapterItem
import by.ve.omdbapiandroid.view.model.SearchQueryAdapterItem
import com.nhaarman.mockitokotlin2.*
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(Enclosed::class)
class MoviesListViewModelTest {

    abstract class BaseTest {

        @get:Rule
        val liveDataRule = InstantTaskExecutorRule()

        protected lateinit var onRecentQueryClick: (SearchQueryDto) -> Unit

        protected val filterParamsSubject = BehaviorSubject.create<FilterParams>()
        protected val filterViewModel: FilterViewModel = mock {
            on { appliedFilterParams } doReturn filterParamsSubject
        }
        protected val moviesPagedListLiveData: LiveData<PagedList<MovieAdapterItem>> = mock()
        protected val movieAdapterItemPagedListBuilder: MovieAdapterItemPagedListBuilder = mock {
            on { build() } doReturn moviesPagedListLiveData
        }
        protected val searchQueriesPagedListLiveData: LiveData<PagedList<SearchQueryAdapterItem>> = mock()
        protected val searchQueryAdapterItemPagedListBuilder: SearchQueryAdapterItemPagedListBuilder = mock {
            on { build(any()) } doAnswer {
                onRecentQueryClick = it.arguments[0] as (SearchQueryDto) -> Unit
                searchQueriesPagedListLiveData
            }
        }

        protected val tested by lazy {
            MoviesListViewModel(
                filterViewModel,
                movieAdapterItemPagedListBuilder,
                searchQueryAdapterItemPagedListBuilder
            )
        }

        protected fun TestScheduler.advanceTimeByMs(delayMs: Int) {
            advanceTimeBy(delayMs.toLong(), TimeUnit.MILLISECONDS)
        }
    }

    class InitialStateTest : BaseTest() {

        @get:Rule
        val rxRule = RxTestRule()

        @Test
        fun `when created then movies paged list live data initialized`() {
            // when
            tested

            // then
            assertEquals(moviesPagedListLiveData, tested.movies)
        }

        @Test
        fun `when created then recent queries paged list live data initialized`() {
            // when
            tested

            // then
            assertEquals(searchQueriesPagedListLiveData, tested.recentQueries)
        }

        @Test
        fun `when created then recent searches closed`() {
            // when
            tested

            // then
            assertEquals(false, tested.recentSearchesOpen.value)
        }

        @Test
        fun `when created then query is empty`() {
            // when
            tested

            // then
            assertEquals("", tested.query.value)
        }
    }

    class QueryChangeText : BaseTest() {

        private val testScheduler = TestScheduler()

        @get:Rule
        val rxRule = RxTestRule(testScheduler)

        @Before
        fun `ignore initial state emission`() {
            tested
            testScheduler.advanceTimeByMs(1000)
            clearInvocations(filterViewModel, movieAdapterItemPagedListBuilder, searchQueryAdapterItemPagedListBuilder)
        }

        @Test
        fun `given search query submitted when less than 500ms left then do not search new query`() {
            // given
            tested.onSearchQuerySubmit("New query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            verify(movieAdapterItemPagedListBuilder, never()).updateQuery(argThat {
                query == "New query"
            })
        }

        @Test
        fun `given search query submitted when less than 500ms left then do not display new query`() {
            // given
            tested.onSearchQuerySubmit("New query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            assertEquals("", tested.query.value)
        }

        @Test
        fun `given search query submitted when less than 500ms left then do not update filter viewmodel with new params`() {
            // given
            tested.onSearchQuerySubmit("New query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            verify(filterViewModel, never()).onFilterParamsRestored(any())
        }

        @Test
        fun `given search query submitted when more than 500ms left then search new query`() {
            // given
            tested.onSearchQuerySubmit("New query")

            // when
            testScheduler.advanceTimeByMs(501)

            // then
            verify(movieAdapterItemPagedListBuilder).updateQuery(argThat {
                query == "New query"
            })
        }

        @Test
        fun `given search query submitted when more than 500ms left then display new query`() {
            // given
            tested.onSearchQuerySubmit("New query")

            // when
            testScheduler.advanceTimeByMs(501)

            // then
            assertEquals("New query", tested.query.value)
        }

        @Test
        fun `given search query submitted when more than 500ms left then update filter viewmodel with new params`() {
            // given
            tested.onSearchQuerySubmit("New query")

            // when
            testScheduler.advanceTimeByMs(501)

            // then
            verify(filterViewModel).onFilterParamsRestored(FilterParams.EMPTY)
        }

        @Test
        fun `given search query submitted and after less than 500ms another query submitted when less than 500ms left since another query then do not search original query`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("Another query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            verify(movieAdapterItemPagedListBuilder, never()).updateQuery(argThat {
                query == "New query"
            })
        }

        @Test
        fun `given search query submitted and after less than 500ms another query submitted when less than 500ms left since another query then do not search another query`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("Another query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            verify(movieAdapterItemPagedListBuilder, never()).updateQuery(argThat {
                query == "Another query"
            })
        }

        @Test
        fun `given search query submitted and after less than 500ms another query submitted when less than 500ms left since another query then do not display any of those queries`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("Another query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            assertEquals("", tested.query.value)
        }

        @Test
        fun `given search query submitted and after less than 500ms another query submitted when less than 500ms left since another query then do not update filter viewmodel with new params`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("Another query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            verify(filterViewModel, never()).onFilterParamsRestored(any())
        }

        @Test
        fun `given search query submitted and after less than 500ms the same query submitted again when 500ms left since first submission then search the query`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("New query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            verify(movieAdapterItemPagedListBuilder).updateQuery(argThat {
                query == "New query"
            })
        }

        @Test
        fun `given search query submitted and after less than 500ms the same query submitted again when 500ms left since first submission then display the query`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("New query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            assertEquals("New query", tested.query.value)
        }

        @Test
        fun `given search query submitted and after less than 500ms the same query submitted again when 500ms left since first submission then update filter viewmodel with new params`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("New query")

            // when
            testScheduler.advanceTimeByMs(250)

            // then
            verify(filterViewModel).onFilterParamsRestored(FilterParams.EMPTY)
        }

        @Test
        fun `given search query submitted and after less than 500ms another query submitted when more than 500ms left since another query then do not search original query`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("Another query")

            // when
            testScheduler.advanceTimeByMs(501)

            // then
            verify(movieAdapterItemPagedListBuilder, never()).updateQuery(argThat {
                query == "New query"
            })
        }

        @Test
        fun `given search query submitted and after less than 500ms another query submitted when more than 500ms left since another query then search another query`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("Another query")

            // when
            testScheduler.advanceTimeByMs(501)

            // then
            verify(movieAdapterItemPagedListBuilder).updateQuery(argThat {
                query == "Another query"
            })
        }

        @Test
        fun `given search query submitted and after less than 500ms another query submitted when more than 500ms left since another query then display another query`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("Another query")

            // when
            testScheduler.advanceTimeByMs(501)

            // then
            assertEquals("Another query", tested.query.value)
        }

        @Test
        fun `given search query submitted and after less than 500ms another query submitted when more than 500ms left since another query then update filter viewmodel with new params`() {
            // given
            tested.onSearchQuerySubmit("New query")
            testScheduler.advanceTimeByMs(250)
            tested.onSearchQuerySubmit("Another query")

            // when
            testScheduler.advanceTimeByMs(501)

            // then
            verify(filterViewModel).onFilterParamsRestored(FilterParams.EMPTY)
        }

        @Test
        fun `given the same search query submitted when more than 500ms left then do not search new query`() {
            // given
            tested.onSearchQuerySubmit("")

            // when
            testScheduler.advanceTimeByMs(501)

            // then
            verify(movieAdapterItemPagedListBuilder, never()).updateQuery(argThat {
                query == ""
            })
        }

        @Test
        fun `given search query submitted when more than 500ms left then do not update filter viewmodel with new params`() {
            // given
            tested.onSearchQuerySubmit("")

            // when
            testScheduler.advanceTimeByMs(501)

            // then
            verify(filterViewModel, never()).onFilterParamsRestored(any())
        }
    }

    abstract class FirstEmissionIgnoringTest : BaseTest() {

        @get:Rule
        val rxRule = RxTestRule()

        @Before
        fun `ignore initial state emission`() {
            tested
            clearInvocations(filterViewModel, movieAdapterItemPagedListBuilder, searchQueryAdapterItemPagedListBuilder)
        }
    }

    class FilterParamsUpdateTest : FirstEmissionIgnoringTest() {

        @Test
        fun `when new filter params applied then search old query with new params`() {
            // when
            filterParamsSubject.onNext(FilterParams(1901, MediaContentType.MOVIE))

            // then
            movieAdapterItemPagedListBuilder.updateQuery(SearchQueryDto("", 1901, MediaContentType.MOVIE))
        }

        @Test
        fun `when new filter params applied then display old search query`() {
            // when
            filterParamsSubject.onNext(FilterParams(1901, MediaContentType.MOVIE))

            // then
            assertEquals("", tested.query.value)
        }

        @Test
        fun `when new filter params applied then update filter viewmodel with new params`() {
            // when
            filterParamsSubject.onNext(FilterParams(1901, MediaContentType.MOVIE))

            // then
            verify(filterViewModel).onFilterParamsRestored(FilterParams(1901, MediaContentType.MOVIE))
        }
    }

    class RecentSearchSelectionTest : FirstEmissionIgnoringTest() {

        @Test
        fun `given recent search query when it is selected then search it`() {
            // given
            val recentSearchQuery = SearchQueryDto("New query", 1906, MediaContentType.SERIES)

            // when
            onRecentQueryClick.invoke(recentSearchQuery)

            // then
            movieAdapterItemPagedListBuilder.updateQuery(recentSearchQuery)
        }

        @Test
        fun `given recent search query when it is selected then display its query`() {
            // given
            val recentSearchQuery = SearchQueryDto("New query", 1906, MediaContentType.SERIES)

            // when
            onRecentQueryClick.invoke(recentSearchQuery)

            // then
            assertEquals("New query", tested.query.value)
        }

        @Test
        fun `given recent search query when it is selected then update filter viewmodel with its params`() {
            // given
            val recentSearchQuery = SearchQueryDto("New query", 1906, MediaContentType.SERIES)

            // when
            onRecentQueryClick.invoke(recentSearchQuery)

            // then
            verify(filterViewModel).onFilterParamsRestored(FilterParams(1906, MediaContentType.SERIES))
        }
    }

    class OnClearedTest : BaseTest() {

        @Test
        fun `when cleared then clears filter viewmodel`() {
            // when
            tested.onCleared()

            // then
            verify(filterViewModel).onCleared()
        }

        @Test
        fun `when cleared then disposes movie paged list builder`() {
            // when
            tested.onCleared()

            // then
            verify(movieAdapterItemPagedListBuilder).dispose()
        }
    }
}