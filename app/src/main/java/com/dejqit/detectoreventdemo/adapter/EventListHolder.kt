package com.dejqit.detectoreventdemo.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dejqit.detectoreventdemo.BR
import com.dejqit.detectoreventdemo.R
import com.dejqit.detectoreventdemo.api.EventClient
import com.dejqit.detectoreventdemo.model.CameraContent
import com.dejqit.detectoreventdemo.model.EventContent
import com.dejqit.detectoreventdemo.ui.event.EventViewModel
import kotlinx.android.synthetic.main.fragment_event_list.view.*

class EventListHolder constructor(private val dataBinding: ViewDataBinding, private val eventViewModel: EventViewModel)
    : RecyclerView.ViewHolder(dataBinding.root) {

    fun bind(serverId: Int, event: EventContent.Event) {
        dataBinding.setVariable(BR.eventData, event)
        dataBinding.executePendingBindings()

        try {
            val client = EventClient.ServerStorage.getClient(serverId)

            // Camera Name
            CameraContent.getCameraForEvent(client, event) {
                    isSuccess, camera ->
                if(isSuccess) if (camera != null) {
                    itemView.cameraName.text = String.format("%s %s %s %s", camera.vendor, camera.model, camera.comment, camera.displayName)
                }
            }

            // Download snapshot
            itemView.snapshotImage.setImageResource(R.drawable.ic_film_strip)
            itemView.snapshotImage.clipToOutline = true

            CameraContent.getCameraSnapshotForEvent(client, event, 1024)
            { isSuccess, snapshot ->
                if (isSuccess) itemView.snapshotImage.setImageBitmap(snapshot)
            }

        } catch (e: Exception) {

        }



    }
}