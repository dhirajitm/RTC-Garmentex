package app.rtcgarmentex.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import app.rtcgarmentex.R
import app.rtcgarmentex.data.request.ProfileRequest
import app.rtcgarmentex.data.response.BaseResponseModel
import app.rtcgarmentex.databinding.ActivityProfileBinding
import app.rtcgarmentex.network.ApiClient
import app.rtcgarmentex.network.ApiService
import app.rtcgarmentex.utils.ConstantsHelper.PHONE_NUMBER_LENGTH
import app.rtcgarmentex.utils.SharedPrefHelper
import app.rtcgarmentex.utils.ToastHelper
import app.rtcgarmentex.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : BaseActivity() {
    lateinit var mBinding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()

        mBinding.submitBtn.setOnClickListener {
            if (validated()) {
                updateProfile()
            }
        }

        mBinding.backIv.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        mBinding.nameEt.setText(SharedPrefHelper.getUserName(this))
        mBinding.phoneEt.setText(SharedPrefHelper.getPhone(this))
        mBinding.emailEt.setText(SharedPrefHelper.getEmail(this))
        mBinding.statusEt.setText(SharedPrefHelper.getStatus(this))
    }

    private fun validated(): Boolean {
        if (mBinding.nameEt.text.toString().trim().isEmpty()) {
            mBinding.nameEt.setText("")
            ToastHelper.showSnackBar(mBinding.root, "Name required")
            return false
        }
        if (mBinding.phoneEt.text.toString().trim().isEmpty()) {
            mBinding.phoneEt.setText("")
            ToastHelper.showSnackBar(mBinding.root, "Phone required")
            return false
        }
        if (mBinding.phoneEt.text.toString().length != PHONE_NUMBER_LENGTH) {
            ToastHelper.showSnackBar(mBinding.root, "Phone must be $PHONE_NUMBER_LENGTH digit")
            return false
        }
        return true
    }

    private fun updateProfile() {
        val profileRequest = ProfileRequest(
            SharedPrefHelper.getUserId(this),
            mBinding.nameEt.text.toString().trim(),
            mBinding.phoneEt.text.toString().trim().toLong()
        )
        mBinding.progressbar.visibility = View.VISIBLE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.updateProfile(getHeaderMap(), profileRequest).enqueue(object : Callback<BaseResponseModel> {
            override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                mBinding.progressbar.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.status) {
                        ToastHelper.showSnackBar(mBinding.root, response.body()!!.message)
                        SharedPrefHelper.setUserName(this@ProfileActivity, mBinding.nameEt.text.toString().trim())
                        SharedPrefHelper.setPhone(this@ProfileActivity, mBinding.phoneEt.text.toString().trim())
                        onBackPressed()
                    }
                } else {
                    Utils.errorMessage(response, this@ProfileActivity)
                }
            }

            override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                mBinding.progressbar.visibility = View.GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }
        })
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["userId"] = SharedPrefHelper.getUserId(this).toString()
        headerMap["branchId"] = SharedPrefHelper.getBranchId(this).toString()
        headerMap["token"] = SharedPrefHelper.getUserToken(this)
        return headerMap
    }

}