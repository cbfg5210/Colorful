package com.ue.colorful.widget

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.SystemClock
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

        ivPickerSelector.addOnLayoutChangeListener({ v, left, top, _, _, _, _, _, _ ->
            color = getColorFromBitmap(left + v.measuredWidth * 0.5F, top + v.measuredHeight * 0.5F)
            colorListener?.onColorSelected(color)
        })

        val touchMoveListener = object : View.OnTouchListener {
            internal var lastX = 0
            internal var lastY = 0
            internal var offX = 0
            internal var offY = 0
            internal var l = 0
            internal var t = 0
            internal var r = 0
            internal var b = 0

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

                        l = view.left + offX
                        t = view.top + offY
                        r = view.right + offX
                        b = view.bottom + offY

                        if (l < left) l = left
                        else if (r > right) r = right

                        if (t < top) t = top
                        else if (b > bottom) b = bottom

                        view.layout(l, t, r, b)
                    }
                }
                return true
            }
        }

        ivPickerSelector.setOnTouchListener(touchMoveListener)
        vgDirection.setOnTouchListener(touchMoveListener)

        val directionListener = object : View.OnClickListener {
            internal var nX = 0F
            internal var nY = 0F
            override fun onClick(v: View) {
                nX = ivPickerSelector.x
                nY = ivPickerSelector.y
                when (v.id) {
                    R.id.ivMoveUp -> nY -= 3
                    R.id.ivMoveDown -> nY += 3
                    R.id.ivMoveLeft -> nX -= 3
                    R.id.ivMoveRight -> nX += 3
                }
                //模拟touch事件
                ivPickerSelector.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, ivPickerSelector.x, ivPickerSelector.y, 0))
                ivPickerSelector.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, nX, nY, 0))
            }
        }
        ivMoveUp.setOnClickListener(directionListener)
        ivMoveDown.setOnClickListener(directionListener)
        ivMoveLeft.setOnClickListener(directionListener)
        ivMoveRight.setOnClickListener(directionListener)

        post({
            //加入队列等待执行
            color = getColorFromBitmap(measuredWidth * 0.5F, measuredHeight * 0.5F)
            colorListener?.onColorSelected(color)
        })
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

    fun setColorListener(colorListener: ColorListener) {
        this.colorListener = colorListener
    }
}