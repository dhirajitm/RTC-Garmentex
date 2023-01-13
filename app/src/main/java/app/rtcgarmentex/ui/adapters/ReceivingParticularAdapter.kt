package app.rtcgarmentex.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.rtcgarmentex.data.response.receivingResponse.OrderTotalItems
import app.rtcgarmentex.data.response.receivingResponse.ParticularsData
import app.rtcgarmentex.data.response.receivingResponse.ReceivedItem
import app.rtcgarmentex.databinding.RowReceivingBoxBinding
import app.rtcgarmentex.ui.listeners.ReceivingItemListener
import app.rtcgarmentex.ui.listeners.ReceivingParticularBoxListener

class ReceivingParticularAdapter(private var context: Context, private var listener: ReceivingParticularBoxListener) : RecyclerView.Adapter<ReceivingParticularAdapter.ViewHolder>(),
    ReceivingItemListener {
    private var particularList = mutableListOf<ParticularsData>()
    lateinit var mBinding: RowReceivingBoxBinding

    //    private val viewReceivingItemAdapter: ViewReceivingItemAdapter by lazy { ViewReceivingItemAdapter(context, this) }
    val viewPool = RecyclerView.RecycledViewPool();

    init {
        particularList = ArrayList()
    }

    inner class ViewHolder(val itemBinding: RowReceivingBoxBinding) : RecyclerView.ViewHolder(itemBinding.root), ReceivingItemListener {

        fun onBind(item: OrderTotalItems, position: Int) {
            itemBinding.snEt.setText((position + 1).toString())
            itemBinding.particularEt.setText(item.particular_name)
            itemBinding.typeEt.setText(item.type)
            itemBinding.qtyEt.setText(item.qty.toString())
            itemBinding.amountEt.setText(item.amount.toString())
            itemBinding.completedCb.isChecked = isCompleted(position)

            val viewReceivingItemAdapter = ViewReceivingItemAdapter(context, this)
            val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            layoutManager.initialPrefetchItemCount = particularList[position].receivedItems.size
            itemBinding.receivedItemRv.layoutManager = layoutManager
            itemBinding.receivedItemRv.adapter = viewReceivingItemAdapter
            viewReceivingItemAdapter.setData(particularList[position].receivedItems, position)
            itemBinding.receivedItemRv.setRecycledViewPool(viewPool)

            itemBinding.completedCb.setOnCheckedChangeListener { p0, p1 ->
                run {
                    particularList[position].competed = p1
                    listener.onCompleteChecked(position, p1)
                }
            }
        }

        override fun updateReceivedItem(pos: Int, item: ReceivedItem) {

        }
    }


    fun setData(data: ArrayList<ParticularsData>) {
        particularList.clear()
        particularList = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mBinding = RowReceivingBoxBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = particularList[position].orderTotalItems
        holder.onBind(item, position)
    }

    override fun getItemCount(): Int {
        return particularList.size
    }

    override fun updateReceivedItem(pos: Int, item: ReceivedItem) {
        particularList[item.parentPos].receivedItems[pos] = item
        listener.onItemUpdate(item.parentPos, particularList[item.parentPos])
    }

    private fun isCompleted(position: Int): Boolean {
        if (particularList[position].receivedItems.isNotEmpty()) {
            return particularList[position].receivedItems.last().status == "1"
        }
        return false
    }

/*
    override fun updateAmount(parentPos: Int, itemPos: Int, amount: String) {
        particularList[parentPos].receivedItems[itemPos].amount = amount.toInt()
        listener.onItemUpdate(parentPos, particularList[parentPos])
    }

    override fun updateQty(parentPos: Int, itemPos: Int, receivedQty: String) {
        particularList[parentPos].receivedItems[itemPos].received_quantity = receivedQty.toInt()
        listener.onItemUpdate(parentPos, particularList[parentPos])
    }

    override fun updateDate(parentPos: Int, itemPos: Int, receivedDate: String) {
        particularList[parentPos].receivedItems[itemPos].received_date = receivedDate
        listener.onItemUpdate(parentPos, particularList[parentPos])
    }
*/

}