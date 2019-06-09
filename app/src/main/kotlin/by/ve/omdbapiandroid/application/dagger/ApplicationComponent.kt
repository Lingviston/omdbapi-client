package by.ve.omdbapiandroid.application.dagger

import android.app.Application
import by.ve.omdbapiandroid.application.OmdbapiApplication
import by.ve.omdbapiandroid.db.dagger.DatabaseModule
import by.ve.omdbapiandroid.domain.dagger.DomainModule
import by.ve.omdbapiandroid.network.dagger.NetworkModule
import by.ve.omdbapiandroid.parsing.dagger.ParsingModule
import by.ve.omdbapiandroid.repositories.dagger.RepositoriesModule
import by.ve.omdbapiandroid.view.dagger.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ParsingModule::class,
        NetworkModule::class,
        MainActivityModule::class,
        RepositoriesModule::class,
        DomainModule::class,
        DatabaseModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<OmdbapiApplication> {

    override fun inject(instance: OmdbapiApplication?)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}