package by.ve.omdbapiandroid.parsing

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ParsingModule {

    @Provides
    @Singleton
    fun provideMoshi() = Moshi.Builder().build()
}