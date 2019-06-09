package by.ve.omdbapiandroid.presentation.paging

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import by.ve.omdbapiandroid.domain.MoviesDataSourceFactory
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.view.model.MovieAdapterItem
import by.ve.omdbapiandroid.view.model.factory.MovieAdapterItemFactory


class MovieAdapterItemPagedListBuilder(
    private val moviesDataSourceFactory: MoviesDataSourceFactory,
    private val movieAdapterItemFactory: MovieAdapterItemFactory
) {

    companion object {

        private const val PAGE_SIZE = 10

        private val CONFIG = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }

    fun build(): LiveData<PagedList<MovieAdapterItem>> = LivePagedListBuilder<Int, MovieAdapterItem>(
        moviesDataSourceFactory.map(movieAdapterItemFactory::create),
        CONFIG
    ).build()

    fun updateQuery(query: SearchQueryDto) {
        moviesDataSourceFactory.query = query
    }

    fun dispose() {
        moviesDataSourceFactory.dispose()
    }
}
