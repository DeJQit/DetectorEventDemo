package com.dejqit.detectoreventdemo.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dejqit.detectoreventdemo.BR
import com.dejqit.detectoreventdemo.model.EventContent
import com.dejqit.detectoreventdemo.ui.event.EventViewModel

class EventListHolder constructor(private val dataBinding: ViewDataBinding, private val eventViewModel: EventViewModel)
    : RecyclerView.ViewHolder(dataBinding.root) {

    fun bind(event: EventContent.Event) {
        dataBinding.setVariable( BR.eventData, event)
        dataBinding.executePendingBindings()
        //itemView.onClick

    }
}
