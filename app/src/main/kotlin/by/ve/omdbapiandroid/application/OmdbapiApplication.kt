package by.ve.omdbapiandroid.application

import by.ve.omdbapiandroid.application.dagger.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class OmdbapiApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.factory().create(this)
}