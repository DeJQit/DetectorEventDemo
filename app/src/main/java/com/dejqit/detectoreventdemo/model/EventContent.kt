package com.dejqit.detectoreventdemo.model

import com.dejqit.detectoreventdemo.api.EventApi
import com.dejqit.detectoreventdemo.api.EventClient
import kotlinx.serialization.*
import kotlinx.serialization.Optional
import kotlinx.serialization.internal.ArrayListClassDesc
import kotlinx.serialization.internal.StringDescriptor
import retrofit2.Call
import java.util.*
import retrofit2.Callback
import retrofit2.Response

object EventContent {

    fun getEventList(onResult: (isSuccess: Boolean, eventList: EventList) -> Unit) {
//        return listOf(
//            Event("b171", "", "source", "type", "alertState", "2345"),
//            Event("id2", "origin", "source", "type", "alertState", "2345"),
//            Event("id3", "origin", "source", "type", "alertState", "2345"),
//            Event("id4", "origin", "source", "type", "alertState", "2345")
//        )
        val createClient = EventClient.createClient()
        val create = createClient.create(EventApi::class.java)
        val lastEvents = create.getLastEvents()

        lastEvents.enqueue(object : Callback<EventContent.EventList> {
            override fun onFailure(call: Call<EventList>, t: Throwable) {
                onResult(false, EventList(events = emptyList(), more = false))
            }

            override fun onResponse(call: Call<EventList>, response: Response<EventList>) {
                response.body()?.let {
                    onResult(true, it)
                    return
                }
                onResult(false, EventList(events = emptyList(), more = false))
            }

        })
    }

    @Serializable()
    data class EventList (
        val events: List<Event>,
        val more:   Boolean
    )


    @Serializable()
    data class Event (
        val id: String,
        val origin: String,
        val source: String,
        val type: String,
        val alertState: String,
        val timestamp: String
        //val extra
        //val rectangles
    )

//    object EventListSerializer : KSerializer<List<Event>> {
//        override val descriptor: SerialDescriptor = StringDescriptor.withName("FieldList")
//
//        override fun deserialize(input: Decoder): List<Event> {
//
//        }
//
//        override fun serialize(output: Encoder, obj: List<Event>) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//    }


}