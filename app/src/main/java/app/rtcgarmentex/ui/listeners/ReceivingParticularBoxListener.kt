package app.rtcgarmentex.ui.listeners

import app.rtcgarmentex.data.response.receivingResponse.ParticularsData

interface ReceivingParticularBoxListener {
    fun onCompleteChecked(pos: Int, bool: Boolean)
    fun onItemUpdate(pos: Int, data: ParticularsData)
}