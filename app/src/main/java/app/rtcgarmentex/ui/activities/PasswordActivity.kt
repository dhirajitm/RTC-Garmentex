package app.rtcgarmentex.ui.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import app.rtcgarmentex.R
import app.rtcgarmentex.data.request.PasswordRequest
import app.rtcgarmentex.data.response.BaseResponseModel
import app.rtcgarmentex.databinding.ActivityPasswordBinding
import app.rtcgarmentex.network.ApiClient
import app.rtcgarmentex.network.ApiService
import app.rtcgarmentex.utils.ConstantsHelper
import app.rtcgarmentex.utils.ConstantsHelper.MIN_PASSWORD_LENGTH
import app.rtcgarmentex.utils.SharedPrefHelper
import app.rtcgarmentex.utils.ToastHelper
import app.rtcgarmentex.utils.Utils
import app.rtcgarmentex.utils.Utils.Companion.hideKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PasswordActivity : BaseActivity() {
    lateinit var mBinding: ActivityPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.submitBtn.setOnClickListener {
            hideKeyboard(this)
            if (validated()) {
                changePassword()
            }
        }

        mBinding.backIv.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validated(): Boolean {
        if (mBinding.oldEt.text.toString().trim().isEmpty()) {
            mBinding.oldEt.setText("")
            mBinding.oldEt.requestFocus()
            ToastHelper.showSnackBar(mBinding.root, "Old password required")
            return false
        }

        if (mBinding.oldEt.text.toString() != SharedPrefHelper.getPassword(this)) {
            mBinding.oldEt.requestFocus()
            ToastHelper.showSnackBar(mBinding.root, "Old password not matching")
            return false
        }

        if (mBinding.newEt.text.toString().trim().isEmpty()) {
            mBinding.newEt.setText("")
            mBinding.newEt.requestFocus()
            ToastHelper.showSnackBar(mBinding.root, "New password required")
            return false
        }

        if (mBinding.newEt.text.toString().length < MIN_PASSWORD_LENGTH) {
            ToastHelper.showSnackBar(mBinding.root, "Password must be $MIN_PASSWORD_LENGTH charactor")
            return false
        }

        if (mBinding.newEt.text.toString().contains(" ")) {
            mBinding.newEt.requestFocus()
            ToastHelper.showSnackBar(mBinding.root, "No space allowed in password")
            return false
        }

        if (mBinding.confirmEt.text.toString().trim().isEmpty()) {
            mBinding.confirmEt.setText("")
            mBinding.confirmEt.requestFocus()
            ToastHelper.showSnackBar(mBinding.root, "Confirm password required")
            return false
        }

        if (mBinding.newEt.text.toString() != mBinding.confirmEt.text.toString()) {
            ToastHelper.showSnackBar(mBinding.root, "New password and Confirm password are not same")
            return false
        }

        if (mBinding.oldEt.text.toString() == mBinding.newEt.text.toString()) {
            ToastHelper.showSnackBar(mBinding.root, "Old password and New password are same")
            return false
        }

        return true
    }

    private fun changePassword() {
        val passwordRequest = PasswordRequest(
            SharedPrefHelper.getUserId(this),
            mBinding.oldEt.text.toString().trim(),
            mBinding.newEt.text.toString().trim(),
            mBinding.confirmEt.text.toString().trim()
        )
        mBinding.progressbar.visibility = View.VISIBLE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.updatePassword(getHeaderMap(), passwordRequest).enqueue(object : Callback<BaseResponseModel> {
            override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                mBinding.progressbar.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.status) {
                        ToastHelper.showSnackBar(mBinding.root, response.body()!!.message)
                        SharedPrefHelper.setPassword(this@PasswordActivity, mBinding.newEt.text.toString())
//                        showConfirmation()
                        mBinding.oldEt.setText("")
                        mBinding.newEt.setText("")
                        mBinding.confirmEt.setText("")
                    }
                } else {
                    Utils.errorMessage(response, this@PasswordActivity)
                }
            }

            override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                mBinding.progressbar.visibility = View.GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }
        })
    }

    private fun showConfirmation() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        // set title
        builder.setTitle("Password Changed")
        builder.setMessage("Your passworde")
        // set dialog non cancelable
        builder.setCancelable(true)
        builder.setPositiveButton("OK", { dialogInterface, i ->
/*
            SharedPrefHelper.setPassword(this, "")
            SharedPrefHelper.setUserId(this, -1)
            SharedPrefHelper.setUserName(this, "")
            SharedPrefHelper.setRememberMe(this, false)
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
*/
            finish()
        })
        // show dialog
        builder.show()
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["userId"] = SharedPrefHelper.getUserId(this).toString()
        headerMap["branchId"] = SharedPrefHelper.getBranchId(this).toString()
        headerMap["token"] = SharedPrefHelper.getUserToken(this)
        return headerMap
    }

}