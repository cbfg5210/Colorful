package com.ue.colorful.model

/**
 * Created by hawk on 2017/12/21.
 */
class ColorItem(private val colorInt: Int) {
    val hex: String

    init {
        this.hex = String.format("#%06x", 0xFFFFFF and colorInt)
    }
}