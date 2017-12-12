package com.ue.colorful.feature.game

import android.content.Context
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View

import java.text.DecimalFormat

class Chronometer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : AppCompatTextView(context, attrs, defStyle) {

    private var mBase: Long = 0
    private var mVisible: Boolean = false
    private var mStarted: Boolean = false

    private var mRunning: Boolean = false
    var timeElapsed: Long = 0
        private set

    private val mHandler = object : Handler() {
        override fun handleMessage(m: Message) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime())
                sendMessageDelayed(Message.obtain(this, TICK_WHAT),
                        100)
            }
        }
    }

    init {

        init()
    }

    private fun init() {
        mBase = SystemClock.elapsedRealtime()
        updateText(mBase)
    }

    fun start() {
        mBase = SystemClock.elapsedRealtime()
        mStarted = true
        updateRunning()
    }

    fun stop() {
        mStarted = false
        updateRunning()
    }

    fun resume() {
        mStarted = true
        updateRunning()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mVisible = false
        updateRunning()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        mVisible = visibility == View.VISIBLE
        updateRunning()
    }

    @Synchronized private fun updateText(now: Long) {
        timeElapsed = now - mBase

        val df = DecimalFormat("00")
        val df2 = DecimalFormat("0")

        val hours = (timeElapsed / (3600 * 1000)).toInt()
        var remaining = (timeElapsed % (3600 * 1000)).toInt()

        val minutes = (remaining / (60 * 1000)).toInt()
        remaining = (remaining % (60 * 1000)).toInt()

        val seconds = (remaining / 1000).toInt()

        val milliseconds = (timeElapsed.toInt() % 1000 / 100).toInt()

        var text = ""

        if (hours > 0) {
            text += df.format(hours.toLong()) + ":"
        }

        text += df.format(minutes.toLong()) + ":"
        text += df.format(seconds.toLong()) + "."
        text += df2.format(milliseconds.toLong())

        setText(text)
    }

    private fun updateRunning() {
        val running = mVisible && mStarted
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime())
                mHandler.sendMessageDelayed(Message.obtain(mHandler,
                        TICK_WHAT), 100)
            } else {
                mHandler.removeMessages(TICK_WHAT)
            }
            mRunning = running
        }
    }

    companion object {
        private val TICK_WHAT = 2
    }

}