package com.alexkorrnd.diplomapp.data.network

import android.content.Context
import com.alexkorrnd.diplomapp.BuildConfig
import com.alexkorrnd.diplomapp.data.network.cookies.AddCookiesInterceptor
import com.alexkorrnd.diplomapp.data.network.cookies.ReceivedCookiesInterceptor
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestApi {
    private lateinit var retrofit: Retrofit

    fun init(context: Context): Unit {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(NetworkConst.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NetworkConst.READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(AddCookiesInterceptor(context))
                .addInterceptor(ReceivedCookiesInterceptor(context))


        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(context)
            //add http logging only for debug-builds
            okHttpClient.addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            okHttpClient.addNetworkInterceptor(StethoInterceptor())
        }

        retrofit = Retrofit.Builder()
                .baseUrl(NetworkConst.BASE_URL)
                .client(okHttpClient.build())
                .validateEagerly(BuildConfig.DEBUG)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)

}
