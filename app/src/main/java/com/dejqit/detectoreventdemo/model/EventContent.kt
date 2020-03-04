package com.dejqit.detectoreventdemo.model

import com.dejqit.detectoreventdemo.api.EventApi
import com.dejqit.detectoreventdemo.api.EventClient
import kotlinx.serialization.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

        lastEvents.enqueue(object : Callback<EventList> {
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
        @Serializable(with = DateSerializer::class)
        val timestamp: Date
        //val extra
        //val rectangles
    )

    @Serializer(forClass = Date::class)
    object DateSerializer : KSerializer<Date> {
        private val df: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS", Locale.US)

        override fun serialize(output: Encoder, obj: Date) {
            output.encodeString(df.format(obj))
        }

        override fun deserialize(input: Decoder): Date {
            return df.parse(input.decodeString())!!
        }
    }

}

