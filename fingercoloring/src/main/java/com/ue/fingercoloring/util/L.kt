package com.ue.fingercoloring.util

import android.util.Log

/**
 * Created by Swifty.Wang on 2015/8/4.
 */
class L private constructor() {

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        var isDebug = false
        private val TAG = "FillColor"

        fun i(msg: String?) {
            if (isDebug)
                if (msg != null)
                    Log.i(TAG, msg)
        }

        fun d(msg: String?) {
            if (isDebug)
                if (msg != null)
                    Log.d(TAG, msg)
        }

        fun e(msg: String?) {
            if (isDebug)
                if (msg != null)
                    Log.e(TAG, msg)
        }

        fun v(msg: String?) {
            if (isDebug)
                if (msg != null)
                    Log.v(TAG, msg)
        }

        fun i(tag: String, msg: String?) {
            if (isDebug)
                if (msg != null)
                    Log.i(tag, msg)
        }

        fun d(tag: String, msg: String?) {
            if (isDebug)
                if (msg != null)
                    Log.d(tag, msg)
        }

        fun e(tag: String, msg: String?) {
            if (isDebug)
                if (msg != null)
                    Log.e(tag, msg)
        }

        fun v(tag: String, msg: String?) {
            if (isDebug)
                if (msg != null)
                    Log.v(tag, msg)
        }
    }
}