package com.dejqit.detectoreventdemo.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dejqit.detectoreventdemo.api.EventClient
import com.dejqit.detectoreventdemo.model.EventContent

class EventViewModel : ViewModel() {

    var serverId: Int = 0

    // Background text
    val text: LiveData<String>
        get() = _text

    // Event list
    val listEvent: LiveData<List<EventContent.Event>>
        get() = _listEvent

    // Event list state
    enum class EventListState {
        LOADING, // When list loading, show loading indicator
        EMPTY,  // When list is empty, display label "Empty"
        ERROR,  // When error occur, display label with error and show button retry
        DONE    // All done
    }

    val state: LiveData<EventListState>
        get() = _listState

    private val _text  = MutableLiveData<String>()

    private val _listEvent = MutableLiveData<List<EventContent.Event>>()

    private val _listState = MutableLiveData<EventListState>()


    fun fetchEventList() {
        _listState.value = EventListState.LOADING
        try {
            val client = EventClient.ServerStorage.getClient(serverId)
            EventContent.getEventList(client) { isSuccess, eventList, error ->
                if (isSuccess) {
                    _listEvent.value = eventList.events
                    if (eventList.events.isEmpty()) {
                        _listState.value = EventListState.EMPTY
                        _text.value = "Event list empty."
                    } else {
                        _listState.value = EventListState.DONE
                    }
                } else {
                    _listEvent.value = emptyList()
                    _text.value = "Error: $error"
                    _listState.value = EventListState.ERROR
                }
            }
        } catch (e: Exception) {
            _listEvent.value = emptyList()
            _text.value = "Error: $e"
            _listState.value = EventListState.ERROR
        }

    }


}