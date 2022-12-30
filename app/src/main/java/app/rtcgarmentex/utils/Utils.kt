package app.rtcgarmentex.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import org.json.JSONObject
import retrofit2.Response


public class Utils {
    companion object {
        public fun getScreenWidth(context: Activity): Int {
            val displayMetrics = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        public fun getScreenHeight(context: Activity): Int {
            val displayMetrics = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }

        public fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun showKeyboard(view: View) {
            view.requestFocus()
            if (!isHardKeyboardAvailable(view)) {
                val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(view, 0)
            }
        }

        private fun isHardKeyboardAvailable(view: View): Boolean {
            return view.context.resources.configuration.keyboard != Configuration.KEYBOARD_NOKEYS
        }

        fun hideKeyboard(activity: Activity) {
            try {
                val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (activity.currentFocus != null && activity.currentFocus!!.windowToken != null)
                    inputManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun hideKeyboards(context: Context?) {
            try {
                val inputManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    (context as Activity).currentFocus!!.windowToken,
                    0
                )
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
        }

        fun hideKeyboard(view: View) {
            try {
                val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (view.windowToken != null)
                    inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun errorMessage(response: Response<*>, context: Context) {
            if (response.errorBody() != null) {
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                if (jsonObj.has("errors"))
                    ToastHelper.showToast(context, jsonObj.getString("errors"))
                else
                    ToastHelper.showToast(context, "No record found")
            } else {
                ToastHelper.showToast(context, "Failed")
            }
        }

    }
}