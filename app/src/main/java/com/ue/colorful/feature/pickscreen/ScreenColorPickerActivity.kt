package com.ue.colorful.feature.pickscreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.ue.colorful.R
import com.ue.colorful.constant.Constants


class ScreenColorPickerActivity : AppCompatActivity() {
    private val REQUEST_MEDIA_PROJECTION = 1
    private var serviceIntent: Intent? = null

    private val mMediaProjectionManager by lazy {
        getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_color_picker)
    }

    fun onBtnClick(v: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION)
        } else {
            Toast.makeText(this, "Android5.0以上才可以屏幕取色", Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "没有授权", Toast.LENGTH_SHORT).show()
                return
            }
            Toast.makeText(this, "已授权", Toast.LENGTH_SHORT).show()
            serviceIntent = serviceIntent ?: Intent(this, CaptureService::class.java)
            serviceIntent!!.putExtra(Constants.REQ_RESULT_DATA, data)
            startService(serviceIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceIntent != null) {
            stopService(serviceIntent)
        }
    }
}
