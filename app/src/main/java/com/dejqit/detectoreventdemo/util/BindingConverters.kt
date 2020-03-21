package com.dejqit.detectoreventdemo.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object BindingConverters {
    //    private val humanFormat: DateFormat = SimpleDateFormat("")
    private val humanFormat: DateFormat =
        DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
    private val networkFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS", Locale.US)

    @JvmStatic
    fun formatDateTime(dateTime: String): String {
        return humanFormat.format(networkFormat.parse(dateTime))
    }
}