package by.ve.omdbapiandroid.network

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY_PARAM_NAME = "apiKey"

class ApiKeyAddingInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request().run {
            val urlWithApiKey = url()
                .newBuilder()
                .addQueryParameter(API_KEY_PARAM_NAME, apiKey)
                .build()

            val requestWithApiKey = newBuilder().url(urlWithApiKey).build()

            chain.proceed(requestWithApiKey)
        }
    }
}
