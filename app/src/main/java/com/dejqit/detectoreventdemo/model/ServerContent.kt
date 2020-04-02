package com.dejqit.detectoreventdemo.model

import com.dejqit.detectoreventdemo.api.ServerApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.Serializable
import retrofit2.Retrofit

object ServerContent {

    fun getServerVersion(
        client: Retrofit,
        onResult: (isSuccess: Boolean, version: ServerVersion, error: String) -> Unit
    ): Disposable? {

        val create = client.create(ServerApi::class.java)
        return create.getServerVersion()
            .retry(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // On Request Successful
                onResult(true, it, "OK")
            }, {
                // On Error occurred
                onResult(false, ServerVersion("unknown version"), it.toString())
            })
    }


    @Serializable()
    data class ServerVersion (
        val version: String
    )

}