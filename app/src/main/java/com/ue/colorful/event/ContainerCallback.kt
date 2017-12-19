package com.ue.colorful.event

/**
 * Created by hawk on 2017/12/19.
 */
interface ContainerCallback {
    fun copyColor(color: Int)
    fun addPaletteColor(color: Int)
    fun gameOver(gameMode: Int, gameResult: Long)
}