package app.rtcgarmentex.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import app.rtcgarmentex.ui.activities.BaseActivity
import com.google.android.material.snackbar.Snackbar


class ToastHelper {

    companion object {
        fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
            (context as BaseActivity).mToast?.cancel()
            context.mToast = Toast.makeText(context, message, duration)
            context.mToast!!.show()
        }

        fun dismiss(context: Context) {
            (context as BaseActivity).mToast?.cancel()
        }

        fun showSnackBar(parentLayout: View?, msg: String?) {
            //Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            val snackBar = Snackbar.make(parentLayout!!, msg!!, Snackbar.LENGTH_SHORT)
            snackBar.setActionTextColor(Color.WHITE)
            val view = snackBar.view
            val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            tv.setTextColor(Color.WHITE)
            /* snackBar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });*/
            snackBar.show()
        }
    }
}