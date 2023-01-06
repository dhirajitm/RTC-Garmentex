package app.rtcgarmentex.data.response

import com.google.gson.annotations.SerializedName

data class StringResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String


) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return name
    }
}
