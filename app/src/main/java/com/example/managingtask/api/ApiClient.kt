package com.example.managingtask.api

import com.example.managingtask.BuildConfig
import com.example.managingtask.data.IApiClient
import com.example.managingtask.data.ITokenManager
import com.example.managingtask.util.WrapperConverterFactory
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient(
    private val tokenManager: ITokenManager
): IApiClient {

    override val service = createService()

    private fun createService(): ApiService{
        val httpClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }

        httpClientBuilder.addInterceptor(logging)
        httpClientBuilder.addInterceptor {
            val requestBuilder = it.request()
                .newBuilder()
                .header("Authorization", "Bearer " + tokenManager.token!!)
                .build()
            return@addInterceptor it.proceed(requestBuilder)
        }

        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .addConverterFactory(WrapperConverterFactory(GsonConverterFactory.create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))

        builder.client(httpClientBuilder.build())
        return builder.build().create(ApiService::class.java)
    }
}