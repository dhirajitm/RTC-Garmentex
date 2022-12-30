package app.rtcgarmentex.data.response

import com.google.gson.annotations.SerializedName

open class BaseResponseModel(
    @SerializedName("status")
    val status: Boolean = false,
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("errors")
    val errors: String = "",
    @SerializedName("message")
    val message: String = "",
)
