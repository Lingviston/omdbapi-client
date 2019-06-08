package by.ve.omdbapiandroid.db

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val RECENT_SEARCHES_DB_FILE_NAME = "recent_searches.db"

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRecentSearchesDatabase(application: Application) =
        Room.databaseBuilder(application, RecentSearchesDatabase::class.java, RECENT_SEARCHES_DB_FILE_NAME).build()

    @Provides
    @Singleton
    fun provideRecentSearchesDao(database: RecentSearchesDatabase) = database.getRecentSearchesDao()
}