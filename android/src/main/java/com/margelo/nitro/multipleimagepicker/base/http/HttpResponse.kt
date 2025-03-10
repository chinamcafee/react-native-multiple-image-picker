package com.margelo.nitro.multipleimagepicker.base.http

open class HttpResponse @JvmOverloads constructor(
    var errorCode: String = "",
    var errorMsg: String = ""
) {
    var time = 0L

    init {
        time = System.currentTimeMillis()
    }

    fun setRequestTime(reqTime: Long) {
        time -= reqTime
    }
}