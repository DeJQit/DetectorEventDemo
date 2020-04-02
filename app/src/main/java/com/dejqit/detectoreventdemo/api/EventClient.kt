package com.dejqit.detectoreventdemo.api

import android.app.Activity
import android.content.Context
import android.util.Log
import com.dejqit.detectoreventdemo.BuildConfig
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.serializationConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object EventClient {

    @Serializable()
    data class LoginServerItem (
        val name: String,
        val base_url: String,
        val username: String,
        val password: String,
        val editable: Boolean = true
    )

    object ServerStorage {
        private val demoStorage = listOf<LoginServerItem>(
            LoginServerItem(
                name = "demo1",
                base_url = "http://try.axxonsoft.com:8000/asip-api/",
                username = "root",
                password = "root",
                editable = false
            ),
            LoginServerItem(
                name = "demo2",
                base_url = "http://62.251.52.165:8000/",
                username = "root",
                password = "root",
                editable = false
            )
        )
        private var storage = ArrayList<LoginServerItem>()

        fun getStorage(): ArrayList<LoginServerItem> {
            return storage
        }

        fun addServer(server: LoginServerItem) {
            storage.add(server)
        }

        fun replaceServer(id: Int, server: LoginServerItem) {
            storage[id] = server
        }

        fun getServer(id: Int): LoginServerItem {
            return storage[id]
        }

        fun deleteServer(id: Int) {
            storage.removeAt(id)
        }

        fun isEmpty(): Boolean {
            return storage.isEmpty()
        }

        fun getSize(): Int {
            return storage.size
        }

        fun getClient(id: Int): Retrofit {
            return createClient(storage[id])
        }

        fun storageSave(activity: Activity) {
            val sharedPref = activity.getSharedPreferences("viewModel", Context.MODE_PRIVATE) ?: return
            with ( sharedPref.edit() ) {
                val json = JSON(strictMode = false)
                val viewModel = json.stringify(LoginServerItem.serializer().list, storage)
                putString("viewModel", viewModel)
                commit()
            }
        }

        fun storageLoad(activity: Activity) {
            val sharedPref = activity.getSharedPreferences("viewModel", Context.MODE_PRIVATE)
            with (sharedPref!!) {
                val viewModel = getString("viewModel", "[]")
                val json = JSON(strictMode = false)
                storage = json.parse(LoginServerItem.serializer().list, viewModel!!) as ArrayList<LoginServerItem>
            }
        }
    }

    fun createClient(server: LoginServerItem): Retrofit {
        val contentType = MediaType.get("application/json")
//        val urlBuilder = HttpUrl.Builder()
//            .scheme("http")
//            .host(server.base_url)
//            .build()
        return Retrofit.Builder()
            .addConverterFactory(serializationConverterFactory(contentType, JSON(strictMode = false)))
            .baseUrl(server.base_url)
            .client(provideOkHTTPClient(server))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun provideOkHTTPClient(server: LoginServerItem): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.connectTimeout(20, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        client.writeTimeout(30, TimeUnit.SECONDS)

        // Debug HTTP requests with headers
        if (BuildConfig.DEBUG) {
            client.addInterceptor(
                HttpLoggingInterceptor { Log.i("OKHTTP3", it) }.setLevel(
                    HttpLoggingInterceptor.Level.HEADERS
                )
            )
//            client.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
        }
        // Authentication
        client.authenticator { _: Route?, response: Response ->
            val basicCredentials = Credentials.basic(server.username, server.password)
            return@authenticator response.request().newBuilder().header("Authorization", basicCredentials).build()
        }

        return client.build()
    }

}


