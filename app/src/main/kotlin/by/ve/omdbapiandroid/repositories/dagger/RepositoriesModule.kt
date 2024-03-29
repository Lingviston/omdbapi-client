package by.ve.omdbapiandroid.repositories.dagger

import by.ve.omdbapiandroid.db.SearchQueriesDao
import by.ve.omdbapiandroid.network.SearchService
import by.ve.omdbapiandroid.repositories.MoviesRepository
import by.ve.omdbapiandroid.repositories.MoviesRepositoryImpl
import by.ve.omdbapiandroid.repositories.SearchQueriesRepository
import by.ve.omdbapiandroid.repositories.SearchQueriesRepositoryImpl
import by.ve.omdbapiandroid.repositories.model.SearchResultsMapper
import by.ve.omdbapiandroid.repositories.model.SearchResultsMapperImpl
import by.ve.omdbapiandroid.repositories.utils.CurrentTimeProvider
import by.ve.omdbapiandroid.repositories.utils.CurrentTimeProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Provides
    fun provideCurrentTimeProvider(): CurrentTimeProvider = CurrentTimeProviderImpl()

    @Provides
    fun provideSearchResultsMapper(): SearchResultsMapper = SearchResultsMapperImpl()

    @Provides
    @Singleton
    fun provideMoviesRepository(
        searchService: SearchService,
        searchResultsMapper: SearchResultsMapper
    ): MoviesRepository = MoviesRepositoryImpl(searchService, searchResultsMapper)

    @Provides
    @Singleton
    fun provideSearchQueriesRepository(
        searchQueriesDao: SearchQueriesDao,
        currentTimeProvider: CurrentTimeProvider
    ): SearchQueriesRepository = SearchQueriesRepositoryImpl(searchQueriesDao, currentTimeProvider)
}
