package app.rtcgarmentex.data.response

data class OrderListResponse(
    val totalRecords: Int,
    val totalPage: Int,
    val data: ArrayList<OrderDetails>
) : BaseResponseModel() {
    data class OrderDetails(
        val buyer_firm_name: String,
        val customer_id: Int,
        val date: String,
        val dispatch_date_from: String,
        val dispatch_date_to: String,
        val employee_id: Int,
        val file_name: String,
        val employee_mobile1: Long,
        val employee_name: String,
        val id: Int,
        val order_date: String,
        val order_no: String,
        val order_type: String,
        val pdflink: String,
        val remarks: String,
        val station: String,
        val sub_party: String,
        val supplier_firm_name: String,
        val supplier_id: Int,
        val transport: String
    )
}
