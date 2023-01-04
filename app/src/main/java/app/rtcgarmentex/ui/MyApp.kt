package app.rtcgarmentex.ui

import android.app.Application
import android.content.Context

class MyApp : Application() {
    companion object {
        private var instance: MyApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}