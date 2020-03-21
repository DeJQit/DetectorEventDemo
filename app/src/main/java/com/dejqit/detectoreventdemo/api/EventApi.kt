package com.dejqit.detectoreventdemo.api

import com.dejqit.detectoreventdemo.model.EventContent
import io.reactivex.Observable
import retrofit2.http.GET

interface EventApi {

    @GET("archive/events/detectors/past/future")
    fun getLastEvents(): Observable<EventContent.EventList>

}