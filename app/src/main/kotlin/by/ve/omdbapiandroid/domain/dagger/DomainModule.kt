package by.ve.omdbapiandroid.domain.dagger

import by.ve.omdbapiandroid.domain.MoviesDataSourceFactory
import by.ve.omdbapiandroid.domain.RecentSearchesDataSourceFactory
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
