package com.ue.colorful.widget

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config
import android.graphics.Paint.Style
import android.graphics.Shader.TileMode
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.ue.colorful.R

/**
 * Displays a color picker to the user and allow them to select a color. A slider for the alpha channel is also available.
 * Enable it by setting setAlphaSliderVisible(boolean) to true.
 */
class PaletteColorPickerView : View {

    /**
     * The width in px of the hue panel.
     */
    private val huePanelWidthPx: Int
    /**
     * The height in px of the alpha panel
     */
    private val alphaPanelHeightPx: Int
    /**
     * The distance in px between the different
     * color panels.
     */
    private val panelSpacingPx: Int
    /**
     * The radius in px of the color palette tracker circle.
     */
    private val circleTrackerRadiusPx: Int
    /**
     * The px which the tracker of the hue or alpha panel
     * will extend outside of its bounds.
     */
    private val sliderTrackerOffsetPx: Int
    /**
     * Height of slider tracker on hue panel,
     * width of slider on alpha panel.
     */
    private val sliderTrackerSizePx: Int

    private lateinit var satValPaint: Paint
    private lateinit var satValTrackerPaint: Paint

    private lateinit var alphaPaint: Paint
    private lateinit var hueAlphaTrackerPaint: Paint

    private lateinit var borderPaint: Paint

    private var valShader: Shader? = null
    private var satShader: Shader? = null
    private var alphaShader: Shader? = null

    /*
     * We cache a bitmap of the sat/value panel which is expensive to draw each time.
     * We can reuse it when the user is sliding the circle picker as long as the hue isn't changed.
     */
    private var satValBackgroundCache: BitmapCache? = null
    /* We cache the hue background to since its also very expensive now. */
    private var hueBackgroundCache: BitmapCache? = null

    /* Current values */
    private var alpha = 0xff
    private var hue = 360f
    private var sat = 0f
    private var value = 0f

    private val borderColor = -0x424243

    /**
     * Minimum required padding. The offset from the
     * edge we must have or else the finger tracker will
     * get clipped when it's drawn outside of the view.
     */
    private val mRequiredPadding: Int

    /**
     * The Rect in which we are allowed to draw.
     * Trackers can extend outside slightly,
     * due to the required padding we have set.
     */
    private var drawingRect: Rect? = null

    private var satValRect: Rect? = null
    private var hueRect: Rect? = null
    private var alphaRect: Rect? = null

    private var startTouchPoint: Point? = null

    private var alphaPatternDrawable: AlphaPatternDrawable? = null
    private var onColorChangedListener: OnColorChangedListener? = null

    /**
     * Get the current color this view is showing.
     *
     * @return the current color.
     */
    /**
     * Set the color the view should show.
     *
     * @param color The color that should be selected. #argb
     */
    var color: Int
        get() = Color.HSVToColor(alpha, floatArrayOf(hue, sat, value))
        set(color) = setColor(color, false)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        huePanelWidthPx = dpToPx(context, HUE_PANEL_WDITH_DP.toFloat())
        alphaPanelHeightPx = dpToPx(context, ALPHA_PANEL_HEIGH_DP.toFloat())
        panelSpacingPx = dpToPx(context, PANEL_SPACING_DP.toFloat())
        circleTrackerRadiusPx = dpToPx(context, CIRCLE_TRACKER_RADIUS_DP.toFloat())
        sliderTrackerSizePx = dpToPx(context, SLIDER_TRACKER_SIZE_DP.toFloat())
        sliderTrackerOffsetPx = dpToPx(context, SLIDER_TRACKER_OFFSET_DP.toFloat())

        mRequiredPadding = resources.getDimensionPixelSize(R.dimen.cpv_required_padding)

        initPaintTools()

        //Needed for receiving trackball motion events.
        isFocusable = true
        isFocusableInTouchMode = true
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val state = Bundle()
        state.putParcelable("instanceState", super.onSaveInstanceState())
        state.putInt("alpha", alpha)
        state.putFloat("hue", hue)
        state.putFloat("sat", sat)
        state.putFloat("value", value)

        return state
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            alpha = state.getInt("alpha")
            hue = state.getFloat("hue")
            sat = state.getFloat("sat")
            value = state.getFloat("value")
            super.onRestoreInstanceState(state.getParcelable("instanceState"))

            return
        }
        super.onRestoreInstanceState(state)
    }

    private fun initPaintTools() {
        satValPaint = Paint()
        satValTrackerPaint = Paint()
        hueAlphaTrackerPaint = Paint()
        alphaPaint = Paint()
        borderPaint = Paint()

        satValTrackerPaint.style = Style.STROKE
        satValTrackerPaint.strokeWidth = dpToPx(context, 2f).toFloat()
        satValTrackerPaint.isAntiAlias = true

        hueAlphaTrackerPaint.color = -0x1000000
        hueAlphaTrackerPaint.style = Style.STROKE
        hueAlphaTrackerPaint.strokeWidth = dpToPx(context, 2f).toFloat()
        hueAlphaTrackerPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        if (drawingRect!!.width() <= 0 || drawingRect!!.height() <= 0) {
            return
        }

        drawSatValPanel(canvas)
        drawHuePanel(canvas)
        drawAlphaPanel(canvas)
    }

    private fun drawSatValPanel(canvas: Canvas) {
        val rect = satValRect

        if (BORDER_WIDTH_PX > 0) {
            borderPaint.color = borderColor
            canvas.drawRect(drawingRect!!.left.toFloat(), drawingRect!!.top.toFloat(),
                    (rect!!.right + BORDER_WIDTH_PX).toFloat(),
                    (rect.bottom + BORDER_WIDTH_PX).toFloat(), borderPaint)
        }

        //Black gradient has either not been created or the view has been resized.
        valShader = valShader ?: LinearGradient(rect!!.left.toFloat(), rect.top.toFloat(), rect.left.toFloat(), rect.bottom.toFloat(), -0x1, -0x1000000, TileMode.CLAMP)

        //If the hue has changed we need to recreate the cache.
        if (satValBackgroundCache == null || satValBackgroundCache!!.value != hue) {

            satValBackgroundCache = satValBackgroundCache ?: BitmapCache()

            //We create our bitmap in the cache if it doesn't exist.
            satValBackgroundCache!!.bitmap = satValBackgroundCache!!.bitmap ?: Bitmap.createBitmap(rect!!.width(), rect.height(), Config.ARGB_8888)

            //We create the canvas once so we can draw on our bitmap and the hold on to it.
            satValBackgroundCache!!.canvas = satValBackgroundCache!!.canvas ?: Canvas(satValBackgroundCache!!.bitmap!!)

            val rgb = Color.HSVToColor(floatArrayOf(hue, 1f, 1f))

            satShader = LinearGradient(rect!!.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.top.toFloat(), -0x1, rgb, TileMode.CLAMP)

            val mShader = ComposeShader(valShader!!, satShader!!, PorterDuff.Mode.MULTIPLY)
            satValPaint.shader = mShader

            // Finally we draw on our canvas, the result will be
            // stored in our bitmap which is already in the cache.
            // Since this is drawn on a canvas not rendered on
            // screen it will automatically not be using the
            // hardware acceleration. And this was the code that
            // wasn't supported by hardware acceleration which mean
            // there is no need to turn it of anymore. The rest of
            // the view will still be hw accelerated.
            satValBackgroundCache!!.canvas!!.drawRect(0f, 0f,
                    satValBackgroundCache!!.bitmap!!.width.toFloat(),
                    satValBackgroundCache!!.bitmap!!.height.toFloat(),
                    satValPaint)

            //We set the hue value in our cache to which hue it was drawn with,
            //then we know that if it hasn't changed we can reuse our cached bitmap.
            satValBackgroundCache!!.value = hue

        }

        // We draw our bitmap from the cached, if the hue has changed
        // then it was just recreated otherwise the old one will be used.
        canvas.drawBitmap(satValBackgroundCache!!.bitmap!!, null, rect!!, null)

        val p = satValToPoint(sat, value)

        satValTrackerPaint.color = -0x1000000
        canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), (circleTrackerRadiusPx - dpToPx(context, 1f)).toFloat(), satValTrackerPaint)

        satValTrackerPaint.color = -0x222223
        canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), circleTrackerRadiusPx.toFloat(), satValTrackerPaint)

    }

    private fun drawHuePanel(canvas: Canvas) {
        val rect = hueRect

        if (BORDER_WIDTH_PX > 0) {
            borderPaint.color = borderColor

            canvas.drawRect((rect!!.left - BORDER_WIDTH_PX).toFloat(),
                    (rect.top - BORDER_WIDTH_PX).toFloat(),
                    (rect.right + BORDER_WIDTH_PX).toFloat(),
                    (rect.bottom + BORDER_WIDTH_PX).toFloat(),
                    borderPaint)
        }

        if (hueBackgroundCache == null) {
            hueBackgroundCache = BitmapCache()
            hueBackgroundCache!!.bitmap = Bitmap.createBitmap(rect!!.width(), rect.height(), Config.ARGB_8888)
            hueBackgroundCache!!.canvas = Canvas(hueBackgroundCache!!.bitmap!!)

            val hueColors = IntArray((rect.height() + 0.5f).toInt())

            // Generate array of all colors, will be drawn as individual lines.
            var h = 360f
            for (i in hueColors.indices) {
                hueColors[i] = Color.HSVToColor(floatArrayOf(h, 1f, 1f))
                h -= 360f / hueColors.size
            }

            // Time to draw the hue color gradient,
            // its drawn as individual lines which
            // will be quite many when the resolution is high
            // and/or the panel is large.
            val linePaint = Paint()
            linePaint.strokeWidth = 0f
            for (i in hueColors.indices) {
                linePaint.color = hueColors[i]
                hueBackgroundCache!!.canvas!!.drawLine(0f, i.toFloat(), hueBackgroundCache!!.bitmap!!.width.toFloat(), i.toFloat(), linePaint)
            }
        }

        canvas.drawBitmap(hueBackgroundCache!!.bitmap!!, null, rect!!, null)

        val p = hueToPoint(hue)

        val r = RectF()
        r.left = (rect.left - sliderTrackerOffsetPx).toFloat()
        r.right = (rect.right + sliderTrackerOffsetPx).toFloat()
        r.top = (p.y - sliderTrackerSizePx / 2).toFloat()
        r.bottom = (p.y + sliderTrackerSizePx / 2).toFloat()

        canvas.drawRoundRect(r, 2f, 2f, hueAlphaTrackerPaint)
    }

    private fun drawAlphaPanel(canvas: Canvas) {
        /*
     * Will be drawn with hw acceleration, very fast.
		 * Also the AlphaPatternDrawable is backed by a bitmap
		 * generated only once if the size does not change.
		 */
        val rect = alphaRect

        if (BORDER_WIDTH_PX > 0) {
            borderPaint.color = borderColor
            canvas.drawRect((rect!!.left - BORDER_WIDTH_PX).toFloat(),
                    (rect.top - BORDER_WIDTH_PX).toFloat(),
                    (rect.right + BORDER_WIDTH_PX).toFloat(),
                    (rect.bottom + BORDER_WIDTH_PX).toFloat(),
                    borderPaint)
        }

        alphaPatternDrawable!!.draw(canvas)

        val hsv = floatArrayOf(hue, sat, value)
        val color = Color.HSVToColor(hsv)
        val acolor = Color.HSVToColor(0, hsv)

        alphaShader = LinearGradient(rect!!.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.top.toFloat(),
                color, acolor, TileMode.CLAMP)

        alphaPaint.shader = alphaShader

        canvas.drawRect(rect, alphaPaint)

        val p = alphaToPoint(alpha)

        val r = RectF()
        r.left = (p.x - sliderTrackerSizePx / 2).toFloat()
        r.right = (p.x + sliderTrackerSizePx / 2).toFloat()
        r.top = (rect.top - sliderTrackerOffsetPx).toFloat()
        r.bottom = (rect.bottom + sliderTrackerOffsetPx).toFloat()

        canvas.drawRoundRect(r, 2f, 2f, hueAlphaTrackerPaint)
    }

    private fun hueToPoint(hue: Float): Point {

        val rect = hueRect
        val height = rect!!.height().toFloat()

        val p = Point()

        p.y = (height - hue * height / 360f + rect.top).toInt()
        p.x = rect.left

        return p
    }

    private fun satValToPoint(sat: Float, value: Float): Point {

        val rect = satValRect
        val height = rect!!.height().toFloat()
        val width = rect.width().toFloat()

        val p = Point()

        p.x = (sat * width + rect.left).toInt()
        p.y = ((1f - value) * height + rect.top).toInt()

        return p
    }

    private fun alphaToPoint(alpha: Int): Point {
        val rect = alphaRect
        val width = rect!!.width().toFloat()

        val p = Point()

        p.x = (width - alpha * width / 0xff + rect.left).toInt()
        p.y = rect.top

        return p
    }

    private fun pointToSatVal(x: Float, y: Float): FloatArray {
        var x = x
        var y = y

        val rect = satValRect
        val result = FloatArray(2)

        val width = rect!!.width().toFloat()
        val height = rect.height().toFloat()

        if (x < rect.left) {
            x = 0f
        } else if (x > rect.right) {
            x = width
        } else {
            x = x - rect.left
        }

        if (y < rect.top) {
            y = 0f
        } else if (y > rect.bottom) {
            y = height
        } else {
            y = y - rect.top
        }

        result[0] = 1f / width * x
        result[1] = 1f - 1f / height * y

        return result
    }

    private fun pointToHue(y: Float): Float {
        var y = y

        val rect = hueRect

        val height = rect!!.height().toFloat()

        if (y < rect.top) {
            y = 0f
        } else if (y > rect.bottom) {
            y = height
        } else {
            y = y - rect.top
        }

        return 360f - y * 360f / height
    }

    private fun pointToAlpha(x: Int): Int {
        var x = x

        val rect = alphaRect
        val width = rect!!.width()

        if (x < rect.left) {
            x = 0
        } else if (x > rect.right) {
            x = width
        } else {
            x = x - rect.left
        }

        return 0xff - x * 0xff / width

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var update = false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTouchPoint = Point(event.x.toInt(), event.y.toInt())
                update = moveTrackersIfNeeded(event)
            }
            MotionEvent.ACTION_MOVE -> update = moveTrackersIfNeeded(event)
            MotionEvent.ACTION_UP -> {
                startTouchPoint = null
                update = moveTrackersIfNeeded(event)
            }
        }

        if (update) {
            onColorChangedListener?.onColorChanged(Color.HSVToColor(alpha, floatArrayOf(hue, sat, value)))
            invalidate()
            return true
        }

        return super.onTouchEvent(event)
    }

    private fun moveTrackersIfNeeded(event: MotionEvent): Boolean {
        if (startTouchPoint == null) return false

        var update = false

        val startX = startTouchPoint!!.x
        val startY = startTouchPoint!!.y

        if (hueRect!!.contains(startX, startY)) {
            hue = pointToHue(event.y)

            update = true
        } else if (satValRect!!.contains(startX, startY)) {
            val result = pointToSatVal(event.x, event.y)

            sat = result[0]
            value = result[1]

            update = true
        } else if (alphaRect != null && alphaRect!!.contains(startX, startY)) {
            alpha = pointToAlpha(event.x.toInt())

            update = true
        }

        return update
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val finalWidth: Int
        val finalHeight: Int

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val widthAllowed = View.MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val heightAllowed = View.MeasureSpec.getSize(heightMeasureSpec) - paddingBottom - paddingTop

        if (widthMode == View.MeasureSpec.EXACTLY || heightMode == View.MeasureSpec.EXACTLY) {
            //A exact value has been set in either direction, we need to stay within this size.

            if (widthMode == View.MeasureSpec.EXACTLY && heightMode != View.MeasureSpec.EXACTLY) {
                //The with has been specified exactly, we need to adopt the height to fit.
                var h = widthAllowed - panelSpacingPx - huePanelWidthPx
                h += panelSpacingPx + alphaPanelHeightPx

                if (h > heightAllowed) {
                    //We can't fit the view in this container, set the size to whatever was allowed.
                    finalHeight = heightAllowed
                } else {
                    finalHeight = h
                }

                finalWidth = widthAllowed

            } else if (heightMode == View.MeasureSpec.EXACTLY && widthMode != View.MeasureSpec.EXACTLY) {
                //The height has been specified exactly, we need to stay within this height and adopt the width.

                var w = heightAllowed + panelSpacingPx + huePanelWidthPx
                w -= panelSpacingPx + alphaPanelHeightPx

                if (w > widthAllowed) {
                    //we can't fit within this container, set the size to whatever was allowed.
                    finalWidth = widthAllowed
                } else {
                    finalWidth = w
                }

                finalHeight = heightAllowed

            } else {
                //If we get here the dev has set the width and height to exact sizes. For example match_parent or 300dp.
                //This will mean that the sat/value panel will not be square but it doesn't matter. It will work anyway.
                //In all other senarios our goal is to make that panel square.

                //We set the sizes to exactly what we were told.
                finalWidth = widthAllowed
                finalHeight = heightAllowed
            }
        } else {
            //If no exact size has been set we try to make our view as big as possible
            //within the allowed space.

            //Calculate the needed width to layout using max allowed height.
            var widthNeeded = heightAllowed + panelSpacingPx + huePanelWidthPx

            //Calculate the needed height to layout using max allowed width.
            var heightNeeded = widthAllowed - panelSpacingPx - huePanelWidthPx

            widthNeeded -= panelSpacingPx + alphaPanelHeightPx
            heightNeeded += panelSpacingPx + alphaPanelHeightPx

            var widthOk = false
            var heightOk = false

            if (widthNeeded <= widthAllowed) {
                widthOk = true
            }

            if (heightNeeded <= heightAllowed) {
                heightOk = true
            }

            if (widthOk && heightOk) {
                finalWidth = widthAllowed
                finalHeight = heightNeeded
            } else if (!heightOk && widthOk) {
                finalHeight = heightAllowed
                finalWidth = widthNeeded
            } else if (!widthOk && heightOk) {
                finalHeight = heightNeeded
                finalWidth = widthAllowed
            } else {
                finalHeight = heightAllowed
                finalWidth = widthAllowed
            }

        }

        setMeasuredDimension(finalWidth + paddingLeft + paddingRight,
                finalHeight + paddingTop + paddingBottom)
    }

    override fun getPaddingTop(): Int {
        return Math.max(super.getPaddingTop(), mRequiredPadding)
    }

    override fun getPaddingBottom(): Int {
        return Math.max(super.getPaddingBottom(), mRequiredPadding)
    }

    override fun getPaddingLeft(): Int {
        return Math.max(super.getPaddingLeft(), mRequiredPadding)
    }

    override fun getPaddingRight(): Int {
        return Math.max(super.getPaddingRight(), mRequiredPadding)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawingRect = Rect()
        drawingRect!!.left = paddingLeft
        drawingRect!!.right = w - paddingRight
        drawingRect!!.top = paddingTop
        drawingRect!!.bottom = h - paddingBottom

        //The need to be recreated because they depend on the size of the view.
        valShader = null
        satShader = null
        alphaShader = null

        // Clear those bitmap caches since the size may have changed.
        satValBackgroundCache = null
        hueBackgroundCache = null

        setUpSatValRect()
        setUpHueRect()
        setUpAlphaRect()
    }

    private fun setUpSatValRect() {
        //Calculate the size for the big color rectangle.
        val dRect = drawingRect

        val left = dRect!!.left + BORDER_WIDTH_PX
        val top = dRect.top + BORDER_WIDTH_PX
        var bottom = dRect.bottom - BORDER_WIDTH_PX
        val right = dRect.right - BORDER_WIDTH_PX - panelSpacingPx - huePanelWidthPx

        bottom -= alphaPanelHeightPx + panelSpacingPx

        satValRect = Rect(left, top, right, bottom)
    }

    private fun setUpHueRect() {
        //Calculate the size for the hue slider on the left.
        val dRect = drawingRect

        val left = dRect!!.right - huePanelWidthPx + BORDER_WIDTH_PX
        val top = dRect.top + BORDER_WIDTH_PX
        val bottom = dRect.bottom - BORDER_WIDTH_PX - (panelSpacingPx + alphaPanelHeightPx)
        val right = dRect.right - BORDER_WIDTH_PX

        hueRect = Rect(left, top, right, bottom)
    }

    private fun setUpAlphaRect() {
        val dRect = drawingRect

        val left = dRect!!.left + BORDER_WIDTH_PX
        val top = dRect.bottom - alphaPanelHeightPx + BORDER_WIDTH_PX
        val bottom = dRect.bottom - BORDER_WIDTH_PX
        val right = dRect.right - BORDER_WIDTH_PX

        alphaRect = Rect(left, top, right, bottom)

        alphaPatternDrawable = AlphaPatternDrawable(dpToPx(context, 4f))
        alphaPatternDrawable!!.setBounds(Math.round(alphaRect!!.left.toFloat()), Math
                .round(alphaRect!!.top.toFloat()), Math.round(alphaRect!!.right.toFloat()), Math
                .round(alphaRect!!.bottom.toFloat()))
    }

    /**
     * Set a OnColorChangedListener to get notified when the color
     * selected by the user has changed.
     *
     * @param listener the listener
     */
    fun setOnColorChangedListener(listener: OnColorChangedListener) {
        onColorChangedListener = listener
    }

    /**
     * Set the color this view should show.
     *
     * @param color    The color that should be selected. #argb
     * @param callback If you want to get a callback to your OnColorChangedListener.
     */
    fun setColor(color: Int, callback: Boolean) {

        val alpha = Color.alpha(color)
        val red = Color.red(color)
        val blue = Color.blue(color)
        val green = Color.green(color)

        val hsv = FloatArray(3)

        Color.RGBToHSV(red, green, blue, hsv)

        this.alpha = alpha
        hue = hsv[0]
        sat = hsv[1]
        value = hsv[2]

        if (callback) {
            onColorChangedListener?.onColorChanged(Color.HSVToColor(this.alpha, floatArrayOf(hue, sat, value)))
        }

        invalidate()
    }

    private inner class BitmapCache {
        var canvas: Canvas? = null
        var bitmap: Bitmap? = null
        var value: Float = 0.toFloat()
    }

    private fun dpToPx(c: Context, dipValue: Float): Int {
        val metrics = c.resources.displayMetrics
        val value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
        val res = (value + 0.5).toInt() // Round
        // Ensure at least 1 pixel if value was > 0
        return if (res == 0 && value > 0) 1 else res
    }

    interface OnColorChangedListener {
        fun onColorChanged(newColor: Int)
    }

    companion object {
        private val HUE_PANEL_WDITH_DP = 30
        private val ALPHA_PANEL_HEIGH_DP = 20
        private val PANEL_SPACING_DP = 10
        private val CIRCLE_TRACKER_RADIUS_DP = 5
        private val SLIDER_TRACKER_SIZE_DP = 4
        private val SLIDER_TRACKER_OFFSET_DP = 2

        /**
         * The width in pixels of the border
         * surrounding all color panels.
         */
        private val BORDER_WIDTH_PX = 1
    }
}
