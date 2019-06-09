package by.ve.omdbapiandroid.repositories

import androidx.paging.DataSource
import by.ve.omdbapiandroid.db.SearchQueriesDao
import by.ve.omdbapiandroid.db.SearchQueryEntity
import by.ve.omdbapiandroid.repositories.model.MediaContentType
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto

private const val ANY_YEAR = -1
private const val ANY_TYPE = ""

class SearchQueriesRepositoryImpl(private val searchQueriesDao: SearchQueriesDao) : SearchQueriesRepository {

    override fun getAllSearchQueriesListFactory(): DataSource.Factory<Int, SearchQueryDto> =
        searchQueriesDao.getAll().map { entity ->
            SearchQueryDto(
                query = entity.query,
                year = entity.year.takeIf { it != ANY_YEAR },
                type = if (entity.type == ANY_TYPE) null else MediaContentType.valueOf(entity.type)
            )
        }

    override fun addRecentSearch(searchQuery: SearchQueryDto) {
        searchQueriesDao.addRecentSearch(
            SearchQueryEntity(
                query = searchQuery.query,
                year = searchQuery.year ?: ANY_YEAR,
                type = searchQuery.type?.name ?: ANY_TYPE,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}