package by.ve.omdbapiandroid.view.dagger

import android.app.Application
import by.ve.omdbapiandroid.view.model.factory.MovieAdapterItemFactory
import by.ve.omdbapiandroid.view.model.factory.MovieAdapterItemFactoryImpl
import by.ve.omdbapiandroid.view.model.factory.SearchQueryAdapterItemFactory
import by.ve.omdbapiandroid.view.model.factory.SearchQueryAdapterItemFactoryImpl
import dagger.Module
import dagger.Provides

@Module(includes = [MainActivityModule::class])
class ViewModule {

    @Provides
    fun provideMovieAdapterItemFactory(): MovieAdapterItemFactory = MovieAdapterItemFactoryImpl()

    @Provides
    fun provideSearchQueryAdapterItemFactory(application: Application): SearchQueryAdapterItemFactory =
        SearchQueryAdapterItemFactoryImpl(application)
}
