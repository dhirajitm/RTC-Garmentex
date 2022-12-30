package app.rtcgarmentex.data.request

data class PasswordRequest(val emp_id: Int, val old_password: String, val new_password: String, val confirm_password: String)
