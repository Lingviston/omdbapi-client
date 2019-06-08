package by.ve.omdbapiandroid.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecentSearchesDao {

    @Query("SELECT * from RecentSearches ORDER BY timestamp DESC")
    fun getAll(): DataSource.Factory<Int, RecentSearchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecentSearch(recentSearchEntity: RecentSearchEntity)
}