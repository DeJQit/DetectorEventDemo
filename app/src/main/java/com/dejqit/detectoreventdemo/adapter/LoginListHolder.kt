package com.dejqit.detectoreventdemo.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dejqit.detectoreventdemo.BR
import com.dejqit.detectoreventdemo.model.ServerContent
import com.dejqit.detectoreventdemo.ui.login.LoginViewModel

class LoginListHolder constructor(private val dataBinding: ViewDataBinding, private val loginViewModel: LoginViewModel)
    : RecyclerView.ViewHolder(dataBinding.root){

    fun bind(serverItem: ServerContent.LoginServerItem) {
        dataBinding.setVariable(BR.server, serverItem)
        dataBinding.executePendingBindings()
    }
}