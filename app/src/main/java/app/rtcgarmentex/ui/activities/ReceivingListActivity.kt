package app.rtcgarmentex.ui.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.View.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import app.rtcgarmentex.R
import app.rtcgarmentex.data.response.ReceivingListResponse
import app.rtcgarmentex.databinding.ActivityOrderListBinding
import app.rtcgarmentex.network.ApiClient
import app.rtcgarmentex.network.ApiService
import app.rtcgarmentex.ui.adapters.ReceivingListAdapter
import app.rtcgarmentex.utils.SharedPrefHelper
import app.rtcgarmentex.utils.ToastHelper
import app.rtcgarmentex.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceivingListActivity : BaseActivity() {
    lateinit var mBinding: ActivityOrderListBinding
    private val receivingListAdapter: ReceivingListAdapter by lazy { ReceivingListAdapter(this) }
    private var currentPage = 1
    private var totalPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.orderListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.orderListRv.setHasFixedSize(true)
        mBinding.orderListRv.adapter = receivingListAdapter

        getOrderListPage()

        mBinding.backIv.setOnClickListener {
            onBackPressed()
        }
        mBinding.searchIv.visibility = INVISIBLE
        mBinding.searchIv.setOnClickListener {
            if (mBinding.searchEt.visibility == VISIBLE) {
                getOrderListSearch()
            }
            mBinding.logo.visibility = GONE
            mBinding.searchEt.visibility = VISIBLE
        }
        mBinding.searchEt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                    getOrderListSearch()
                    return true;
                }
                return true
            }

        })

        mBinding.nextIv.setOnClickListener {
            currentPage++
            getOrderListPage()
        }

        mBinding.preIv.setOnClickListener {
            currentPage--
            getOrderListPage()
        }

        setPageView()

    }

    private fun getOrderListPage() {
        mBinding.progressbar.visibility = VISIBLE
        mBinding.noRecordTv.visibility = GONE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.getReceivingList(getHeaderMap(), SharedPrefHelper.getUserId(this), currentPage).enqueue(object : Callback<ReceivingListResponse> {
            override fun onResponse(call: Call<ReceivingListResponse>, response: Response<ReceivingListResponse>) {
                mBinding.progressbar.visibility = GONE
                if (response.isSuccessful) {
                    receivingListAdapter.setData(response.body()!!.data)
                    totalPage = response.body()!!.totalPage
                    setPageView()
                    if (response.body()!!.data.size == 0 && currentPage == 1) {
                        mBinding.noRecordTv.visibility = VISIBLE
                    }
                } else {
                    Utils.errorMessage(response, this@ReceivingListActivity)
                }
            }

            override fun onFailure(call: Call<ReceivingListResponse>, t: Throwable) {
                mBinding.progressbar.visibility = GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }
        })
    }

    private fun validated(): Boolean {
        if (mBinding.searchEt.text.toString().trim().isEmpty()) {
            mBinding.searchEt.setText("")
            ToastHelper.showSnackBar(mBinding.root, "Please enter search keyword")
            return false
        }
        return true
    }

    private fun getOrderListSearch() {
        if (!validated()) {
            return
        }
        Utils.hideKeyboard(this)
        mBinding.progressbar.visibility = VISIBLE
        mBinding.noRecordTv.visibility = GONE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.getReceivingList(getHeaderMap(), SharedPrefHelper.getUserId(this), 1).enqueue(object : Callback<ReceivingListResponse> {
            override fun onResponse(call: Call<ReceivingListResponse>, response: Response<ReceivingListResponse>) {
                mBinding.progressbar.visibility = GONE
                if (response.isSuccessful) {
                    if (response.body()!!.status) {
                        receivingListAdapter.setData(response.body()!!.data)
                        totalPage = response.body()!!.totalPage
                        mBinding.pageLl.visibility = GONE
                        if (response.body()!!.data.size == 0) {
                            mBinding.noRecordTv.visibility = VISIBLE
                        }
                    }
                } else {
                    mBinding.noRecordTv.visibility = VISIBLE
                    Utils.errorMessage(response, this@ReceivingListActivity)
                }
            }

            override fun onFailure(call: Call<ReceivingListResponse>, t: Throwable) {
                mBinding.progressbar.visibility = GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }
        })
    }

    private fun setPageView() {
        mBinding.preIv.isEnabled = true
        mBinding.preIv.setBackgroundColor(resources.getColor(R.color.app_red))
        mBinding.nextIv.isEnabled = true
        mBinding.nextIv.setBackgroundColor(resources.getColor(R.color.app_red))
        mBinding.currentPage.text = currentPage.toString()

        if (totalPage == 0) {
            mBinding.pageLl.visibility = GONE
        } else {
            mBinding.pageLl.visibility = VISIBLE
            // Left visible
            if (currentPage <= 1) {
                mBinding.preIv.isEnabled = false
                mBinding.preIv.setBackgroundColor(resources.getColor(R.color.et_disabled))
            }
            // right visible
            if (currentPage >= totalPage) {
                mBinding.nextIv.isEnabled = false
                mBinding.nextIv.setBackgroundColor(resources.getColor(R.color.et_disabled))
            }
        }
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["userId"] = SharedPrefHelper.getUserId(this).toString()
        headerMap["branchId"] = SharedPrefHelper.getBranchId(this).toString()
        headerMap["token"] = SharedPrefHelper.getUserToken(this)
        return headerMap
    }

}