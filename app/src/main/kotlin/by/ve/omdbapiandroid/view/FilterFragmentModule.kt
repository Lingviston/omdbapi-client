package by.ve.omdbapiandroid.view

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FilterFragmentModule {

    @ContributesAndroidInjector
    fun contributeFilterDialogFragment(): FilterDialogFragment
}