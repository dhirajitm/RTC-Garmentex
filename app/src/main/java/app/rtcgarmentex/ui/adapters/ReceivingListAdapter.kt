package app.rtcgarmentex.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.rtcgarmentex.R
import app.rtcgarmentex.data.response.ReceivingListResponse
import app.rtcgarmentex.databinding.RowOrderListBinding
import app.rtcgarmentex.databinding.RowReceivingListBinding

class ReceivingListAdapter(private val context: Context) : RecyclerView.Adapter<ReceivingListAdapter.ViewHolder>() {
    private var orderList = mutableListOf<ReceivingListResponse.ReceivingDetails>()
    lateinit var mBinding: RowReceivingListBinding

    init {
        orderList = ArrayList()
    }

    fun setData(data: ArrayList<ReceivingListResponse.ReceivingDetails>) {
        orderList.clear()
        orderList = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        mBinding = RowReceivingListBinding.inflate(LayoutInflater.from(context), parent, false)
/*
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_order_list, parent, false)
*/

        return ViewHolder(mBinding.root)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = orderList[position]
        holder.srNo.text = "S.No. " + (position + 1).toString()
        holder.orderNoTv.text = item.order_no
        holder.buyerTv.text = item.customer_firm_name
        holder.supplierTv.text = item.supplier_firm_name
        holder.dateTv.text = item.order_date
        if (item.status == "0") {
            holder.status.setText("Pending")
        } else {
            holder.status.setText("Completed")
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return orderList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val srNo: TextView = itemView.findViewById(R.id.sr_no)
        val status: TextView = itemView.findViewById(R.id.status)
        val orderNoTv: TextView = itemView.findViewById(R.id.order_no_tv)
        val buyerTv: TextView = itemView.findViewById(R.id.buyer_tv)
        val supplierTv: TextView = itemView.findViewById(R.id.supplier_tv)
        val dateTv: TextView = itemView.findViewById(R.id.date_tv)
    }

}
