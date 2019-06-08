package by.ve.omdbapiandroid.repositories

import androidx.paging.DataSource
import by.ve.omdbapiandroid.repositories.model.RecentSearchDto


interface RecentSearchesRepository {

    fun getAllRecentSearchesListFactory(): DataSource.Factory<Int, RecentSearchDto>

    fun addRecentSearch(recentSearchDto: RecentSearchDto)
}