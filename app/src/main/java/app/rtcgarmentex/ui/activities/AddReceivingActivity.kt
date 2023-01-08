package app.rtcgarmentex.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import app.rtcgarmentex.R
import app.rtcgarmentex.data.response.ReceivingOrdersSearchListResponse
import app.rtcgarmentex.data.response.receivingResponse.ParticularsData
import app.rtcgarmentex.data.response.receivingResponse.ReceivedItem
import app.rtcgarmentex.data.response.receivingResponse.ReceivingDetailResponse
import app.rtcgarmentex.databinding.ActivityAddReceivingBinding
import app.rtcgarmentex.network.ApiClient
import app.rtcgarmentex.network.ApiService
import app.rtcgarmentex.ui.adapters.AddParticularAdapter
import app.rtcgarmentex.ui.adapters.ReceivingParticularAdapter
import app.rtcgarmentex.utils.SharedPrefHelper
import app.rtcgarmentex.utils.ToastHelper
import app.rtcgarmentex.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReceivingActivity : BaseActivity() {
    lateinit var mBinding: ActivityAddReceivingBinding
    private var ordersList = ArrayList<ReceivingOrdersSearchListResponse.OrderSearch>()
    private val receivingParticularAdapter: ReceivingParticularAdapter by lazy { ReceivingParticularAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
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
    }

    private fun getOrderReceivingDetails() {
        mBinding.progressbar.visibility = View.VISIBLE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.getReceivingDetails(SharedPrefHelper.getUserId(this), mBinding.orderNo.text.toString()).enqueue(object : Callback<ReceivingDetailResponse> {
            override fun onResponse(call: Call<ReceivingDetailResponse>, response: Response<ReceivingDetailResponse>) {
                mBinding.progressbar.visibility = View.GONE
                if (response.isSuccessful) {
                    setReceivingData(response.body()!!)
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

    private fun setReceivingData(body: ReceivingDetailResponse) {
        try {
            mBinding.customerSp.setText(body.data.customer_firm_name)
            mBinding.supplierSp.setText(body.data.supplier_firm_name)
            mBinding.subPartyEt.setText(body.data.sub_party)
            mBinding.orderTypeEt.setText(body.data.order_type)
            mBinding.transportEt.setText(body.data.transport)
            mBinding.stationEt.setText(body.data.station)
            mBinding.fromDateEt.setText(body.data.dispatch_date_from)
            mBinding.toDateEt.setText(body.data.dispatch_date_to)
            mBinding.remarkEt.setText(body.data.remarks)

            /*add empty received row*/
            if (body.data.particularsData.isNotEmpty()) {
                for (item in body.data.particularsData) {
                    if (item.receivedItems.isNotEmpty() && item.receivedItems.get(item.receivedItems.size - 1).status == "1") {

                    } else {
                        item.receivedItems.add(ReceivedItem(0, "", 0, 0, 0, "", 0, "0"))
                    }
                }
            }

            mBinding.receivingBoxRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            mBinding.receivingBoxRv.setHasFixedSize(true)
            mBinding.receivingBoxRv.adapter = receivingParticularAdapter
            receivingParticularAdapter.setData(body.data.particularsData as ArrayList<ParticularsData>)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
        retrofit.getReceivingSearchList(SharedPrefHelper.getUserId(this)).enqueue(object : Callback<ReceivingOrdersSearchListResponse> {
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
}