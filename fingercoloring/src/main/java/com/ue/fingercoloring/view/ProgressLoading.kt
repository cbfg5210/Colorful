package com.ue.fingercoloring.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar


/**
 * Created by Swifty.Wang on 2015/5/29.
 */
class ProgressLoading(context: Context) : Dialog(context) {
    companion object {
        private var dialog: ProgressLoading? = null

        /**
         * press back button can dismiss progressdialog
         *
         * @param context
         * @param cancelable
         */
        fun show(context: Context, cancelable: Boolean? = false) {
            dialog = ProgressLoading(context)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.setCancelable(cancelable!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            val detail_layout = FrameLayout(context)
            val progressBar = ProgressBar(context)
            detail_layout.addView(progressBar)
            dialog!!.setContentView(detail_layout)
            dialog!!.window!!
                    .setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            dialog!!.show()
        }

        fun DismissDialog() {
            if (dialog != null && dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }

        fun setOndismissListener(ondismissListener: DialogInterface.OnDismissListener) {
            dialog!!.setOnDismissListener(ondismissListener)
        }
    }
}