package com.dejqit.detectoreventdemo.api

import com.dejqit.detectoreventdemo.model.ServerContent
import io.reactivex.Observable
import retrofit2.http.GET

interface ServerApi {
    @GET("product/version")
    fun getServerVersion(): Observable<ServerContent.ServerVersion>
}