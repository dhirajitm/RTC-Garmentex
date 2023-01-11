package app.rtcgarmentex.data.response.receivingResponse

data class ReceivedItem(
    var amount: Int,
    val date: String,
    val id: Int,
    val order_id: Int,
    val order_particular_id: Int,
    var received_date: String,
    var received_quantity: Int,
    val status: String,
    var parentPos:Int
)