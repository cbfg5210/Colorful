package com.ue.colorful.widget

import android.content.Context
import android.graphics.Bitmap
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

    private var colorListener: ColorListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setPadding(0, 0, 0, 0) //不设置的话取色不正确
        View.inflate(context, R.layout.layout_photo_picker, this)
    }

    fun setPhoto(photoRes: Int) {
        ivPickerPhoto.setImageResource(photoRes)
    }

    fun setPhoto(photo: Drawable?) {
        ivPickerPhoto.setImageDrawable(photo)
    }

    fun setPhoto(photo: Bitmap?) {
        ivPickerPhoto.setImageBitmap(photo)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        ivPickerSelector.addOnLayoutChangeListener({ v, left, top, _, _, _, _, _, _ ->
            color = getColorFromBitmap(left + v.measuredWidth * 0.5F, top + v.measuredHeight * 0.5F)
            colorListener?.onColorSelected(color)
        })

        val touchMoveListener = object : View.OnTouchListener {
            internal var lastX = 0
            internal var lastY = 0
            internal var l = 0
            internal var t = 0

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.x.toInt()
                        lastY = event.y.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        l = view.left + event.x.toInt() - lastX
                        t = view.top + event.y.toInt() - lastY

                        if (l < left - view.measuredWidth / 2) l = left - view.measuredWidth / 2
                        else if (l > right - view.measuredWidth / 2) l = right - view.measuredWidth / 2

                        if (t < top - view.measuredHeight / 2) t = top - view.measuredHeight / 2
                        else if (t > bottom - view.measuredHeight / 2) t = bottom - view.measuredHeight / 2

                        view.layout(l, t, l + view.measuredWidth, t + view.measuredHeight)
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
        if (ivPickerPhoto.drawable == null) return 0

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