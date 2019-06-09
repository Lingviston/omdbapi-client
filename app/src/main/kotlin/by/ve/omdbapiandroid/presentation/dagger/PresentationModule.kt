package by.ve.omdbapiandroid.presentation.dagger

import android.app.Application
import by.ve.omdbapiandroid.domain.MoviesDataSourceFactory
import by.ve.omdbapiandroid.domain.RecentSearchesDataSourceFactory
import by.ve.omdbapiandroid.presentation.paging.MovieAdapterItemPagedListBuilder
import by.ve.omdbapiandroid.presentation.paging.SearchQueryAdapterItemPagedListBuilder
import by.ve.omdbapiandroid.presentation.viewmodel.FilterViewModel
import by.ve.omdbapiandroid.view.model.factory.MovieAdapterItemFactory
import by.ve.omdbapiandroid.view.model.factory.SearchQueryAdapterItemFactory
import dagger.Module
import dagger.Provides

private const val DEFAULT_PAGE_SIZE = 10

@Module
class PresentationModule {

    @Provides
    fun provideMovieAdapterItemPagedListBuilder(
        moviesDataSourceFactory: MoviesDataSourceFactory,
        movieAdapterItemFactory: MovieAdapterItemFactory
    ) = MovieAdapterItemPagedListBuilder(moviesDataSourceFactory, movieAdapterItemFactory)

    @Provides
    fun provideSearchQueryAdapterItemPagedListBuilder(
        recentSearchesDataSourceFactory: RecentSearchesDataSourceFactory,
        searchQueryAdapterItemFactory: SearchQueryAdapterItemFactory
    ) = SearchQueryAdapterItemPagedListBuilder(recentSearchesDataSourceFactory, searchQueryAdapterItemFactory)

    @Provides
    fun provideFilterViewModel(application: Application) = FilterViewModel(application)
}
