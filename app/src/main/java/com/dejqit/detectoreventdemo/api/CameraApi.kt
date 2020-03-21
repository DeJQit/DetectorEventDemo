package com.dejqit.detectoreventdemo.api

import com.dejqit.detectoreventdemo.model.CameraContent
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CameraApi {

    @GET("camera/list")
    fun getCameraList(): Observable<CameraContent.CameraList>

    @GET("archive/media/{cameraId}/{timestampFormatted}")
    fun getCameraSnapshot(
        @Path(
            "cameraId",
            encoded = true
        ) cameraId: String, // Ex.: OWNEROR-2LRQDEG/DeviceIpint.3/SourceEndpoint.video:0:0
        @Path("timestampFormatted") timestampFormatted: String, // Ex.: 20190320T121212.000000
        @Query("w") width: Int, // Ex.: 0
        @Query("h") height: Int // Ex.: 200
    ): Observable<ResponseBody>

}