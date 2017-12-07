/*
 * Copyright (C) 2017 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ue.colorful

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import java.util.*

class MultiColorPickerView : FrameLayout {

    var color: Int = 0
        private set
    var selectedPoint: Point? = null
        private set

    private var palette: ImageView? = null
    private var mainSelector: Selector? = null

    private var paletteDrawable: Drawable? = null

    private var selectorList: MutableList<Selector>? = null

    private var mAlpha = 0.5f

    val colorHtml: String
        get() = String.format("%06X", 0xFFFFFF and color)

    // hex to int : R
    // hex to int : G
    // hex to int : B
    val colorRGB: IntArray
        get() {
            val rgb = IntArray(3)
            val color = java.lang.Long.parseLong(String.format("%06X", 0xFFFFFF and this.color), 16).toInt()
            rgb[0] = color shr 16 and 0xFF
            rgb[1] = color shr 8 and 0xFF
            rgb[2] = color shr 0 and 0xFF
            return rgb
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        getAttrs(attrs)
        onCreate()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        getAttrs(attrs)
        onCreate()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
        getAttrs(attrs)
        onCreate()
    }

    private fun init() {
        selectorList = ArrayList()
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                } else {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                onFirstLayout()
            }
        })
    }

    private fun onFirstLayout() {
        selectCenter()
        loadListeners()
    }

    private fun getAttrs(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultiColorPickerView)
        try {
            if (a.hasValue(R.styleable.MultiColorPickerView_palette2))
                paletteDrawable = a.getDrawable(R.styleable.MultiColorPickerView_palette2)
        } finally {
            a.recycle()
        }
    }

    private fun onCreate() {
        setPadding(0, 0, 0, 0)
        palette = ImageView(context)
        if (paletteDrawable != null)
            palette!!.setImageDrawable(paletteDrawable)

        val wheelParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        wheelParams.gravity = Gravity.CENTER
        addView(palette, wheelParams)
    }

    private fun loadListeners() {
        setOnTouchListener { v, event ->
            if (mainSelector != null) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mainSelector!!.selector.isPressed = true
                        onTouchReceived(event)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        mainSelector!!.selector.isPressed = true
                        onTouchReceived(event)
                    }
                    else -> {
                        mainSelector!!.selector.isPressed = false
                        false
                    }
                }
            } else
                false
        }
    }

    private fun onTouchReceived(event: MotionEvent): Boolean {
        val snapPoint = Point(event.x.toInt(), event.y.toInt())
        color = getColorFromBitmap(snapPoint.x.toFloat(), snapPoint.y.toFloat())

        // check validation
        if (color != Color.TRANSPARENT) {
            mainSelector!!.selector.x = (snapPoint.x - mainSelector!!.selector.measuredWidth / 2).toFloat()
            mainSelector!!.selector.y = (snapPoint.y - mainSelector!!.selector.measuredHeight / 2).toFloat()
            selectedPoint = Point(snapPoint.x, snapPoint.y)
            fireColorListener(color)
            return true
        } else
            return false
    }

    private fun getColorFromBitmap(x: Float, y: Float): Int {
        if (paletteDrawable == null) return 0

        val invertMatrix = Matrix()
        palette!!.imageMatrix.invert(invertMatrix)

        val mappedPoints = floatArrayOf(x, y)
        invertMatrix.mapPoints(mappedPoints)

        if (palette!!.drawable != null && palette!!.drawable is BitmapDrawable &&
                mappedPoints[0] > 0 && mappedPoints[1] > 0 &&
                mappedPoints[0] < palette!!.drawable.intrinsicWidth && mappedPoints[1] < palette!!.drawable.intrinsicHeight) {

            invalidate()
            return (palette!!.drawable as BitmapDrawable).bitmap.getPixel(mappedPoints[0].toInt(), mappedPoints[1].toInt())
        }
        return 0
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
    }

    private fun fireColorListener(color: Int) {
        if (mainSelector!!.colorListener != null) {
            mainSelector!!.colorListener.onColorSelected(color)
        }
    }

    fun setSelectorPoint(x: Int, y: Int) {
        if (mainSelector != null) {
            mainSelector!!.selector.x = x.toFloat()
            mainSelector!!.selector.y = y.toFloat()
            selectedPoint = Point(x, y)
            color = getColorFromBitmap(x.toFloat(), y.toFloat())
            fireColorListener(color)
        }
    }

    fun addSelector(drawable: Drawable?, colorListener: ColorListener?) {
        if (drawable == null || colorListener == null) return

        val selectorImage = ImageView(context)
        selectorImage.setImageDrawable(drawable)
        val selector = Selector(selectorImage, colorListener)

        selector.selector.x = (measuredWidth / 2 - selector.selector.width / 2).toFloat()
        selector.selector.y = (measuredHeight / 2 - selector.selector.height / 2).toFloat()

        val thumbParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        thumbParams.gravity = Gravity.CENTER

        selectorImage.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    swapAlpha(selector)
                    mainSelector = selector

                    val invertMatrix = Matrix()
                    palette!!.imageMatrix.invert(invertMatrix)

                    val mappedPoints = floatArrayOf(motionEvent.x, motionEvent.y)
                    invertMatrix.mapPoints(mappedPoints)

                    val event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, motionEvent.x, motionEvent.y, 0)
                    dispatchTouchEvent(event)
                }
            }
            false
        }

        addView(selector.selector, thumbParams)
        swapAlpha(selector)
        selectorList!!.add(selector)
        mainSelector = selector
    }

    private fun swapAlpha(selector: Selector) {
        if (mainSelector != null) {
            mainSelector!!.selector.alpha = 1.0f
            selector.selector.alpha = mAlpha
        }
    }

    fun selectCenter() {
        if (mainSelector != null)
            setSelectorPoint(measuredWidth / 2 - mainSelector!!.selector.width / 2, measuredHeight / 2 - mainSelector!!.selector.height / 2)
    }

    fun setSelectorAlpha(alpha: Float) {
        this.mAlpha = alpha
    }
}
