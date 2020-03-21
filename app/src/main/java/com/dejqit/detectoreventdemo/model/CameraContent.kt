package com.dejqit.detectoreventdemo.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.dejqit.detectoreventdemo.api.CameraApi
import com.dejqit.detectoreventdemo.api.EventClient
import com.dejqit.detectoreventdemo.model.EventContent.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.Serializable
import java.util.function.Consumer

object CameraContent {

    private var cameraListDisposables = Disposables.disposed()
    private var cameraSnapshotDisposables = Disposables.disposed()

    fun getCameraList(onResult: (isSuccess: Boolean, cameraList: CameraList, error: String) -> Unit) {
        val createClient = EventClient.createClient()
        val create = createClient.create(CameraApi::class.java)
        cameraListDisposables = create.getCameraList()
            .cache()
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
        event: Event,
        onResult: (isSuccess: Boolean, camera: Camera?) -> Unit)
    {
        getCameraList { isSuccess, cameraList, _ ->
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
        event: Event,
        width: Int,
        onResult: (isSuccess: Boolean, snapshot: Bitmap?) -> Unit
    ) {
        cameraSnapshotDisposables = EventClient.createClient().create(CameraApi::class.java)
            .getCameraSnapshot(event.source.removePrefix("hosts/"), event.timestamp, width, 0)
            .retry(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    onResult(true, BitmapFactory.decodeStream(it.byteStream()))
                },
                {
                    onResult(false, null)
                    Log.d("CAM", it.toString())
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

