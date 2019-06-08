package by.ve.omdbapiandroid.repositories

import androidx.paging.DataSource
import by.ve.omdbapiandroid.db.RecentSearchEntity
import by.ve.omdbapiandroid.db.RecentSearchesDao
import by.ve.omdbapiandroid.repositories.model.RecentSearchDto
import by.ve.omdbapiandroid.repositories.model.RecentSearchParamsDto

class RecentSearchesRepositoryImpl(private val recentSearchesDao: RecentSearchesDao) : RecentSearchesRepository {

    override fun getAllRecentSearchesListFactory(): DataSource.Factory<Int, RecentSearchDto> =
        recentSearchesDao.getAll().map {
            RecentSearchDto(
                uid = it.uid!!,
                params = RecentSearchParamsDto(it.query)
            )
        }

    override fun addRecentSearch(paramsDto: RecentSearchParamsDto) {
        recentSearchesDao.addRecentSearch(RecentSearchEntity(query = paramsDto.query))
    }
}