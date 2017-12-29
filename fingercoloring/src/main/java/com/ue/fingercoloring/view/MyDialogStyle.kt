package com.ue.fingercoloring.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.ue.fingercoloring.R

/**
 * Created by macpro001 on 30/5/15.
 */
open class MyDialogStyle(var context: Context) {
    private var dialog: Dialog

    init {
        dialog = Dialog(context, POPSTYLE)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

    fun dismissDialog() {
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun showDialog() {
        if (!(context as Activity).isFinishing) {
            dialog.show()
        }
    }

    fun showTwoButtonDialog(Content: CharSequence, btntext1: String, btntext2: String, listener1: View.OnClickListener, listener2: View.OnClickListener, cancelable: Boolean) {
        dialog.setCancelable(cancelable)
        val detail_layout = View.inflate(context, R.layout.dialog_two_button, null)
        dialog.setContentView(detail_layout)
        val content = detail_layout.findViewById<View>(R.id.content) as TextView
        content.text = Content
        val button1 = detail_layout.findViewById<View>(R.id.button1) as Button
        val button2 = detail_layout.findViewById<View>(R.id.button2) as Button
        button1.text = btntext1
        button2.text = btntext2
        button1.setOnClickListener(listener1)
        button2.setOnClickListener(listener2)
        showDialog()
    }

    companion object {
        val POPSTYLE = R.style.MyDialogPop
    }
}
