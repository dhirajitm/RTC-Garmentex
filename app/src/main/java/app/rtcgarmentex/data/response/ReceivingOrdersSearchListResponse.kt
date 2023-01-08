package app.rtcgarmentex.data.response

import com.google.gson.annotations.SerializedName

data class ReceivingOrdersSearchListResponse(
    val data: ArrayList<OrderSearch>
) : BaseResponseModel() {
    data class OrderSearch(
        @SerializedName("id")
        val id: Int,
        @SerializedName("order_no")
        val orderNo: String
    ) {
        override fun equals(other: Any?): Boolean {
            return super.equals(other)
        }

        override fun hashCode(): Int {
            return id
        }

        override fun toString(): String {
            return orderNo
        }
    }
}
