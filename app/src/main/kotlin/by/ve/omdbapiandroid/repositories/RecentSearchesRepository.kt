package by.ve.omdbapiandroid.repositories

import androidx.paging.DataSource
import by.ve.omdbapiandroid.repositories.model.RecentSearchDto
import by.ve.omdbapiandroid.repositories.model.RecentSearchParamsDto


interface RecentSearchesRepository {

    fun getAllRecentSearchesListFactory(): DataSource.Factory<Int, RecentSearchDto>

    fun addRecentSearch(paramsDto: RecentSearchParamsDto)
}