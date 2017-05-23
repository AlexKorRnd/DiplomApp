package com.alexkorrnd.diplomapp.data.network

object NetworkConst {
    val BASE_URL: String
        get() = MAPS_API_URL

    private const val MAPS_API_URL = "http://maps.google.com/"

    const val CONNECTION_TIMEOUT: Long = 60
    const val READ_TIMEOUT: Long = 60

    const val LOG_TAG = "Network"

}