package app.rtcgarmentex.data.response.receivingResponse

data class ParticularsData(
    val orderTotalItems: OrderTotalItems,
    val receivedItems: ArrayList<ReceivedItem>
)