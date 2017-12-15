package com.ue.colorful.widget

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.ue.colorful.R
import com.ue.colorful.event.ColorListener
import kotlinx.android.synthetic.main.layout_photo_picker.view.*

class PhotoColorPickerView : FrameLayout {
    var color: Int = 0
        private set

    private var paletteDrawable: Drawable? = null
    private var colorListener: ColorListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.PhotoColorPickerView)
            if (a.hasValue(R.styleable.PhotoColorPickerView_palette)) {
                paletteDrawable = a.getDrawable(R.styleable.PhotoColorPickerView_palette)
            }
            a.recycle()
        }

        setPadding(0, 0, 0, 0) //不设置的话取色不正确
        View.inflate(context, R.layout.layout_photo_picker, this)
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        if (paletteDrawable != null) {
            ivPickerPhoto.setImageDrawable(paletteDrawable)
        }

        addDirectionTouchListener()
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> onTouchReceived(event)
                MotionEvent.ACTION_MOVE -> onTouchReceived(event)
                else -> false
            }
        }

        post(Runnable {
            //加入队列等待执行
            color = getColorFromBitmap(measuredWidth * 0.5F, measuredHeight * 0.5F)
            colorListener?.onColorSelected(color)
        })
    }

    private fun addDirectionTouchListener() {
        vgDirection.setOnTouchListener(object : View.OnTouchListener {
            var lastX = 0
            var lastY = 0
            var offX = 0
            var offY = 0
            var l = 0
            var r = 0
            var t = 0
            var b = 0

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.x.toInt()
                        lastY = event.y.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        //计算移动的距离
                        //Log.e("PhotoColorPickerView", "onTouch: l=$left,r=$right,t=$top,b=$bottom");
                        //l=0,r=720,t=0,b=958
                        offX = event.x.toInt() - lastX
                        offY = event.y.toInt() - lastY
                        //调用layout方法来重新放置它的位置
                        l = view.left + offX
                        r = view.right + offX
                        t = view.top + offY
                        b = view.bottom + offY

                        if (l < left) {
                            l = 0
                            r = view.measuredWidth
                        } else if (r > right) {
                            r = right
                            l = r - view.measuredWidth
                        }
                        if (t < top) {
                            t = 0
                            b = view.measuredHeight
                        } else if (b > bottom) {
                            b = bottom
                            t = b - view.measuredHeight
                        }
                        view.layout(l, t, r, b)
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
        ivPickerSelector.x = event.x - ivPickerSelector.measuredWidth / 2
        ivPickerSelector.y = event.y - ivPickerSelector.measuredHeight / 2
        colorListener?.onColorSelected(color)
        return true
    }

    private fun getColorFromBitmap(x: Float, y: Float): Int {
        if (paletteDrawable == null) return 0

        val invertMatrix = Matrix()
        ivPickerPhoto.imageMatrix.invert(invertMatrix)

        val mappedPoints = floatArrayOf(x, y)
        invertMatrix.mapPoints(mappedPoints)

        return if (ivPickerPhoto.drawable != null && ivPickerPhoto.drawable is BitmapDrawable
                && mappedPoints[0] > 0 && mappedPoints[1] > 0
                && mappedPoints[0] < ivPickerPhoto.drawable.intrinsicWidth
                && mappedPoints[1] < ivPickerPhoto.drawable.intrinsicHeight) {

            (ivPickerPhoto.drawable as BitmapDrawable).bitmap.getPixel(mappedPoints[0].toInt(), mappedPoints[1].toInt())
        } else 0
    }

    fun offsetXY(offsetX: Float, offsetY: Float) {
        ivPickerSelector.x += offsetX
        ivPickerSelector.y += offsetY
        //(selector.x + selector.measuredWidth / 2, selector.y + selector.measuredWidth / 2)才是中心点坐标
        color = getColorFromBitmap(ivPickerSelector.x + ivPickerSelector.measuredWidth / 2, ivPickerSelector.y + ivPickerSelector.measuredWidth / 2)
        colorListener?.onColorSelected(color)
    }

    fun setColorListener(colorListener: ColorListener) {
        this.colorListener = colorListener
    }
}