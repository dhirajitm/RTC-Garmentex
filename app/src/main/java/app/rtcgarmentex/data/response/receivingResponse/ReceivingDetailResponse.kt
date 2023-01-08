package app.rtcgarmentex.data.response.receivingResponse

import app.rtcgarmentex.data.response.BaseResponseModel

data class ReceivingDetailResponse(
    val data: ReceivingDetails
) : BaseResponseModel() {
    data class ReceivingDetails(
        val customer_firm_name: String,
        val customer_id: Int,
        val date: String,
        val dispatch_date_from: String,
        val dispatch_date_to: String,
        val employee_id: Int,
        val file_name: String,
        val id: Int,
        val order_date: String,
        val order_no: String,
        val order_type: String,
        val particularsData: ArrayList<ParticularsData>,
        val receivingStatus: String,
        val remarks: String,
        val station: String,
        val sub_party: String,
        val supplier_firm_name: String,
        val supplier_id: Int,
        val transport: String
    )
}