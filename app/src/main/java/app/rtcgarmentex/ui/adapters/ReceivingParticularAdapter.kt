package app.rtcgarmentex.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.rtcgarmentex.R
import app.rtcgarmentex.data.response.receivingResponse.ParticularsData
import app.rtcgarmentex.data.response.receivingResponse.ReceivedItem
import app.rtcgarmentex.databinding.RowReceivingBoxBinding

class ReceivingParticularAdapter(private val context: Context) : RecyclerView.Adapter<ReceivingParticularAdapter.ViewHolder>() {
    private var particularList = mutableListOf<ParticularsData>()
    lateinit var mBinding: RowReceivingBoxBinding
    private val viewReceivingItemAdapter: ViewReceivingItemAdapter by lazy { ViewReceivingItemAdapter(context) }

    init {
        particularList = ArrayList()
    }

    fun setData(data: ArrayList<ParticularsData>) {
        particularList.clear()
        particularList = data.toMutableList()
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val srNo: EditText = itemView.findViewById(R.id.sn_et)
        val particularEt: EditText = itemView.findViewById(R.id.particular_et)
        val typeEt: EditText = itemView.findViewById(R.id.type_et)
        val qtyEt: EditText = itemView.findViewById(R.id.qty_et)
        val amountEt: EditText = itemView.findViewById(R.id.amount_et)
        val receivedItemRv: RecyclerView = itemView.findViewById(R.id.received_item_rv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = RowReceivingBoxBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(mBinding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = particularList[position].orderTotalItems
        holder.srNo.setText((position + 1).toString())
        holder.particularEt.setText(item.particular_name)
        holder.typeEt.setText(item.type)
        holder.qtyEt.setText(item.qty.toString())
        holder.amountEt.setText(item.amount.toString())

        holder.receivedItemRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.receivedItemRv.setHasFixedSize(true)
        holder.receivedItemRv.adapter = viewReceivingItemAdapter
        viewReceivingItemAdapter.setData(particularList[position].receivedItems as ArrayList<ReceivedItem>)

    }

    override fun getItemCount(): Int {
        return particularList.size
    }

}