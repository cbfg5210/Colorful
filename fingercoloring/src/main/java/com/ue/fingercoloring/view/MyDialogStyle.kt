package com.ue.fingercoloring.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView

import com.ue.fingercoloring.R
import com.ue.fingercoloring.util.DensityUtil

/**
 * Created by macpro001 on 30/5/15.
 */
open class MyDialogStyle(var context: Context) {
    protected var dialog: Dialog

    init {
        dialog = Dialog(context, POPSTYLE)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

    fun dismissDialog(onCancelListener: DialogInterface.OnDismissListener) {
        if (dialog != null && dialog.isShowing) {
            dialog.setOnDismissListener(onCancelListener)
            dialog.dismiss()
        }
    }

    fun dismissDialog() {
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun reShowDialog() {
        if (!(context as Activity).isFinishing) {
            if (dialog != null && !dialog.isShowing) {
                dialog.show()
            }
        }
    }

    fun showDialog() {
        if (!(context as Activity).isFinishing) {
            dialog.show()
        }
    }

    fun showOneButtonDialog(titlestr: String, Content: CharSequence, btntext1: String, listener1: View.OnClickListener, cancelable: Boolean) {
        dialog.setCancelable(cancelable)
        val detail_layout = View.inflate(context, R.layout.dialog_one_button, null)
        dialog.setContentView(detail_layout)

        val content = detail_layout.findViewById<View>(R.id.content) as TextView
        val title = detail_layout.findViewById<View>(R.id.title) as TextView
        if (title != null) {
            title.text = titlestr
            title.visibility = View.VISIBLE
        } else {
            title.visibility = View.GONE
        }
        content.text = Content
        val button1 = detail_layout.findViewById<View>(R.id.button1) as Button
        button1.text = btntext1
        button1.setOnClickListener(listener1)
        showDialog()
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

    fun showBlankDialog(titlestr: String, btntext1: String, listener1: View.OnClickListener, cancelable: Boolean, view: View) {
        dialog.setCancelable(cancelable)
        val detail_layout = View.inflate(context, R.layout.dialog_blank, null)
        dialog.setContentView(detail_layout)

        val title = detail_layout.findViewById<View>(R.id.title) as TextView
        val frameLayout = detail_layout.findViewById<View>(R.id.customcontent) as FrameLayout
        if (title != null) {
            title.text = titlestr
            title.visibility = View.VISIBLE
        } else {
            title.visibility = View.GONE
        }
        frameLayout.addView(view)
        val button1 = detail_layout.findViewById<View>(R.id.button1) as Button
        button1.text = btntext1
        button1.setOnClickListener(listener1)
        showDialog()
    }

    fun showBlankDialog(titlestr: String, view: View) {
        val listener = View.OnClickListener { dismissDialog() }
        showBlankDialog(titlestr, context.getString(R.string.ok), listener, true, view)
    }

    fun showBlankDialog(titlestr: String, view: View, listener: View.OnClickListener) {
        showBlankDialog(titlestr, context.getString(R.string.ok), listener, true, view)
    }


    protected fun showTwoImageDialog(buffer: StringBuffer, resId1: Drawable, s1: String, resId2: Drawable, s2: String, listener1: View.OnClickListener, listener2: View.OnClickListener, b: Boolean) {
        dialog.setCancelable(b)
        val detail_layout = View.inflate(
                context,
                R.layout.dialog_two_button, null)
        dialog.setContentView(detail_layout)
        val content = detail_layout.findViewById<View>(R.id.content) as TextView
        content.text = buffer
        val button1 = detail_layout.findViewById<View>(R.id.button1) as Button
        val button2 = detail_layout.findViewById<View>(R.id.button2) as Button
        resId1.setBounds(0, 0, DensityUtil.dip2px(context, 40f), DensityUtil.dip2px(context, 40f))
        resId2.setBounds(0, 0, DensityUtil.dip2px(context, 40f), DensityUtil.dip2px(context, 40f))
        button1.setCompoundDrawables(null, resId1, null, null)
        button2.setCompoundDrawables(null, resId2, null, null)
        button1.text = s1
        button1.setTextColor(context.resources.getColor(R.color.maincolor))
        button2.text = s2
        button2.setTextColor(context.resources.getColor(R.color.maincolor))
        button1.setOnClickListener(listener1)
        button2.setOnClickListener(listener2)
        showDialog()
    }

    companion object {
        val POPSTYLE = R.style.MyDialogPop
    }
}
