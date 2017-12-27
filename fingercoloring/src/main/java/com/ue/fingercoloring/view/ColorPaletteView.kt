package com.ue.fingercoloring.view

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import android.view.View

class ColorPaletteView : View {
    private var colorWheelPaint: Paint
    private var colorWheelBitmap: Bitmap? = null
    private var colorWheelRadius: Int = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    init {
        colorWheelPaint = Paint()
        colorWheelPaint.isAntiAlias = true
        colorWheelPaint.isDither = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val size = Math.min(widthSize, heightSize)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        val centerX = width / 2
        val centerY = height / 2

        canvas.drawBitmap(colorWheelBitmap!!, (centerX - colorWheelRadius).toFloat(), (centerY - colorWheelRadius).toFloat(), null)
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        colorWheelRadius = width / 2
        colorWheelBitmap = createColorWheelBitmap(colorWheelRadius * 2, colorWheelRadius * 2)
    }

    private fun createColorWheelBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888)

        val colorCount = 12
        val colorAngleStep = 360 / 12
        val colors = IntArray(colorCount + 1)
        val hsv = floatArrayOf(0f, 1f, 1f)
        for (i in colors.indices) {
            hsv[0] = ((i * colorAngleStep + 180) % 360).toFloat()
            colors[i] = Color.HSVToColor(hsv)
        }
        colors[colorCount] = colors[0]

        val sweepGradient = SweepGradient((width / 2).toFloat(), (height / 2).toFloat(), colors, null)
        val radialGradient = RadialGradient((width / 2).toFloat(), (height / 2).toFloat(), colorWheelRadius.toFloat(), -0x1, 0x00FFFFFF, TileMode.CLAMP)
        val composeShader = ComposeShader(sweepGradient, radialGradient, PorterDuff.Mode.SRC_OVER)

        colorWheelPaint.shader = composeShader

        val canvas = Canvas(bitmap)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), colorWheelRadius.toFloat(), colorWheelPaint)

        return bitmap
    }
}