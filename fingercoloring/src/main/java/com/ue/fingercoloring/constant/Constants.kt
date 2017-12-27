package com.ue.fingercoloring.constant

import java.io.File

/**
 * Created by hawk on 2017/12/26.
 */

object Constants {
    val ASSETS = "file:///android_asset/"
    private val BASE_FOLDER = File.separator + "fingerColoring" + File.separator
    val FOLDER_THEMES = BASE_FOLDER + "themes" + File.separator
    val FOLDER_WORKS = BASE_FOLDER + "works" + File.separator
    val REQ_ADVANCED_PAINT = 900
    val REPAINT_RESULT = 999
    var SHARE_WORK = "share_work"
}
