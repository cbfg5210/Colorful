package com.ue.colorful.widget

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import com.ue.colorful.R
import com.ue.colorful.event.ColorListener
import kotlinx.android.synthetic.main.fragment_photo_picker.view.*


class PhotoColorPickerView : FrameLayout {
    var color: Int = 0
        private set

    private val palette: ImageView
    private val selector: ImageView

    private var paletteDrawable: Drawable? = null
    private var colorListener: ColorListener? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.PhotoColorPickerView)
            if (a.hasValue(R.styleable.PhotoColorPickerView_palette)) {
                paletteDrawable = a.getDrawable(R.styleable.PhotoColorPickerView_palette)
                palette.setImageDrawable(paletteDrawable)
            }
            a.recycle()
        }
    }

    init {
        setPadding(0, 0, 0, 0) //不设置的话取色不正确
        palette = ImageView(context)
        //palette.adjustViewBounds = true //这里如果设置的话取色不正确

        val wheelParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        wheelParams.gravity = Gravity.CENTER
        addView(palette, wheelParams)

        selector = ImageView(context)
        selector.setImageResource(R.drawable.bg_photo_selector)

        selector.x = (measuredWidth / 2 - selector.width / 2).toFloat()
        selector.y = (measuredHeight / 2 - selector.height / 2).toFloat()

        val thumbParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        thumbParams.gravity = Gravity.CENTER
        addView(selector, thumbParams)

        setListeners()
    }

    private fun setListeners() {
        //添加globalLayoutListener是为了在初始化完成就向外部反馈取到的颜色，然后再移除掉globalLayoutListener
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) viewTreeObserver.removeGlobalOnLayoutListener(this)
                else viewTreeObserver.removeOnGlobalLayoutListener(this)

                setSelectorPoint((measuredWidth / 2 - selector.width / 2).toFloat(), (measuredHeight / 2 - selector.height / 2).toFloat())

                addDirectionTouchListener()
            }
        })

        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> onTouchReceived(event)
                MotionEvent.ACTION_MOVE -> onTouchReceived(event)
                else -> false
            }
        }
    }

    private fun addDirectionTouchListener() {
        vgDirection.setOnTouchListener(object : View.OnTouchListener {
            var lastX = 0
            var lastY = 0
            var offX = 0
            var offY = 0

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.x.toInt()
                        lastY = event.y.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        //计算移动的距离
                        offX = event.x.toInt() - lastX
                        offY = event.y.toInt() - lastY
                        //调用layout方法来重新放置它的位置
                        view.layout(view.left + offX, view.top + offY, view.right + offX, view.bottom + offY)
                    }
                }
                return true
            }
        })

        val directionListener = object : View.OnClickListener {
            override fun onClick(v: View) {
                when (v.id) {
                    R.id.ivMoveUp -> offsetXY(0F, -3F)
                    R.id.ivMoveDown -> offsetXY(0F, 3F)
                    R.id.ivMoveLeft -> offsetXY(-3F, 0F)
                    R.id.ivMoveRight -> offsetXY(3F, 0F)
                }
            }
        }
        ivMoveUp.setOnClickListener(directionListener)
        ivMoveDown.setOnClickListener(directionListener)
        ivMoveLeft.setOnClickListener(directionListener)
        ivMoveRight.setOnClickListener(directionListener)
    }

    private fun onTouchReceived(event: MotionEvent): Boolean {
        color = getColorFromBitmap(event.x, event.y)
        selector.x = event.x - selector.measuredWidth / 2
        selector.y = event.y - selector.measuredHeight / 2
        colorListener?.onColorSelected(color)
        return true
    }

    private fun getColorFromBitmap(x: Float, y: Float): Int {
        if (paletteDrawable == null) return 0

        val invertMatrix = Matrix()
        palette.imageMatrix.invert(invertMatrix)

        val mappedPoints = floatArrayOf(x, y)
        invertMatrix.mapPoints(mappedPoints)

        return if (palette.drawable != null && palette.drawable is BitmapDrawable
                && mappedPoints[0] > 0 && mappedPoints[1] > 0
                && mappedPoints[0] < palette.drawable.intrinsicWidth
                && mappedPoints[1] < palette.drawable.intrinsicHeight) {

            (palette.drawable as BitmapDrawable).bitmap.getPixel(mappedPoints[0].toInt(), mappedPoints[1].toInt())
        } else 0
    }

    fun setSelectorPoint(x: Float, y: Float) {
        selector.x = x
        selector.y = y
        color = getColorFromBitmap(x, y)
        colorListener?.onColorSelected(color)
    }

    fun offsetXY(offsetX: Float, offsetY: Float) {
        selector.x += offsetX
        selector.y += offsetY
        //(selector.x + selector.measuredWidth / 2, selector.y + selector.measuredWidth / 2)才是中心点坐标
        color = getColorFromBitmap(selector.x + selector.measuredWidth / 2, selector.y + selector.measuredWidth / 2)
        colorListener?.onColorSelected(color)
    }

    fun setColorListener(colorListener: ColorListener) {
        this.colorListener = colorListener
    }
}