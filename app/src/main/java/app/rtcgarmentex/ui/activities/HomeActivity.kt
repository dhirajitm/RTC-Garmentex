package app.rtcgarmentex.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import app.rtcgarmentex.R
import app.rtcgarmentex.data.response.BaseResponseModel
import app.rtcgarmentex.data.response.OrderListResponse
import app.rtcgarmentex.databinding.ActivityHomeBinding
import app.rtcgarmentex.network.ApiClient
import app.rtcgarmentex.network.ApiService
import app.rtcgarmentex.utils.SharedPrefHelper
import app.rtcgarmentex.utils.ToastHelper
import app.rtcgarmentex.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : BaseActivity() {
    lateinit var mBinding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        popupMenu()
        getOrderList()

        mBinding.orderListBtn.setOnClickListener {
            startActivity(Intent(this, OrderListActivity::class.java))
        }

        mBinding.addOrderBtn.setOnClickListener {
            startActivity(Intent(this, AddOrderActivity::class.java))
            finish()
        }

        mBinding.addReceivingBtn.setOnClickListener {
            startActivity(Intent(this, AddReceivingActivity::class.java))
        }
    }

    private fun popupMenu() {
        // creating a object of Popupmenu
        val popupMenu = PopupMenu(this, mBinding.userIv)

        // we need to inflate the object
        // with popup_menu.xml file
        popupMenu.inflate(R.menu.user_popup_menu)
        popupMenu.setForceShowIcon(true)

        // adding click listener to image
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_profile -> {
                    editProfile()
                    true
                }
                R.id.change_password -> {
                    changePassword()
                    true
                }
                R.id.sign_out -> {
                    signOut()
                    true
                }
                else -> {
                    true
                }
            }

        }

        // event on long press on image
        mBinding.userIv.setOnClickListener {
            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenu)
                menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)
            } catch (e: Exception) {
                Log.d("", e.toString())
            } finally {
                popupMenu.show()
            }
        }
    }

    private fun editProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    private fun changePassword() {
        startActivity(Intent(this, PasswordActivity::class.java))
    }

    private fun signOut() {
/*
        SharedPrefHelper.setPassword(this, "")
        SharedPrefHelper.setUserId(this, -1)
        SharedPrefHelper.setUserName(this, "")
        SharedPrefHelper.setRememberMe(this, false)
*/
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun getOrderList() {
        mBinding.progressbar.visibility = View.VISIBLE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.getOrderList(SharedPrefHelper.getUserId(this), 1).enqueue(object : Callback<OrderListResponse> {
            override fun onResponse(call: Call<OrderListResponse>, response: Response<OrderListResponse>) {
                mBinding.progressbar.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.status) {
                        mBinding.orderCountTv.text = "Totol Orders: ${response.body()!!.totalRecords}"
                    }
                } else {
                    Utils.errorMessage(response, this@HomeActivity)
                }

            }

            override fun onFailure(call: Call<OrderListResponse>, t: Throwable) {
                mBinding.progressbar.visibility = View.GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }
        })
    }
}