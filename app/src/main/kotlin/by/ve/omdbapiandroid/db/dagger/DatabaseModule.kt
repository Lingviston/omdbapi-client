package by.ve.omdbapiandroid.db.dagger

import android.app.Application
import androidx.room.Room
import by.ve.omdbapiandroid.db.SearchQueriesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val RECENT_SEARCHES_DB_FILE_NAME = "search_queries.db"

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideSearchQueriesDatabase(application: Application) =
        Room.databaseBuilder(application, SearchQueriesDatabase::class.java, RECENT_SEARCHES_DB_FILE_NAME).build()

    @Provides
    @Singleton
    fun provideSearchQueriesDao(database: SearchQueriesDatabase) = database.getRecentQueriesDao()
}