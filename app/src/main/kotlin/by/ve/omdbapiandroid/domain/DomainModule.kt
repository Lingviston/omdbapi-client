package by.ve.omdbapiandroid.domain

import by.ve.omdbapiandroid.repositories.MoviesRepository
import by.ve.omdbapiandroid.repositories.RecentSearchesRepository
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideMoviesDataSourceFactory(
        moviesRepository: MoviesRepository,
        recentSearchesRepository: RecentSearchesRepository
    ): MoviesDataSourceFactory = MoviesDataSourceFactory(moviesRepository, recentSearchesRepository)

    @Provides
    fun provideRecentSearchesDataSourceFactory(
        recentSearchesRepository: RecentSearchesRepository
    ): RecentSearchesDataSourceFactory = RecentSearchesDataSourceFactory(recentSearchesRepository)
}