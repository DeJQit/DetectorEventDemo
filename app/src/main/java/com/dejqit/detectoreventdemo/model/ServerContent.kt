package com.dejqit.detectoreventdemo.model

import com.dejqit.detectoreventdemo.api.EventClient
import com.dejqit.detectoreventdemo.api.ServerApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.Serializable

object ServerContent {

    private var disposable = Disposables.disposed()

    fun getServerVersion(onResult: (isSuccess: Boolean, version: ServerVersion, error: String) -> Unit) {

        val createClient = EventClient.createClient()
        val create = createClient.create(ServerApi::class.java)
        disposable = create.getServerVersion()
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

    @Serializable()
    data class LoginServerItem (
        val name: String,
        val base_url: String,
        val username: String,
        val password: String,
        var description: String
    )
}