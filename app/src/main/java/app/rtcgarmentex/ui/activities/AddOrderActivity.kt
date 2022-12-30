package app.rtcgarmentex.ui.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import app.rtcgarmentex.R
import app.rtcgarmentex.data.ParticularModel
import app.rtcgarmentex.data.response.BaseResponseModel
import app.rtcgarmentex.data.response.CustomerResponse
import app.rtcgarmentex.data.response.SupplierResponse
import app.rtcgarmentex.databinding.ActivityAddOrderBinding
import app.rtcgarmentex.network.ApiClient
import app.rtcgarmentex.network.ApiService
import app.rtcgarmentex.ui.adapters.AddParticularAdapter
import app.rtcgarmentex.ui.listeners.ParticularListener
import app.rtcgarmentex.utils.SharedPrefHelper
import app.rtcgarmentex.utils.ToastHelper
import app.rtcgarmentex.utils.Utils
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddOrderActivity : BaseActivity(), ParticularListener {
    lateinit var mBinding: ActivityAddOrderBinding
    private val addParticularAdapter: AddParticularAdapter by lazy { AddParticularAdapter(this, this) }

    private val particularList = ArrayList<ParticularModel>()
    private var supplierList = ArrayList<SupplierResponse>()
    private var customerList = ArrayList<CustomerResponse>()
    private var supplierNames = ArrayList<String>()
    private var customerNames = ArrayList<String>()
    private var fromDate: String = ""
    private var toDate: String = ""
    private var customerId: String = ""
    private var supplierId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddOrderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initAdapter()
        getSupplierList()
        getCustomerList()

        mBinding.customerSp.setOnClickListener {
            showCustomerDialog()
        }

        mBinding.supplierSp.setOnClickListener {
            showSupplierDialog()
        }

        val today = Calendar.getInstance()
        mBinding.fromDateEt.setOnClickListener {
            openFromDatePicker()
        }

        mBinding.toDateEt.setOnClickListener {
            openToDatePicker()
        }

        mBinding.addIv.setOnClickListener {
            particularList.add(ParticularModel("", "", 0, 0))
            addParticularAdapter.setData(particularList)
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

    private fun showCustomerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        val editText = dialog.findViewById<EditText>(R.id.edit_text)
        (dialog.findViewById<TextView>(R.id.title)).setText(getString(R.string.select_customer))
        val listView = dialog.findViewById<ListView>(R.id.list_view)
        val adapter = ArrayAdapter<CustomerResponse>(this, android.R.layout.simple_list_item_1, customerList)
        listView.adapter = adapter

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        listView.setOnItemClickListener { parent, view, position, id ->
            mBinding.customerSp.setText(adapter.getItem(position)?.firmName) // The item that was clicked
            customerId = adapter.getItem(position)?.id.toString()
            dialog.dismiss()
        }
    }

    private fun showSupplierDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        val editText = dialog.findViewById<EditText>(R.id.edit_text)
        (dialog.findViewById<TextView>(R.id.title)).setText(getString(R.string.select_supplier))
        val listView = dialog.findViewById<ListView>(R.id.list_view)
        val adapter = ArrayAdapter<SupplierResponse>(this, android.R.layout.simple_list_item_1, supplierList)
        listView.adapter = adapter

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        listView.setOnItemClickListener { parent, view, position, id ->
            mBinding.supplierSp.setText(adapter.getItem(position)?.firmName) // The item that was clicked
            supplierId = adapter.getItem(position)?.id.toString()
//            ToastHelper.showSnackBar(mBinding.root, supplierId)
            dialog.dismiss()
        }
    }

    private fun openFromDatePicker() {
        val c = Calendar.getInstance()

        // on below line we are getting
        // our day, month and year.
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            this,
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                fromDate = (String.format("%04d", year) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth))
                mBinding.fromDateEt.setText(fromDate)
            },
            // on below line we are passing year, month
            // and day for the selected date in our date picker.
            y, m, d
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }

    private fun openToDatePicker() {
        val c = Calendar.getInstance()

        // on below line we are getting
        // our day, month and year.
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            this,
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                toDate = (String.format("%04d", year) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth))
                mBinding.toDateEt.setText(toDate)
            },
            // on below line we are passing year, month
            // and day for the selected date in our date picker.
            y, m, d
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }

    private fun getSupplierList() {
        mBinding.progressbar.visibility = VISIBLE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.getSupplierList().enqueue(object : Callback<ArrayList<SupplierResponse>> {
            override fun onResponse(call: Call<ArrayList<SupplierResponse>>, response: Response<ArrayList<SupplierResponse>>) {
                mBinding.progressbar.visibility = GONE
                if (response.isSuccessful) {
                    supplierList.clear()
                    supplierList = response.body()!!
                    supplierNames.clear()
                    for (s in supplierList) {
                        supplierNames.add(s.firmName)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<SupplierResponse>>, t: Throwable) {
                mBinding.progressbar.visibility = GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }

        })
    }

    private fun getCustomerList() {
        mBinding.progressbar.visibility = VISIBLE
        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.getCustomerList().enqueue(object : Callback<ArrayList<CustomerResponse>> {
            override fun onResponse(call: Call<ArrayList<CustomerResponse>>, response: Response<ArrayList<CustomerResponse>>) {
                mBinding.progressbar.visibility = GONE
                if (response.isSuccessful) {
                    customerList.clear()
                    customerList = response.body()!!
                    customerNames.clear()
                    for (c in customerList) {
                        customerNames.add(c.firmName)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<CustomerResponse>>, t: Throwable) {
                mBinding.progressbar.visibility = GONE
                ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
            }

        })
    }

    private fun submitData() {
        mBinding.submitBtn.isEnabled = false
        mBinding.progressbar.visibility = VISIBLE
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)

        multipartBody.addFormDataPart("emp_id", SharedPrefHelper.getUserId(this).toString())
        multipartBody.addFormDataPart("customer_id", customerId)
        multipartBody.addFormDataPart("supplier_id", supplierId)
        multipartBody.addFormDataPart("sub_party", mBinding.subPartyEt.text.toString())
        multipartBody.addFormDataPart("order_type", mBinding.orderTypeEt.text.toString())
        multipartBody.addFormDataPart("transport", mBinding.transportEt.text.toString())
        multipartBody.addFormDataPart("station", mBinding.stationEt.text.toString())
        multipartBody.addFormDataPart("dispatch_date_from", fromDate) //2022-09-29
        multipartBody.addFormDataPart("dispatch_date_to", toDate) //2022-09-29
        multipartBody.addFormDataPart("remarks", mBinding.remarkEt.text.toString())


        particularList.forEachIndexed { index, particularItem ->
            multipartBody.addFormDataPart("particular_name[$index]", particularItem.particular)
            multipartBody.addFormDataPart("item_type[$index]", particularItem.type)
            multipartBody.addFormDataPart("qty[$index]", particularItem.qty.toString())
            multipartBody.addFormDataPart("amount[$index]", particularItem.amount.toString())
        }

        val retrofit = ApiClient.buildService(ApiService::class.java)
        retrofit.postAddOrder(multipartBody.build())
            .enqueue(object : Callback<BaseResponseModel> {
                override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                    mBinding.submitBtn.isEnabled = true
                    mBinding.progressbar.visibility = GONE
                    if (response.isSuccessful) {
                        if (response.body()!!.status) {
                            ToastHelper.showSnackBar(mBinding.root, response.body()!!.message)
                            onBackPressed()
                        }
                    } else {
                        Utils.errorMessage(response, this@AddOrderActivity)
                    }
                }

                override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                    mBinding.submitBtn.isEnabled = true
                    mBinding.progressbar.visibility = GONE
                    ToastHelper.showSnackBar(mBinding.root, getString(R.string.api_failed))
                }
            })
    }

    private fun validated(): Boolean {
        if (customerId.isEmpty()) {
            ToastHelper.showSnackBar(mBinding.root, "Please select Customer")
            return false
        }

        if (supplierId.isEmpty()) {
            ToastHelper.showSnackBar(mBinding.root, "Please select Supplier")
            return false
        }

/*
        if (mBinding.subPartyEt.text.toString().trim().isEmpty()) {
            mBinding.subPartyEt.setText("")
            mBinding.subPartyEt.requestFocus()
            ToastHelper.showSnackBar(mBinding.root, "Sub Party required")
            return false
        }
*/

        if (mBinding.orderTypeEt.text.toString().trim().isEmpty()) {
            mBinding.orderTypeEt.setText("")
            mBinding.orderTypeEt.requestFocus()
            ToastHelper.showSnackBar(mBinding.root, "Order type required")
            return false
        }

        if (mBinding.transportEt.text.toString().trim().isEmpty()) {
            mBinding.transportEt.setText("")
            mBinding.transportEt.requestFocus()
            ToastHelper.showSnackBar(mBinding.root, "Transport required")
            return false
        }

        if (mBinding.stationEt.text.toString().trim().isEmpty()) {
            mBinding.stationEt.setText("")
            mBinding.stationEt.requestFocus()
            ToastHelper.showSnackBar(mBinding.root, "Station required")
            return false
        }

        if (fromDate.isEmpty()) {
            ToastHelper.showSnackBar(mBinding.root, "Dispatch Date From is required")
            return false
        }

        if (toDate.isEmpty()) {
            ToastHelper.showSnackBar(mBinding.root, "Dispatch To From is required")
            return false
        }

        for (p in particularList) {
            if (p.particular.isEmpty()) {
                ToastHelper.showSnackBar(mBinding.root, "Particular is required")
                return false
            }
            if (p.type.isEmpty()) {
                ToastHelper.showSnackBar(mBinding.root, "Type is required")
                return false
            }
            if (p.qty == 0) {
                ToastHelper.showSnackBar(mBinding.root, "Quantity required")
                return false
            }
            if (p.amount == 0) {
                ToastHelper.showSnackBar(mBinding.root, "Amount required")
                return false
            }
        }

        return true
    }

    private fun initAdapter() {
        mBinding.particularsRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.particularsRv.setHasFixedSize(true)
        mBinding.particularsRv.adapter = addParticularAdapter

        particularList.add(ParticularModel("", "", 0, 0))
        addParticularAdapter.setData(particularList)
    }

    override fun deleteParticular(position: Int) {
        particularList.removeAt(position)
        addParticularAdapter.setData(particularList)
    }

    override fun updateParticular(position: Int, item: ParticularModel) {
        particularList[position] = item
    }
}