package by.ve.omdbapiandroid.domain

import androidx.paging.DataSource
import by.ve.omdbapiandroid.repositories.SearchQueriesRepository
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto


class RecentSearchesDataSourceFactory(searchQueriesRepository: SearchQueriesRepository) :
    DataSource.Factory<Int, SearchQueryDto>() {

    private val recentSearchesDtoFactory = searchQueriesRepository.getAllSearchQueriesListFactory()

    override fun create(): DataSource<Int, SearchQueryDto> = recentSearchesDtoFactory.create()
}