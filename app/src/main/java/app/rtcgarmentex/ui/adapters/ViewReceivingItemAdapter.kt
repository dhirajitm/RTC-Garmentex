package app.rtcgarmentex.ui.adapters

import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import app.rtcgarmentex.data.ParticularModel
import app.rtcgarmentex.data.response.receivingResponse.ReceivedItem
import app.rtcgarmentex.databinding.RowAddParticularsBinding
import app.rtcgarmentex.databinding.RowReceivedItemBinding
import app.rtcgarmentex.ui.listeners.ReceivingItemListener
import java.util.*

class ViewReceivingItemAdapter(private var context: Context, private var listener: ReceivingItemListener) : RecyclerView.Adapter<ViewReceivingItemAdapter.ViewHolder>() {
    private var receivedItemList = mutableListOf<ReceivedItem>()
    lateinit var mBinding: RowReceivedItemBinding
    var parentPos = -1

    init {
        receivedItemList = ArrayList()
    }

    fun setData(data: ArrayList<ReceivedItem>, pos: Int) {
        parentPos = pos
        receivedItemList.clear()
        receivedItemList = data.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val itemBinding: RowReceivedItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun onBind(item: ReceivedItem, position: Int) {
            itemBinding.receivingDateEt.setText("")
            itemBinding.receivingQtyEt.setText("")
            itemBinding.receivingAmountEt.setText("")

            // Completed
            if (item.status != "new") {
                itemBinding.receivingDateEt.isEnabled = false
                itemBinding.receivingQtyEt.isEnabled = false
                itemBinding.receivingAmountEt.isEnabled = false
            } else {
                itemBinding.receivingDateEt.isEnabled = true
                itemBinding.receivingQtyEt.isEnabled = true
                itemBinding.receivingAmountEt.isEnabled = true
            }
            itemBinding.receivingDateEt.setText(item.received_date)
            if (item.received_quantity > 0)
                itemBinding.receivingQtyEt.setText(item.received_quantity.toString())
            if (item.amount > 0)
                itemBinding.receivingAmountEt.setText(item.amount.toString())

            itemBinding.receivingDateEt.setOnClickListener {
                openFromDatePicker(itemBinding.receivingDateEt)
            }

            itemBinding.receivingDateEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        item.received_date = p0.toString()
//                        listener.updateReceivedItem(position, item)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            itemBinding.receivingAmountEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        item.amount = p0.toString().toInt()
//                        listener.updateReceivedItem(position, item)
                    } else {
                        item.amount = 0
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            itemBinding.receivingQtyEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        item.received_quantity = p0.toString().toInt()
//                        listener.updateReceivedItem(position, item)
                    } else {
                        item.received_quantity = 0
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = RowReceivedItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = receivedItemList[position]
        holder.onBind(item, position)
    }

    override fun getItemCount(): Int {
        return receivedItemList.size
    }


    private fun openFromDatePicker(receivingDateEt: EditText) {
        val c = Calendar.getInstance()

        // on below line we are getting
        // our day, month and year.
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            context,
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val fromDate = (String.format("%04d", year) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth))
                receivingDateEt.setText(fromDate)
            },
            // on below line we are passing year, month
            // and day for the selected date in our date picker.
            y, m, d,
        )
        datePickerDialog.datePicker.maxDate = Date().time
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }

}
