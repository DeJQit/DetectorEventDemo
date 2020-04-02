package com.dejqit.detectoreventdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dejqit.detectoreventdemo.api.EventClient
import com.dejqit.detectoreventdemo.databinding.LoginFragmentListBinding

class LoginListAdapter() : RecyclerView.Adapter<LoginListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = LoginFragmentListBinding.inflate(inflater, parent, false)
        return LoginListHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return EventClient.ServerStorage.getSize()
    }

    override fun onBindViewHolder(holder: LoginListHolder, position: Int) {
        holder.bind(position)
    }

    fun updateServerList() {
        notifyDataSetChanged()
    }

}