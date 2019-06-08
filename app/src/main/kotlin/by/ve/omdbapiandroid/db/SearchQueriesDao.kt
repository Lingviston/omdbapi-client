package by.ve.omdbapiandroid.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchQueriesDao {

    @Query("SELECT * from SearchQueries ORDER BY timestamp DESC")
    fun getAll(): DataSource.Factory<Int, SearchQueryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecentSearch(searchQueryEntity: SearchQueryEntity)
}