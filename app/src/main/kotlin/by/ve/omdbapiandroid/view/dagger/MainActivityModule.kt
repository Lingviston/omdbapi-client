package by.ve.omdbapiandroid.view.dagger

import by.ve.omdbapiandroid.view.MainActivity
import by.ve.omdbapiandroid.view.recyclerview.adapter.MoviesAdapter
import by.ve.omdbapiandroid.view.recyclerview.adapter.SearchQueriesAdapter
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityModule {

    @ContributesAndroidInjector(modules = [MainActivityProviderModule::class, FilterFragmentModule::class])
    fun contributeMainActivity(): MainActivity
}

@Module
class MainActivityProviderModule {

    @Provides
    fun provideMoviesAdapter() = MoviesAdapter()

    @Provides
    fun provideRecentSearchesAdapter() = SearchQueriesAdapter()
}
