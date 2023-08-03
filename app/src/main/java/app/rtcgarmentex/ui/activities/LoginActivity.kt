package app.rtcgarmentex.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import app.rtcgarmentex.R
import app.rtcgarmentex.data.request.LoginRequest
import app.rtcgarmentex.data.response.LoginResponse
import app.rtcgarmentex.databinding.ActivityLoginBinding
import app.rtcgarmentex.network.ApiClient
import app.rtcgarmentex.network.ApiService
import app.rtcgarmentex.utils.SharedPrefHelper
import app.rtcgarmentex.utils.ToastHelper
import app.rtcgarmentex.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LoginActivity : BaseActivity() {
    lateinit var mBinding: ActivityLoginBinding
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.signInBtn.setOnClickListener {
            if (validated()) {
                Utils.hideKeyboard(this)
                login()
            }
        }

        if (SharedPrefHelper.getRememberMe(this)) {
            mBinding.userEmailEt.setText(SharedPrefHelper.getEmail(this))
            mBinding.userPasswordEt.setText(SharedPrefHelper.getPassword(this))
            mBinding.rememberCb.isChecked = true
        }
    }

    private fun validated(): Boolean {
        mBinding.userEmailError.visibility = GONE
        mBinding.userPasswordError.visibility = GONE
        mBinding.errorTv.visibility = GONE

        if (mBinding.userEmailEt.text.toString().isEmpty()) {
            mBinding.userEmailEt.requestFocus()
            mBinding.userEmailError.visibility = VISIBLE
            return false
        }
/*
        if (!isValidString(mBinding.userEmailEt.text.toString())) {
            mBinding.userEmailEt.requestFocus()
            mBinding.userEmailError.visibility = VISIBLE
            mBinding.userEmailError.text = "Invalid Email Id"
            return false
        }
*/
        if (mBinding.userPasswordEt.text.toString().isEmpty()) {
            mBinding.userPasswordEt.requestFocus()
            mBinding.userPasswordError.visibility = VISIBLE
            return false
        }
        return true
    }

    private fun isValidString(str: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }

    private fun login() {
        val loginRequest = LoginRequest(
            mBinding.userEmailEt.text.toString().trim(),
            mBinding.userPasswordEt.text.toString().trim()
        )
        mBinding.progressbar.visibility = VISIBLE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.requestLogin(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                mBinding.progressbar.visibility = GONE
                if (response.isSuccessful) {
                    if (response.body()!!.status) {
                        SharedPrefHelper.setRememberMe(this@LoginActivity, mBinding.rememberCb.isChecked)
                        SharedPrefHelper.setPassword(this@LoginActivity, mBinding.userPasswordEt.text.toString().trim())
                        val reps = response.body()?.data
                        SharedPrefHelper.setUserId(this@LoginActivity, reps!!.id)
                        SharedPrefHelper.setBranchId(this@LoginActivity, reps.branchId)
                        SharedPrefHelper.setUserToken(this@LoginActivity, reps.token)
                        SharedPrefHelper.setEmail(this@LoginActivity, reps.email)
                        SharedPrefHelper.setUserName(this@LoginActivity, reps.name)
                        SharedPrefHelper.setPhone(this@LoginActivity, reps.phone.toString())
                        SharedPrefHelper.setStatus(this@LoginActivity, reps.status)
//                        ToastHelper.showSnackBar(mBinding.root, reps?.id.toString() + ", " + reps?.name.toString() + ", " + reps?.phone.toString())
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }
                } else {
                    mBinding.errorTv.visibility = VISIBLE
                    Utils.errorMessage(response, this@LoginActivity)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                mBinding.progressbar.visibility = GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }

        })

    }
}