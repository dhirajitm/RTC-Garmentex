package app.rtcgarmentex.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import app.rtcgarmentex.databinding.ActivitySplashBinding
import app.rtcgarmentex.utils.SharedPrefHelper

class SplashActivity : BaseActivity() {
    lateinit var mBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        loadLogin()

/*
        if (SharedPrefHelper.getRememberMe(this)) {
            loadHome()
        } else {
            loadLogin()
        }
*/
    }

    private fun loadHome() {
        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 2000)
    }

    private fun loadLogin() {
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }
}