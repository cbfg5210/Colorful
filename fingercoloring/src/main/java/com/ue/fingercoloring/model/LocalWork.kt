package com.ue.fingercoloring.model

/**
 * Created by Swifty.Wang on 2015/9/1.
 */
class LocalWork(val imageName: String, val imageUrl: String, var lastModDate: String, var lastModTimeStamp: Long, wvHRadio: Float) {
    var wvHRadio: Float = 0.toFloat()
        private set

    init {
        this.wvHRadio = wvHRadio
    }
}
