package com.ue.fingercoloring.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ue.fingercoloring.R
import java.io.File

/**
 * Created by Swifty.Wang on 2015/8/4.
 */
object ShareImageUtil {

    fun shareImg(context: Context, path: String) {
        val file = File(path)
        val uri = Uri.fromFile(file)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_SUBJECT, context!!.getString(R.string.app_name))
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.sharemywork) + context!!.getString(R.string.sharecontent))
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(intent, context!!.getString(R.string.pleaseselect)))
    }
}
