package app.rtcgarmentex.data.response.receivingResponse

data class OrderTotalItems(
    val amount: Int,
    val date: String,
    val id: Int,
    val order_id: Int,
    val particular_name: String,
    val qty: Int,
    val type: String
)