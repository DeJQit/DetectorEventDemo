package com.dejqit.detectoreventdemo.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dejqit.detectoreventdemo.model.EventContent

class EventViewModel : ViewModel() {

    // Background text
    val text: LiveData<String>
        get() = _text

    // Event list
    val listEvent: LiveData<List<EventContent.Event>>
        get() = _listEvent

    // Flag when list is empty
    val isListEmpty: LiveData<Boolean>
        get() = _isListEmpty

    // Flag when list is loading
    val isListLoading: LiveData<Boolean>
        get() = _isListLoading

    private val _text = MutableLiveData<String>().apply {
        value = "Event list empty."
    }

    private val _listEvent = MutableLiveData<List<EventContent.Event>>()

    private val _isListEmpty = MutableLiveData<Boolean>().apply {
        value = true
    }

    private val _isListLoading = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun fetchEventList () {
        _isListLoading.value = true
        EventContent.getEventList { isSuccess, eventList ->
            _isListLoading.value = false
            if (isSuccess) {
                _listEvent.value = eventList.events
                _isListEmpty.value = eventList.events.isEmpty()
            } else {
                _listEvent.value = emptyList()
                _isListEmpty.value = true
            }
        }
    }


}