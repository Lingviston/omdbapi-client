package by.ve.omdbapiandroid.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchQueryEntity::class], version = 1, exportSchema = false)
abstract class SearchQueriesDatabase : RoomDatabase() {

    abstract fun getRecentQueriesDao(): SearchQueriesDao
}