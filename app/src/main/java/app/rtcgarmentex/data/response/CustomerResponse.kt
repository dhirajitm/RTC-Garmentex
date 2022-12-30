package app.rtcgarmentex.data.response

import com.google.gson.annotations.SerializedName

data class CustomerResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("firm_name")
    val firmName: String


) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return firmName
    }
}
