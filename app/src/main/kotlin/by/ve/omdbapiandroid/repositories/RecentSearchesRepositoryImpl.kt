package by.ve.omdbapiandroid.repositories

import androidx.paging.DataSource
import by.ve.omdbapiandroid.db.RecentSearchEntity
import by.ve.omdbapiandroid.db.RecentSearchesDao
import by.ve.omdbapiandroid.repositories.model.RecentSearchDto

class RecentSearchesRepositoryImpl(private val recentSearchesDao: RecentSearchesDao) : RecentSearchesRepository {

    override fun getAllRecentSearchesListFactory(): DataSource.Factory<Int, RecentSearchDto> =
        recentSearchesDao.getAll().map {
            RecentSearchDto(query = it.query)
        }

    override fun addRecentSearch(recentSearchDto: RecentSearchDto) {
        recentSearchesDao.addRecentSearch(
            RecentSearchEntity(
                query = recentSearchDto.query,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}