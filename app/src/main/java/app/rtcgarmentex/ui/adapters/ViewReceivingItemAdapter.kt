package app.rtcgarmentex.ui.adapters

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import app.rtcgarmentex.R
import app.rtcgarmentex.data.response.receivingResponse.ReceivedItem
import app.rtcgarmentex.databinding.RowReceivedItemBinding
import java.util.*

class ViewReceivingItemAdapter(private val context: Context) : RecyclerView.Adapter<ViewReceivingItemAdapter.ViewHolder>() {
    private var receivedItemList = mutableListOf<ReceivedItem>()
    lateinit var mBinding: RowReceivedItemBinding

    init {
        receivedItemList = ArrayList()
    }

    fun setData(data: ArrayList<ReceivedItem>) {
        receivedItemList.clear()
        receivedItemList = data.toMutableList()
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivingDateEt: EditText = itemView.findViewById(R.id.receiving_date_et)
        val receivingQtyEt: EditText = itemView.findViewById(R.id.receiving_qty_et)
        val receivingAmountEt: EditText = itemView.findViewById(R.id.receiving_amount_et)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = RowReceivedItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(mBinding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = receivedItemList[position]
        holder.receivingDateEt.setText("")
        holder.receivingQtyEt.setText("")
        holder.receivingAmountEt.setText("")

        // Completed
        if (item.status == "1") {
            holder.receivingDateEt.isEnabled = false
            holder.receivingQtyEt.isEnabled = false
            holder.receivingAmountEt.isEnabled = false
        } else {
            holder.receivingDateEt.isEnabled = true
            holder.receivingQtyEt.isEnabled = true
            holder.receivingAmountEt.isEnabled = true
            holder.receivingDateEt.setText(item.received_date)
            if (item.received_quantity > 0)
                holder.receivingQtyEt.setText(item.received_quantity.toString())
            if (item.amount > 0)
                holder.receivingAmountEt.setText(item.amount.toString())
        }

        holder.receivingDateEt.setOnClickListener {
            openFromDatePicker(holder.receivingDateEt)
        }
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
            y, m, d
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }

}