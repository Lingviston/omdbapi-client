package by.ve.omdbapiandroid.domain

import by.ve.omdbapiandroid.repositories.MoviesRepository
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideMoviesDataSourceFactory(moviesRepository: MoviesRepository): MoviesDataSourceFactory =
        MoviesDataSourceFactory(moviesRepository)
}