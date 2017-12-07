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

import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView

class ColorPickerView : FrameLayout {

    var color: Int = 0
        private set//设值方法的可见度为 private, 并使用默认实现
    private var selectedPoint: Point? = null

    private var palette: ImageView? = null
    private var selector: ImageView? = null

    private var paletteDrawable: Drawable? = null
    private var selectorDrawable: Drawable? = null

    private var mColorListener: ColorListener? = null

    private var ACTON_UP = false

    val colorHtml: String
        get() = String.format("%06X", 0xFFFFFF and color)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        val a = context!!.obtainStyledAttributes(attrs, R.styleable.ColorPickerView)
        paletteDrawable = a.getDrawable(R.styleable.ColorPickerView_palette)

        if (a.hasValue(R.styleable.ColorPickerView_selector)) selectorDrawable = a.getDrawable(R.styleable.ColorPickerView_selector)
        else selectorDrawable = ContextCompat.getDrawable(getContext(), R.drawable.wheel)

        a.recycle()

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) viewTreeObserver.removeGlobalOnLayoutListener(this)
                else viewTreeObserver.removeOnGlobalLayoutListener(this)

                onFirstLayout()
            }
        })

        setPadding(0, 0, 0, 0)
        palette = ImageView(context)
        if (paletteDrawable != null) {
            palette!!.setImageDrawable(paletteDrawable)
        }

        val wheelParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        wheelParams.gravity = Gravity.CENTER
        addView(palette, wheelParams)

        selector = ImageView(context)
        if (selectorDrawable != null) {
            selector!!.setImageDrawable(selectorDrawable)

            val thumbParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            thumbParams.gravity = Gravity.CENTER
            addView(selector, thumbParams)
        }
    }

    private fun onFirstLayout() {
        val x=measuredWidth / 2 - selector!!.width / 2
        val y=measuredHeight / 2 - selector!!.height / 2

        selector!!.x = x.toFloat()
        selector!!.y = y.toFloat()
        selectedPoint = Point(x, y)
        color = getColorFromBitmap(x.toFloat(), y.toFloat())
        mColorListener?.onColorSelected(color)

        setOnTouchListener(OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> if (ACTON_UP) {
                    selector!!.isPressed = true
                    return@OnTouchListener onTouchReceived(event)
                }
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_MOVE -> if (!ACTON_UP) {
                    selector!!.isPressed = true
                    return@OnTouchListener onTouchReceived(event)
                }
                else -> {
                    selector!!.isPressed = false
                    return@OnTouchListener false
                }
            }
            true
        })
    }

    private fun onTouchReceived(event: MotionEvent): Boolean {
        val snapPoint = Point(event.x.toInt(), event.y.toInt())
        color = getColorFromBitmap(snapPoint.x.toFloat(), snapPoint.y.toFloat())

        if (color == Color.TRANSPARENT) false

        selector!!.x = (snapPoint.x - selector!!.measuredWidth / 2).toFloat()
        selector!!.y = (snapPoint.y - selector!!.measuredHeight / 2).toFloat()
        selectedPoint = Point(snapPoint.x, snapPoint.y)
        mColorListener?.onColorSelected(color)

        return true
    }

    private fun getColorFromBitmap(x: Float, y: Float): Int {
        if (paletteDrawable == null) 0

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

    fun setColorListener(colorListener: ColorListener) {
        mColorListener = colorListener
    }

    fun setPaletteDrawable(drawable: Drawable) {
        removeView(palette)
        palette = ImageView(context)
        paletteDrawable = drawable
        palette!!.setImageDrawable(paletteDrawable)
        addView(palette)

        removeView(selector)
        addView(selector)

        selector!!.x = (measuredWidth / 2 - selector!!.width / 2).toFloat()
        selector!!.y = (measuredHeight / 2 - selector!!.height / 2).toFloat()
    }

    fun setSelectorDrawable(drawable: Drawable) {
        selector!!.setImageDrawable(drawable)
    }
}
