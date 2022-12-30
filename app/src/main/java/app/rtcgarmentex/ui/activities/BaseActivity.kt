package app.rtcgarmentex.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.rtcgarmentex.utils.Utils

open class BaseActivity : AppCompatActivity() {
    var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Utils.hideKeyboard(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (this is AddOrderActivity) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
