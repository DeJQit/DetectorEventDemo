package com.dejqit.detectoreventdemo.adapter

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.dejqit.detectoreventdemo.BR
import com.dejqit.detectoreventdemo.R
import com.dejqit.detectoreventdemo.api.EventClient
import com.dejqit.detectoreventdemo.databinding.LoginFragmentListBinding
import com.dejqit.detectoreventdemo.model.ServerContent

class LoginListHolder constructor(private val dataBinding: LoginFragmentListBinding)
    : RecyclerView.ViewHolder(dataBinding.root){

    fun bind(position: Int) {
        val serverItem = EventClient.ServerStorage.getServer(position)

        dataBinding.setVariable(BR.server, serverItem)
        dataBinding.executePendingBindings()


        dataBinding.loginListItem.setOnLongClickListener {
            val listener = Navigation.createNavigateOnClickListener(
                R.id.action_loginFragment_to_loginAddFragment,
                bundleOf("serverListModelIndex" to position)
            )
            listener.onClick(it)
            true
        }

        try {
            val client = EventClient.ServerStorage.getClient(position)
            ServerContent.getServerVersion(client) { isSuccess, version, error ->
                if (isSuccess) {
                    dataBinding.description.text = version.version

                    // Handler to event list navigation
                    dataBinding.loginListItem.setOnClickListener(
                        Navigation.createNavigateOnClickListener(
                            R.id.action_loginFragment_to_eventFragment,
                            bundleOf(Pair("selectedServer", position))
                        )
                    )

                } else {
                    dataBinding.description.text = "Error: $error"
                }
            }
        } catch (error: Exception) {
            dataBinding.description.text = "Error: $error"
        }
    }

}