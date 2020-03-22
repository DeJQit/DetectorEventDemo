package com.dejqit.detectoreventdemo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dejqit.detectoreventdemo.model.ServerContent

class LoginViewModel : ViewModel() {

    // List of all known servers
    val serverList : LiveData<ArrayList<ServerContent.LoginServerItem>>
        get() = _servers

    val isListEmpty = MutableLiveData<Boolean>()

    val errorMessage = MutableLiveData<String>()

    private val _servers = MutableLiveData<ArrayList<ServerContent.LoginServerItem>>()


    fun getServers(): ArrayList<ServerContent.LoginServerItem>? {
        return _servers.value
    }

    fun addServer(server: ServerContent.LoginServerItem) {
        _servers.value?.add(server)
    }

    fun fetchOneItem() {
        val item = ServerContent.LoginServerItem(name = "serverName",
                base_url = "url",
                username = "username",
                password = "password",
                description = ""
        )
        addServer(item)
    }

}
