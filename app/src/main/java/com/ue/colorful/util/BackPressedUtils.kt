package com.ue.colorful.util

import android.app.Activity
import android.widget.Toast

/**
 * Created by xin on 4/14/17.
 */

object BackPressedUtils {
    private var lastBackPressedTime: Long = 0

    fun exitIfBackTwice(activity: Activity, toast: String) {
        val currentTime = System.currentTimeMillis()
        val timeD = currentTime - lastBackPressedTime
        if (timeD >= 3000) {
            lastBackPressedTime = currentTime
            Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
            return
        }
        activity.finish()
    }
}
