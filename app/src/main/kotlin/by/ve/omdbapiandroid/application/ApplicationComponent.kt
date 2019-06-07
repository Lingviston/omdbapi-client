package by.ve.omdbapiandroid.application

import android.app.Application
import by.ve.omdbapiandroid.domain.DomainModule
import by.ve.omdbapiandroid.network.NetworkModule
import by.ve.omdbapiandroid.parsing.ParsingModule
import by.ve.omdbapiandroid.repositories.RepositoriesModule
import by.ve.omdbapiandroid.view.MainActivityModule
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
        DomainModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<OmdbapiApplication> {

    override fun inject(instance: OmdbapiApplication?)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}