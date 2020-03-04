package com.dejqit.detectoreventdemo.api

import com.dejqit.detectoreventdemo.model.EventContent
import retrofit2.Call
import retrofit2.http.GET

interface EventApi {

    @GET("/asip-api/archive/events/detectors/past/future")
    fun getLastEvents(): Call<EventContent.EventList>
}