package by.ve.omdbapiandroid.repositories

import by.ve.omdbapiandroid.network.SearchService
import by.ve.omdbapiandroid.repositories.model.SearchResultsMapper
import by.ve.omdbapiandroid.repositories.model.SearchResultsMapperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoriesModule {

    @Provides
    fun provideSearchResultsMapper(): SearchResultsMapper = SearchResultsMapperImpl()

    @Provides
    @Singleton
    fun provideMoviesRepository(
        searchService: SearchService,
        searchResultsMapper: SearchResultsMapper
    ): MoviesRepository = MoviesRepositoryImpl(searchService, searchResultsMapper)
}