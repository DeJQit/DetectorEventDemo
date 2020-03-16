package com.dejqit.detectoreventdemo.api

import android.annotation.SuppressLint
import com.dejqit.detectoreventdemo.BuildConfig
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.serializationConverterFactory
import kotlinx.serialization.json.JSON
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object EventClient {

    private const val BASE_URL = "http://try.axxonsoft.com:8000/asip-api/"

    fun createClient(): Retrofit {
        val contentType = MediaType.get("application/json")
        return Retrofit.Builder()
            .addConverterFactory(serializationConverterFactory(contentType, JSON(strictMode = false)))
            .baseUrl(BASE_URL)
            .client(provideOkHTTPClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun provideOkHTTPClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.connectTimeout(20, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        client.writeTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        // Authentication
        client.authenticator { _: Route?, response: Response ->
            val basicCredentials = Credentials.basic("root", "root");
            return@authenticator response.request().newBuilder().header("Authorization", basicCredentials).build()
        }

        return client.build()
    }
}


