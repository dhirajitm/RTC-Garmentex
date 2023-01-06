package app.rtcgarmentex.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.rtcgarmentex.utils.Utils
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private val TAG: String = BaseActivity::class.java.simpleName
    var mToast: Toast? = null
    private var connectivityBroadcastReceiver: BroadcastReceiver? = null
    private var snackbar: Snackbar? = null
    private var fullScreenDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Utils.hideKeyboard(this)
        initConnectivityBroadcastReceiver()
        registerInternetConnectivityBroadcastReceiver()
    }

    private fun registerInternetConnectivityBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.wifi.STATE_CHANGE")
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivityBroadcastReceiver, intentFilter)
    }

    private fun initConnectivityBroadcastReceiver() {
        connectivityBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
/*
                if (NetworkHelperUtil.isOnline()) {
                    Log.d(TAG, "onReceive: Connected")
                    dismissSnackbar()
                    hideInternetDialog()
                } else {
                    Log.d(TAG, "onReceive: Not Connected")
                    showNoInternetConnectionSnackbar()
                    showNoInternetDialog()
                }
*/
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (this is AddOrderActivity) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityBroadcastReceiver)
    }

    private fun dismissSnackbar() {
        if (snackbar != null && snackbar!!.isShown) {
            snackbar!!.dismiss()
        }
    }

    private fun showNoInternetConnectionSnackbar() {
        if (snackbar != null && snackbar!!.isShown) {
            return
        }
        snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet", Snackbar.LENGTH_INDEFINITE)
        snackbar!!.setActionTextColor(Color.WHITE)
        snackbar!!.show()
    }

    private fun hideInternetDialog() {
        Log.d(TAG, "onReceive: Hide Started")
        if (fullScreenDialog != null && fullScreenDialog!!.isShowing) {
            fullScreenDialog!!.hide()
            fullScreenDialog!!.dismiss()
            Log.d(TAG, "onReceive: Hide End")
        }
    }

    private fun showNoInternetDialog() {
        val mBuilder = AlertDialog.Builder(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
            .setTitle("TITLE")
            .setMessage("MESSAGE")
            .setPositiveButton("Positive", null)
            .setNegativeButton("Negative", null)

        // Create the AlertDialog
        fullScreenDialog = mBuilder.create()

        // Show the AlertDialog
        fullScreenDialog!!.show()
    }
}
