package by.ve.omdbapiandroid.domain

import by.ve.omdbapiandroid.repositories.MoviesRepository
import by.ve.omdbapiandroid.repositories.SearchQueriesRepository
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideMoviesDataSourceFactory(
        moviesRepository: MoviesRepository,
        searchQueriesRepository: SearchQueriesRepository
    ): MoviesDataSourceFactory = MoviesDataSourceFactory(moviesRepository, searchQueriesRepository)

    @Provides
    fun provideRecentSearchesDataSourceFactory(
        searchQueriesRepository: SearchQueriesRepository
    ): RecentSearchesDataSourceFactory = RecentSearchesDataSourceFactory(searchQueriesRepository)
}