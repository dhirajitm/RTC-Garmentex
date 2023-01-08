package app.rtcgarmentex.data.response.receivingResponse

data class ReceivedItem(
    val amount: Int,
    val date: String,
    val id: Int,
    val order_id: Int,
    val order_particular_id: Int,
    val received_date: String,
    val received_quantity: Int,
    val status: String
)