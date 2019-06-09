package by.ve.omdbapiandroid.view.dagger

import by.ve.omdbapiandroid.view.FilterDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FilterFragmentModule {

    @ContributesAndroidInjector
    fun contributeFilterDialogFragment(): FilterDialogFragment
}
