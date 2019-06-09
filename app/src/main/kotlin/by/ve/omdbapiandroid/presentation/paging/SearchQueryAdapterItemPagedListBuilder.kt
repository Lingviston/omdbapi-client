package by.ve.omdbapiandroid.presentation.paging

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import by.ve.omdbapiandroid.domain.RecentSearchesDataSourceFactory
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.view.model.SearchQueryAdapterItem
import by.ve.omdbapiandroid.view.model.factory.SearchQueryAdapterItemFactory

private const val PAGE_SIZE = 10

class SearchQueryAdapterItemPagedListBuilder(
    private val recentSearchesDataSourceFactory: RecentSearchesDataSourceFactory,
    private val searchQueryAdapterItemFactory: SearchQueryAdapterItemFactory
) {

    fun build(onClickAction: (SearchQueryDto) -> Unit): LiveData<PagedList<SearchQueryAdapterItem>> =
        LivePagedListBuilder<Int, SearchQueryAdapterItem>(recentSearchesDataSourceFactory.map {
            searchQueryAdapterItemFactory.create(it, onClickAction)
        }, PAGE_SIZE).build()
}