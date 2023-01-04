package app.rtcgarmentex.utils

import android.content.Context
import android.net.ConnectivityManager
import app.rtcgarmentex.ui.MyApp

class NetworkHelperUtil {
    companion object {
        fun isOnline(): Boolean {
            val connectivityManager = MyApp.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return (networkInfo != null && networkInfo.isConnected)
        }
    }
}