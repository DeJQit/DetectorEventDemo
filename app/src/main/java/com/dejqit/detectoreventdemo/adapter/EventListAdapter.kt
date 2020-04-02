package com.dejqit.detectoreventdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dejqit.detectoreventdemo.databinding.FragmentEventListBinding
import com.dejqit.detectoreventdemo.model.EventContent
import com.dejqit.detectoreventdemo.ui.event.EventViewModel

class EventListAdapter(
    private val eventViewModel: EventViewModel,
    private val server: Int
) : RecyclerView.Adapter<EventListHolder>() {
    var eventList: List<EventContent.Event> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = FragmentEventListBinding.inflate(inflater, parent, false)
        return EventListHolder(dataBinding, eventViewModel)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: EventListHolder, position: Int) {
        holder.bind(server, eventList[position])
    }

    fun updateEventList(eventList: List<EventContent.Event>) {
        this.eventList = eventList
        notifyDataSetChanged()
    }
}