package com.ue.colorful

import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView


class ColorPickerView : FrameLayout {

    var color: Int = 0
        private set//设值方法的可见度为 private, 并使用默认实现

    private var palette: ImageView? = null
    private var selector: ImageView? = null

    private var paletteDrawable: Drawable? = null
    private var thumbSize: Float = 0F;
    private var thumbColor: Int = 0

    private var mColorListener: ColorListener? = null

    val colorHtml: String
        get() = String.format("%06X", 0xFFFFFF and color)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context!!.obtainStyledAttributes(attrs, R.styleable.ColorPickerView)

        paletteDrawable = a.getDrawable(R.styleable.ColorPickerView_palette)
        thumbColor = a.getColor(R.styleable.ColorPickerView_thumbColor, Color.BLACK)
        thumbSize = a.getDimension(R.styleable.ColorPickerView_thumbSize, 30F)

        a.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        palette = ImageView(context)
        palette!!.adjustViewBounds = true
        palette!!.setBackgroundColor(Color.WHITE)
        val paletteParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        paletteParams.gravity = Gravity.CENTER
        addView(palette, paletteParams)
        setPaletteDrawable(paletteDrawable)

        selector = ImageView(context)
        val thumbParams = FrameLayout.LayoutParams(thumbSize.toInt(), thumbSize.toInt())
        thumbParams.gravity = Gravity.CENTER
        addView(selector, thumbParams)
        toggleThumbColor()

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) viewTreeObserver.removeGlobalOnLayoutListener(this)
                else viewTreeObserver.removeOnGlobalLayoutListener(this)

                onTouchReceived((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat())
            }
        })

        setOnTouchListener({ _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_MOVE -> onTouchReceived(event.x, event.y)
            }
            true
        })
    }

    fun offsetXY(offsetX: Float, offsetY: Float) {
        selector!!.x += offsetX
        selector!!.y += offsetY

        color = getColorFromBitmap(selector!!.x - thumbSize / 2, selector!!.y - thumbSize / 2)
        mColorListener?.onColorSelected(color)
    }

    private fun onTouchReceived(mX: Float, mY: Float): Boolean {
        selector!!.x = mX - thumbSize / 2
        selector!!.y = mY - thumbSize / 2

        color = getColorFromBitmap(mX, mY)
        mColorListener?.onColorSelected(color)

        return true
    }

    private fun getColorFromBitmap(x: Float, y: Float): Int {
        if (paletteDrawable == null) Color.WHITE

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
        return Color.WHITE
    }

    fun setColorListener(colorListener: ColorListener) {
        mColorListener = colorListener
    }

    fun setPaletteDrawable(drawable: Drawable?) {
        paletteDrawable = drawable
        palette?.setImageDrawable(paletteDrawable)
    }

    fun toggleThumbColor() {
        thumbColor = if (thumbColor == Color.WHITE) Color.BLACK else Color.WHITE

        if (selector?.drawable == null) {
            val shapeRing = GradientDrawable()
            shapeRing.shape = GradientDrawable.OVAL
            shapeRing.setStroke((thumbSize * 0.2F).toInt(), thumbColor)
            selector?.setImageDrawable(shapeRing)
        } else {
            (selector?.drawable as GradientDrawable).setStroke((thumbSize * 0.2F).toInt(), thumbColor)
        }
    }
}
