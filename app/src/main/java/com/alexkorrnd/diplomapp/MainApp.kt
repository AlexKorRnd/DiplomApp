package com.alexkorrnd.diplomapp


import android.app.Application
import com.alexkorrnd.diplomapp.data.network.RestApi

class MainApp : Application() {

    companion object {
        lateinit var instance: MainApp
            private set
    }


    override fun onCreate() {
        super.onCreate()

        instance = this

        RestApi.init(this)
    }
}
