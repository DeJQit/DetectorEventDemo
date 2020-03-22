package com.dejqit.detectoreventdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dejqit.detectoreventdemo.databinding.LoginFragmentListBinding
import com.dejqit.detectoreventdemo.model.ServerContent
import com.dejqit.detectoreventdemo.ui.login.LoginViewModel

class LoginListAdapter(private val loginViewModel: LoginViewModel) : RecyclerView.Adapter<LoginListHolder>() {
    var serverList : ArrayList<ServerContent.LoginServerItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = LoginFragmentListBinding.inflate(inflater, parent, false)
        return LoginListHolder(dataBinding, loginViewModel)
    }

    override fun getItemCount(): Int {
        return serverList.size
    }

    override fun onBindViewHolder(holder: LoginListHolder, position: Int) {
        holder.bind(serverList[position])
    }

    fun updateServerList(serverList1: ArrayList<ServerContent.LoginServerItem>) {
        this.serverList = serverList1
        notifyDataSetChanged()
    }

}