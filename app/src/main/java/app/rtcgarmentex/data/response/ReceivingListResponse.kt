package app.rtcgarmentex.data.response

data class ReceivingListResponse(
    val totalRecords: Int,
    val totalPage: Int,
    val data: ArrayList<ReceivingDetails>
) : BaseResponseModel() {
    data class ReceivingDetails(
        val status: String,
        val order_no: String,
        val customer_firm_name: String,
        val supplier_firm_name: String,
        val order_date: String
    )
}