package com.ue.colorful.model

class PaletteColor(val colorSectionName: String, val hex: Int, val baseName: String) {
    val hexString: String

    init {
        this.hexString = String.format("#%06x", 0xFFFFFF and hex)
    }
}