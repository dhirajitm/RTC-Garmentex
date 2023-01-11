package app.rtcgarmentex.ui.listeners

import app.rtcgarmentex.data.response.receivingResponse.ReceivedItem

interface ReceivingItemListener {
    //    fun updateAmount(parentPos: Int, itemPos: Int, amount: String)
//    fun updateQty(parentPos: Int, itemPos: Int, receivedQty: String)
//    fun updateDate(parentPos: Int, itemPos: Int, receivedDate: String)
    fun updateReceivedItem(pos: Int, item: ReceivedItem)
}