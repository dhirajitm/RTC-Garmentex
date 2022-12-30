package app.rtcgarmentex.data.response

import com.google.gson.annotations.SerializedName

data class SupplierResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("firm_name")
    val firmName: String
) {
    override fun toString(): String {
        return firmName
    }
}

