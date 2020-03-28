package com.dejqit.detectoreventdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.dejqit.detectoreventdemo.R
import com.dejqit.detectoreventdemo.databinding.LoginFragmentListBinding
import com.dejqit.detectoreventdemo.model.ServerContent

class LoginListAdapter(
    var serverList: ArrayList<ServerContent.LoginServerItem>
) : RecyclerView.Adapter<LoginListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = LoginFragmentListBinding.inflate(inflater, parent, false)
        // Handler to event list navigation
        dataBinding.loginListItem.setOnClickListener(Navigation.createNavigateOnClickListener( R.id.action_loginFragment_to_eventFragment))
        return LoginListHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return serverList.size
    }

    override fun onBindViewHolder(holder: LoginListHolder, position: Int) {
        holder.bind(serverList[position])
    }

    fun updateServerList() {
        notifyDataSetChanged()
    }

}