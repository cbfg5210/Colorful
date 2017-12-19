package com.ue.colorful.feature.picker.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import kotlinx.android.synthetic.main.fragment_screen_picker.view.*


class ScreenPickerFragment : Fragment() {
    private val REQ_MEDIA_PROJECTION = 1
    private var serviceIntent: Intent? = null

    private val mMediaProjectionManager by lazy {
        activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_screen_picker, container, false)

        rootView.btnCapture.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQ_MEDIA_PROJECTION)
            } else {
                Toast.makeText(activity, "Android5.0以上才可以屏幕取色", Toast.LENGTH_SHORT).show()
            }
        }

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(activity, "没有授权", Toast.LENGTH_SHORT).show()
                return
            }
            Toast.makeText(activity, "已授权", Toast.LENGTH_SHORT).show()
            serviceIntent = serviceIntent ?: Intent(activity, CaptureService::class.java)
            serviceIntent!!.putExtra(Constants.REQ_RESULT_DATA, data)
            activity.startService(serviceIntent)
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (serviceIntent != null) {
            activity.stopService(serviceIntent)
        }
    }
}
