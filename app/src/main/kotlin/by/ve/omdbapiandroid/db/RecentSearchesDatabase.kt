package by.ve.omdbapiandroid.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RecentSearchEntity::class], version = 1, exportSchema = false)
abstract class RecentSearchesDatabase : RoomDatabase() {

    abstract fun getRecentSearchesDao(): RecentSearchesDao
}