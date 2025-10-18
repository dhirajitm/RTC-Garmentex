package app.rtcgarmentex.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import app.rtcgarmentex.R
import app.rtcgarmentex.data.response.BaseResponseModel
import app.rtcgarmentex.data.response.ReceivingOrdersSearchListResponse
import app.rtcgarmentex.data.response.receivingResponse.ParticularsData
import app.rtcgarmentex.data.response.receivingResponse.ReceivedItem
import app.rtcgarmentex.data.response.receivingResponse.ReceivingDetailResponse
import app.rtcgarmentex.databinding.ActivityAddReceivingBinding
import app.rtcgarmentex.network.ApiClient
import app.rtcgarmentex.network.ApiService
import app.rtcgarmentex.ui.adapters.ReceivingParticularAdapter
import app.rtcgarmentex.ui.listeners.ReceivingParticularBoxListener
import app.rtcgarmentex.utils.SharedPrefHelper
import app.rtcgarmentex.utils.ToastHelper
import app.rtcgarmentex.utils.Utils
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReceivingActivity : BaseActivity(), ReceivingParticularBoxListener {
    lateinit var mBinding: ActivityAddReceivingBinding
    private var ordersList = ArrayList<ReceivingOrdersSearchListResponse.OrderSearch>()
    private val receivingParticularAdapter: ReceivingParticularAdapter by lazy { ReceivingParticularAdapter(this, this) }
    var receivingDetails: ReceivingDetailResponse.ReceivingDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddReceivingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        getSearchOrderListItems();

        mBinding.orderNo.setOnClickListener {
            showOrdersDialog()
        }

        mBinding.searchBtn.setOnClickListener {
            if (mBinding.orderNo.text.toString().isNotEmpty()) {
                // fetch Order receiving
                getOrderReceivingDetails()
            } else {
                ToastHelper.showToast(this, "Order is empty")
            }
        }

        mBinding.submitBtn.setOnClickListener {
            if (validated()) {
                submitData()
            }
        }
        mBinding.backIv.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validated(): Boolean {
        val particularList = receivingDetails!!.particularsData
        for (particularItem in particularList) {
            val item = particularItem.receivedItems.last()
            var checkCount = 0
            if (item.status == "new") {
                if (item.received_date.isNotEmpty())
                    checkCount++
                if (item.received_quantity > 0)
                    checkCount++
                if (item.amount > 0)
                    checkCount++

            }
            if (checkCount == 0 || checkCount == 3) {
//                return true
            } else {
                if (item.received_date.isEmpty())
                    ToastHelper.showSnackBar(mBinding.root, "Received date is empty for ${particularItem.orderTotalItems.particular_name}")
                else if (item.received_quantity == 0)
                    ToastHelper.showSnackBar(mBinding.root, "Received Qty is empty for ${particularItem.orderTotalItems.particular_name}")
                else if (item.amount == 0)
                    ToastHelper.showSnackBar(mBinding.root, "Received Amount is empty for ${particularItem.orderTotalItems.particular_name}")

                return false
            }
        }

        for (particularItem in particularList) {
            val totalItem = particularItem.orderTotalItems.qty
            var itemInRec = 0
            for (item in particularItem.receivedItems) {
                itemInRec += item.received_quantity
            }
            if (itemInRec > totalItem) {
                ToastHelper.showSnackBar(mBinding.root, "Received Qty is more than Order Qty for ${particularItem.orderTotalItems.particular_name}")
                return false
            }
        }


        return true
    }

    private fun getOrderReceivingDetails() {
        mBinding.progressbar.visibility = View.VISIBLE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.getReceivingDetails(getHeaderMap(), SharedPrefHelper.getUserId(this), mBinding.orderNo.text.toString()).enqueue(object : Callback<ReceivingDetailResponse> {
            override fun onResponse(call: Call<ReceivingDetailResponse>, response: Response<ReceivingDetailResponse>) {
                mBinding.progressbar.visibility = View.GONE
                if (response.isSuccessful) {
                    receivingDetails = response.body()!!.data
                    setReceivingData(receivingDetails!!)
                } else {
                    Utils.errorMessage(response, this@AddReceivingActivity)
                }
            }

            override fun onFailure(call: Call<ReceivingDetailResponse>, t: Throwable) {
                mBinding.progressbar.visibility = View.GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }
        })

    }

    private fun setReceivingData(body: ReceivingDetailResponse.ReceivingDetails) {
        try {
            var parentPos = 0
            mBinding.customerSp.setText(body.customer_firm_name)
            mBinding.supplierSp.setText(body.supplier_firm_name)
            mBinding.subPartyEt.setText(body.sub_party)
            mBinding.orderTypeEt.setText(body.order_type)
            mBinding.transportEt.setText(body.transport)
            mBinding.stationEt.setText(body.station)
            mBinding.fromDateEt.setText(body.dispatch_date_from)
            mBinding.toDateEt.setText(body.dispatch_date_to)
            if (body.receivingStatus.toInt() == 0) {
                mBinding.orderStatusSp.setSelection(0)
            } else if (body.receivingStatus.toInt() == 1) {
                mBinding.orderStatusSp.setSelection(2)
            } else {
                mBinding.orderStatusSp.setSelection(1)
            }
            mBinding.remarkEt.setText(body.remarks)

            /*add empty received row*/
            if (body.particularsData.isNotEmpty()) {
                for (item in body.particularsData) {
                    if (item.receivedItems.isNotEmpty() && item.receivedItems.last().status == "1") {
                        item.competed = true
                    } else {
//                        item.receivedItems.add(ReceivedItem(40 + parentPos, "", 0, 0, 0, "2023-01-09", 5, "1", parentPos))
                        item.receivedItems.add(ReceivedItem(0, "", item.orderTotalItems.id, 0, 0, "", 0, "new", parentPos))
                    }
                    parentPos++
                    Log.d("TAG", "onCreate: " + item.receivedItems)
                }
            }

            mBinding.receivingBoxRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            mBinding.receivingBoxRv.setHasFixedSize(true)
            mBinding.receivingBoxRv.adapter = receivingParticularAdapter
            receivingParticularAdapter.setData(body.particularsData)
            mBinding.submitBtn.isEnabled = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun submitData() {
        var index = 0
        mBinding.submitBtn.isEnabled = false
        mBinding.progressbar.visibility = View.VISIBLE
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)

//        multipartBody.addFormDataPart("emp_id", SharedPrefHelper.getUserId(this).toString())
        multipartBody.addFormDataPart("order_id", receivingDetails!!.id.toString())
        val pos = mBinding.orderStatusSp.selectedItemPosition
        multipartBody.addFormDataPart("order_status", if (pos == 0) "0" else if (pos == 1) "2" else "1")
        val particularList = receivingDetails!!.particularsData
        for (particularItem in particularList) {
            val item = particularItem.receivedItems.last()
            multipartBody.addFormDataPart("particular_id[$index]", particularItem.orderTotalItems.id.toString())
            if (item.status == "new" && item.received_quantity > 0) {
                multipartBody.addFormDataPart("particular_received_date[$index]", item.received_date)
                multipartBody.addFormDataPart("particular_received_qty[$index]", item.received_quantity.toString())
                multipartBody.addFormDataPart("particular_received_amount[$index]", item.amount.toString())
            } else {
                multipartBody.addFormDataPart("particular_received_date[$index]", "")
                multipartBody.addFormDataPart("particular_received_qty[$index]", "")
                multipartBody.addFormDataPart("particular_received_amount[$index]", "")
            }
            multipartBody.addFormDataPart("particular_received_status[$index]", if (particularItem.competed) "1" else "0")
            index++
        }

        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.postAddReceiving(getHeaderMap(), multipartBody.build())
            .enqueue(
                object : Callback<BaseResponseModel> {
                    override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                        mBinding.submitBtn.isEnabled = true
                        mBinding.progressbar.visibility = View.GONE
                        if (response.isSuccessful) {
                            if (response.body()!!.status) {
                                ToastHelper.showSnackBar(mBinding.root, response.body()!!.message)
                                onBackPressed()
                            }
                        } else {
                            Utils.errorMessage(response, this@AddReceivingActivity)
                        }
                    }

                    override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                        mBinding.submitBtn.isEnabled = true
                        mBinding.progressbar.visibility = View.GONE
                        ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
                    }
                })
    }


    private fun showOrdersDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        val editText = dialog.findViewById<EditText>(R.id.edit_text)
        (dialog.findViewById<TextView>(R.id.title)).setText(getString(R.string.select_order))
        val listView = dialog.findViewById<ListView>(R.id.list_view)
        val adapter = ArrayAdapter<ReceivingOrdersSearchListResponse.OrderSearch>(this, android.R.layout.simple_list_item_1, ordersList)
        listView.adapter = adapter

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        listView.setOnItemClickListener { parent, view, position, id ->
            mBinding.orderNo.setText(adapter.getItem(position)?.orderNo) // The item that was clicked
            //customerId = adapter.getItem(position)?.id.toString()
            dialog.dismiss()
        }
    }


    private fun getSearchOrderListItems() {
        mBinding.progressbar.visibility = View.VISIBLE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.getReceivingSearchList(getHeaderMap(), SharedPrefHelper.getUserId(this)).enqueue(object : Callback<ReceivingOrdersSearchListResponse> {
            override fun onResponse(call: Call<ReceivingOrdersSearchListResponse>, response: Response<ReceivingOrdersSearchListResponse>) {
                mBinding.progressbar.visibility = View.GONE
                if (response.isSuccessful) {
                    ordersList = response.body()!!.data
                } else {
                    Utils.errorMessage(response, this@AddReceivingActivity)
                }
            }

            override fun onFailure(call: Call<ReceivingOrdersSearchListResponse>, t: Throwable) {
                mBinding.progressbar.visibility = View.GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }
        })
    }

    override fun onCompleteChecked(pos: Int, bool: Boolean) {
        receivingDetails!!.particularsData[pos].competed = bool
    }

    override fun onItemUpdate(pos: Int, data: ParticularsData) {
        if (data.receivedItems.size > 0)
            receivingDetails!!.particularsData[data.receivedItems[0].parentPos] = data
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["userId"] = SharedPrefHelper.getUserId(this).toString()
        headerMap["branchId"] = SharedPrefHelper.getBranchId(this).toString()
        headerMap["token"] = SharedPrefHelper.getUserToken(this)
        return headerMap
    }

}