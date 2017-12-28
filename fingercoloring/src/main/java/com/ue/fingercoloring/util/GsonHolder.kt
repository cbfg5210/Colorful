package com.ue.fingercoloring.util

/**
 * Created by hawk on 2017/4/9.
 */

import android.os.Build

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.lang.reflect.Modifier

class GsonHolder private constructor() {

    init {
        throw UnsupportedOperationException()
    }

    companion object {

        val gson: Gson

        init {
            val sdk = Build.VERSION.SDK_INT
            if (sdk >= Build.VERSION_CODES.M) {
                val gsonBuilder = GsonBuilder()
                        .excludeFieldsWithModifiers(
                                Modifier.FINAL,
                                Modifier.TRANSIENT,
                                Modifier.STATIC)
                gson = gsonBuilder.create()
            } else {
                gson = Gson()
            }
        }
    }
}