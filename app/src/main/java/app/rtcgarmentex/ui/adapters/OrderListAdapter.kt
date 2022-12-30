package app.rtcgarmentex.ui.adapters

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.rtcgarmentex.R
import app.rtcgarmentex.data.response.OrderListResponse
import app.rtcgarmentex.databinding.RowOrderListBinding
import app.rtcgarmentex.utils.ToastHelper

class OrderListAdapter(private val context: Context) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    private var orderList = mutableListOf<OrderListResponse.OrderDetails>()
    lateinit var mBinding: RowOrderListBinding

    init {
        orderList = ArrayList()
    }

    fun setData(data: ArrayList<OrderListResponse.OrderDetails>) {
        orderList.clear()
        orderList = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        mBinding = RowOrderListBinding.inflate(LayoutInflater.from(context), parent, false)
/*
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_order_list, parent, false)
*/

        return ViewHolder(mBinding.root)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = orderList[position]
        holder.srNo.text = (position + 1).toString()
        holder.orderNoTv.text = item.order_no
        holder.buyerTv.text = item.buyer_firm_name
        holder.supplierTv.text = item.supplier_firm_name
        holder.dateTv.text = item.order_date

        holder.itemView.setOnClickListener {
            /* In web view */
/*
            val intent = Intent(context, PdfViewActivity::class.java)
            intent.putExtra("url", item.pdflink)
            context.startActivity(intent)
*/
            /* Download */
            ToastHelper.showSnackBar(mBinding.root, "File downloading...")
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(item.pdflink.trim())
            val request = DownloadManager.Request(uri)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, item.file_name)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val l = manager.enqueue(request)
            Log.d("TAG", "onBindViewHolder: " + l)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return orderList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val srNo: TextView = itemView.findViewById(R.id.sr_no)
        val orderNoTv: TextView = itemView.findViewById(R.id.order_no_tv)
        val buyerTv: TextView = itemView.findViewById(R.id.buyer_tv)
        val supplierTv: TextView = itemView.findViewById(R.id.supplier_tv)
        val dateTv: TextView = itemView.findViewById(R.id.date_tv)
    }

}
