package com.ue.colorful.util

import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by hawk on 2017/12/22.
 */
class SnackBarUtils {
    companion object {
        fun setView(snackBar: Snackbar, layoutId: Int) {
            val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
            snackBarLayout.removeAllViews()
            val viewToAdd = LayoutInflater.from(snackBar.view.context).inflate(layoutId, null)
            val p = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            snackBarLayout.addView(viewToAdd, p)
        }

        fun setAction(snackBar: Snackbar, btnId: Int, onClickListener: View.OnClickListener) {
            snackBar.view?.findViewById<View>(btnId)?.setOnClickListener(onClickListener)
        }

        fun setMessage(snackBar: Snackbar, msgTVId: Int, msg: String) {
            snackBar.view?.findViewById<TextView>(msgTVId)?.text = msg
        }

        fun setColor(snackBar: Snackbar, colorViewId: Int, color: Int) {
            snackBar.view?.findViewById<View>(colorViewId)?.setBackgroundColor(color)
        }
    }
}