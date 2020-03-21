package com.dejqit.detectoreventdemo.model

import com.dejqit.detectoreventdemo.api.EventApi
import com.dejqit.detectoreventdemo.api.EventClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object EventContent {

    private var disposable = Disposables.disposed()

    fun getEventList(onResult: (isSuccess: Boolean, eventList: EventList, error: String) -> Unit) {

        val createClient = EventClient.createClient()
        val create = createClient.create(EventApi::class.java)
        disposable = create.getLastEvents()
            .retry(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // On Request Successful
                onResult(true, it, "OK")
            }, {
                // On Error occurred
                onResult(false, EventList(events = emptyList(), more = false), it.toString())
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
        //@Serializable(with = DateSerializer::class)
        val timestamp: String
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

