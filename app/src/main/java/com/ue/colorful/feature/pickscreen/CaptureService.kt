package com.ue.colorful.feature.pickscreen

import android.annotation.TargetApi
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.ue.colorful.R
import com.ue.colorful.constant.Constants


/**
 * Created by hawk on 2017/12/10.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class CaptureService : Service() {
    private var mResultData: Intent? = null

    private lateinit var mImageReader: ImageReader
    private lateinit var mWindowManager: WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams
    private lateinit var mGestureDetector: GestureDetector

    private lateinit var floatLayout: View
    private lateinit var ivCapture: ImageView
    private lateinit var txColorHex: TextView

    private var mScreenWidth: Int = 0
    private var mScreenHeight: Int = 0
    private var mScreenDensity: Int = 0

    private var mMediaProjectionManager: MediaProjectionManager? = null
    private var mMediaProjection: MediaProjection? = null
    private var mVirtualDisplay: VirtualDisplay? = null

    private//每个像素的间距
            //总的间距
    val screenCapture: Bitmap?
        get() {
            val image = mImageReader.acquireLatestImage() ?: return null

            val width = image.width
            val height = image.height
            val planes = image.planes
            val buffer = planes[0].buffer
            val pixelStride = planes[0].pixelStride
            val rowStride = planes[0].rowStride
            val rowPadding = rowStride - pixelStride * width
            var bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
            bitmap.copyPixelsFromBuffer(buffer)
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
            image.close()

            return bitmap
        }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.hasExtra(Constants.REQ_RESULT_DATA)) {
            mResultData = intent.getParcelableExtra(Constants.REQ_RESULT_DATA)
            Log.e("CaptureService", "onStartCommand: mResultData=" + mResultData!!)

            createFloatView()

            mMediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1)
            startScreenCapture()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun createFloatView() {
        mGestureDetector = GestureDetector(applicationContext, FloatGestrueTouchListener())
        mLayoutParams = WindowManager.LayoutParams()
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val metrics = resources.displayMetrics
        mScreenDensity = metrics.densityDpi
        mScreenWidth = metrics.widthPixels
        mScreenHeight = metrics.heightPixels

        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        mLayoutParams.format = PixelFormat.RGBA_8888
        // 设置Window flag
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mLayoutParams.gravity = Gravity.LEFT or Gravity.TOP
        mLayoutParams.x = mScreenWidth
        mLayoutParams.y = 100
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

        /*val layoutParams = WindowManager.LayoutParams()
        layoutParams.x = x
        layoutParams.y = y
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.format = PixelFormat.TRANSPARENT
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }*/

        floatLayout = LayoutInflater.from(this).inflate(R.layout.layout_float_view, null)
        txColorHex = floatLayout.findViewById(R.id.tvColorHex)
        ivCapture = floatLayout.findViewById(R.id.ivCapture)
        ivCapture.setOnTouchListener { v, event -> mGestureDetector.onTouchEvent(event) }
        try {
            mWindowManager.addView(floatLayout, mLayoutParams)
        } catch (exp: Exception) {
            Log.e("CaptureService", "createFloatView: add view failed=" + exp.message)
        }

    }

    private fun startScreenCapture() {
        if (mMediaProjection != null) {
            setUpVirtualDisplay()
        } else {
            setUpMediaProjection()
            setUpVirtualDisplay()
        }
    }

    private fun setUpMediaProjection() {
        mMediaProjection = mMediaProjectionManager!!.getMediaProjection(Activity.RESULT_OK, mResultData!!)
    }

    private fun setUpVirtualDisplay() {
        mVirtualDisplay = mMediaProjection!!.createVirtualDisplay("ScreenCapture",
                mScreenWidth, mScreenHeight, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.surface, null, null)
    }

    private fun stopScreenCapture() {
        if (mVirtualDisplay == null) {
            return
        }
        mVirtualDisplay!!.release()
        mVirtualDisplay = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopScreenCapture()
    }

    private inner class FloatGestrueTouchListener : GestureDetector.OnGestureListener {
        internal var lastX: Int = 0
        internal var lastY: Int = 0
        internal var paramX: Int = 0
        internal var paramY: Int = 0

        override fun onDown(event: MotionEvent): Boolean {
            // 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
            lastX = event.rawX.toInt()
            lastY = event.rawY.toInt()
            paramX = mLayoutParams.x
            paramY = mLayoutParams.y
            return true
        }

        override fun onShowPress(e: MotionEvent) {
            /*
             * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
             * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
             */
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            // 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
            Log.e("CaptureService", "onSingleTapUp: is attached=" + ViewCompat.isAttachedToWindow(floatLayout))
            if (floatLayout == null || !ViewCompat.isAttachedToWindow(floatLayout)) {
                return false
            }

            val tempBitmap = screenCapture
            Log.e("CaputreService", "onSingleTapUp: bitmap=" + tempBitmap!!)
            if (tempBitmap == null) {
                return false
            }

            val invertMatrix = Matrix()
            floatLayout!!.rootView.matrix.invert(invertMatrix)

            val touchPoint = floatArrayOf(e.rawX, e.rawY)
            invertMatrix.mapPoints(touchPoint)

            val xCoord = touchPoint[0].toInt()
            val yCoord = touchPoint[1].toInt()

            val color = tempBitmap.getPixel(xCoord, yCoord)
            txColorHex.text = "#" + String.format("%06X", 0xFFFFFF and color)

            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            // 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
            val dx = e2.rawX.toInt() - lastX
            val dy = e2.rawY.toInt() - lastY
            mLayoutParams.x = paramX + dx
            mLayoutParams.y = paramY + dy
            // 更新悬浮窗位置
            mWindowManager.updateViewLayout(floatLayout, mLayoutParams)
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            // 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            // 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
            Log.e("CaptureService", "onFling: ")
            return false
        }
    }
}
