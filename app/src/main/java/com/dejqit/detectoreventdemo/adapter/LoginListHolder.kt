package com.dejqit.detectoreventdemo.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dejqit.detectoreventdemo.BR
import com.dejqit.detectoreventdemo.model.ServerContent

class LoginListHolder constructor(private val dataBinding: ViewDataBinding)
    : RecyclerView.ViewHolder(dataBinding.root){

    fun bind(serverItem: ServerContent.LoginServerItem) {
        dataBinding.setVariable(BR.server, serverItem)
        dataBinding.executePendingBindings()
    }

}