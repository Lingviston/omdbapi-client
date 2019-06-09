package by.ve.omdbapiandroid.presentation.paging

import by.ve.omdbapiandroid.domain.MoviesDataSourceFactory
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.view.model.factory.MovieAdapterItemFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test


class MovieAdapterItemPagedListBuilderTest {

    private val moviesDataSourceFactory: MoviesDataSourceFactory = mock()
    private val movieAdapterItemFactory: MovieAdapterItemFactory = mock()

    private val tested = MovieAdapterItemPagedListBuilder(moviesDataSourceFactory, movieAdapterItemFactory)

    @Test
    fun `given search query when update query then update query in factory`() {
        // given
        val searchQueryDto: SearchQueryDto = mock()

        // when
        tested.updateQuery(searchQueryDto)

        // then
        verify(moviesDataSourceFactory).query = searchQueryDto
    }

    @Test
    fun `when dispose then dispose factory`() {
        // when
        tested.dispose()

        // then
        moviesDataSourceFactory.dispose()
    }
}