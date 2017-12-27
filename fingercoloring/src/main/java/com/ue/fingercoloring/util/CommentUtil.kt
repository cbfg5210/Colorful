package com.ue.fingercoloring.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

import com.ue.fingercoloring.R
import com.ue.fingercoloring.constant.SPKeys


/**
 * Created by Swifty.Wang on 2015/7/3.
 */
object CommentUtil {
    fun commentApp(context: Context) {
        try {
            val mAddress = "market://details?id=" + context.packageName
            val marketIntent = Intent("android.intent.action.VIEW")
            marketIntent.data = Uri.parse(mAddress)
            context.startActivity(marketIntent)
            SPUtils.putBoolean(SPKeys.CommentEnableKey, false)
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.commentFailed), Toast.LENGTH_SHORT).show()
        }

    }
}
