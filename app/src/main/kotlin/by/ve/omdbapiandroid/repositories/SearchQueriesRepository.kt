package by.ve.omdbapiandroid.repositories

import androidx.paging.DataSource
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto


interface SearchQueriesRepository {

    fun getAllSearchQueriesListFactory(): DataSource.Factory<Int, SearchQueryDto>

    fun addRecentSearch(searchQuery: SearchQueryDto)
}