package by.ve.omdbapiandroid.domain

import androidx.paging.DataSource
import by.ve.omdbapiandroid.repositories.RecentSearchesRepository
import by.ve.omdbapiandroid.repositories.model.RecentSearchDto


class RecentSearchesDataSourceFactory(recentSearchesRepository: RecentSearchesRepository) :
    DataSource.Factory<Int, RecentSearchDto>() {

    private val recentSearchesDtoFactory = recentSearchesRepository.getAllRecentSearchesListFactory()

    override fun create(): DataSource<Int, RecentSearchDto> = recentSearchesDtoFactory.create()
}