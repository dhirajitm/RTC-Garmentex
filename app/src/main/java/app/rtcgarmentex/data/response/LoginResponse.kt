package app.rtcgarmentex.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: LoginData
) : BaseResponseModel() {

    data class LoginData(
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("phone")
        val phone: Long,
        @SerializedName("status")
        val status: String
    )
}