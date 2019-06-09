package by.ve.omdbapiandroid.network.dagger

import by.ve.omdbapiandroid.BuildConfig
import by.ve.omdbapiandroid.network.ApiKeyAddingInterceptor
import by.ve.omdbapiandroid.network.SearchService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val OMDBAPI_BASE_URL = "https://www.omdbapi.com/"

@Module
class NetworkModule {

    @Provides
    fun provideConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    fun provideApiKeyAddingInterceptor() =
        ApiKeyAddingInterceptor(BuildConfig.OMDBAPI_API_KEY)

    @Provides
    fun provideOkHttpClient(apiKeyAddingInterceptor: ApiKeyAddingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(apiKeyAddingInterceptor).build()

    @Provides
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory =
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Provides
    @Singleton
    fun provideRetrofit(
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        moshiConverterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(OMDBAPI_BASE_URL)
        .addCallAdapterFactory(rxJava2CallAdapterFactory)
        .addConverterFactory(moshiConverterFactory)
        .client(okHttpClient)
        .build();

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): SearchService = retrofit.create(SearchService::class.java)
}
