package com.dejqit.detectoreventdemo.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.dejqit.detectoreventdemo.api.CameraApi
import com.dejqit.detectoreventdemo.model.EventContent.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import java.util.function.Consumer

object CameraContent {

    fun getCameraList(
        client: Retrofit,
        onResult: (isSuccess: Boolean, cameraList: CameraList, error: String) -> Unit
    ): Disposable? {
        val create = client.create(CameraApi::class.java)
        return create.getCameraList()
            .retry(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // On Request Successful
                onResult(true, it, "OK")
            }, {
                // On Error occurred
                onResult(false, CameraList(cameras = emptyList()), it.toString())
            })
    }

    fun getCameraForEvent (
        client: Retrofit,
        event: Event,
        onResult: (isSuccess: Boolean, camera: Camera?) -> Unit)
    {
        getCameraList(client) { isSuccess, cameraList, _ ->
            if(isSuccess) {
                cameraList.cameras.forEach(Consumer { cam ->
                    cam.videoStreams.forEach(Consumer {
                        if(it.accessPoint == event.source) {
                            onResult(true, cam)
                        }
                    })
                })
            }
            onResult(false, null)
        }
    }

    fun getCameraSnapshotForEvent(
        client: Retrofit,
        event: Event,
        width: Int,
        onResult: (isSuccess: Boolean, snapshot: Bitmap?) -> Unit
    ): Disposable? {
        val create = client.create(CameraApi::class.java)
            return create.getCameraSnapshot(event.source.removePrefix("hosts/"), event.timestamp, width, 0)
            .retry(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    onResult(true, BitmapFactory.decodeStream(it.byteStream()))
                },
                {
                    onResult(false, null)
                }
            )
    }

    @Serializable()
    data class CameraList(
        val cameras: List<Camera>
    )

    @Serializable()
    data class Camera(
        // val archives: []
        // val audioStream: []
        val comment: String,
        // val detectors: []
        val displayId: String,
        val displayName: String,
        val groups: List<String>,
        val ipAddress: String,
        val isActivated: Boolean,
        val latitude: String,
        val longitude: String,
        val model: String,
        //val offlineDetectors: []
        //val ptzs: []
        //val textSources: []
        val vendor: String,
        val videoStreams: List<CameraVideoStream>
    )

    @Serializable()
    data class CameraVideoStream(
        val accessPoint: String
    )
}

