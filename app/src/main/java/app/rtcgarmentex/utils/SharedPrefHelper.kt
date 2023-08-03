package app.rtcgarmentex.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefHelper {
    companion object {
        private const val APP_PREFERENCES = "app_preferences"

        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_USER_ID = "id"
        private const val KEY_BRANCH_ID = "branch_id"
        private const val KEY_TOKEN = "token"
        private const val KEY_PASSWORD = "password"
        private const val KEY_USER_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_STATUS = "status"

        private fun getSharedPreference(context: Context): SharedPreferences {
            return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        }

        private fun getSharedPreferenceEditor(context: Context): SharedPreferences.Editor {
            return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit()
        }

        fun setRememberMe(context: Context, value: Boolean) {
            getSharedPreferenceEditor(context).putBoolean(KEY_REMEMBER_ME, value).apply()
        }

        fun getRememberMe(context: Context): Boolean {
            return getSharedPreference(context).getBoolean(KEY_REMEMBER_ME, false)
        }

        fun setEmail(context: Context, value: String) {
            getSharedPreferenceEditor(context).putString(KEY_EMAIL, value).apply()
        }

        fun getEmail(context: Context): String {
            return getSharedPreference(context).getString(KEY_EMAIL, "").toString()
        }

        fun setPassword(context: Context, value: String) {
            getSharedPreferenceEditor(context).putString(KEY_PASSWORD, value).apply()
        }

        fun getPassword(context: Context): String {
            return getSharedPreference(context).getString(KEY_PASSWORD, "").toString()
        }

        fun setUserId(context: Context, value: Int) {
            getSharedPreferenceEditor(context).putInt(KEY_USER_ID, value).apply()
        }

        fun getUserId(context: Context): Int {
            return getSharedPreference(context).getInt(KEY_USER_ID, -1)
        }

        fun setBranchId(context: Context, value: Int) {
            getSharedPreferenceEditor(context).putInt(KEY_BRANCH_ID, value).apply()
        }

        fun getBranchId(context: Context): Int {
            return getSharedPreference(context).getInt(KEY_BRANCH_ID, -1)
        }

        fun setUserToken(context: Context, value: String) {
            getSharedPreferenceEditor(context).putString(KEY_TOKEN, value).apply()
        }

        fun getUserToken(context: Context): String {
            return getSharedPreference(context).getString(KEY_TOKEN, "").toString()
        }

        fun setUserName(context: Context, value: String) {
            getSharedPreferenceEditor(context).putString(KEY_USER_NAME, value).apply()
        }

        fun getUserName(context: Context): String {
            return getSharedPreference(context).getString(KEY_USER_NAME, "").toString()
        }

        fun setPhone(context: Context, value: String) {
            getSharedPreferenceEditor(context).putString(KEY_PHONE, value).apply()
        }

        fun getPhone(context: Context): String {
            return getSharedPreference(context).getString(KEY_PHONE, "").toString()
        }

        fun setStatus(context: Context, value: String) {
            getSharedPreferenceEditor(context).putString(KEY_STATUS, value).apply()
        }

        fun getStatus(context: Context): String {
            return getSharedPreference(context).getString(KEY_STATUS, "").toString()
        }

    }
}