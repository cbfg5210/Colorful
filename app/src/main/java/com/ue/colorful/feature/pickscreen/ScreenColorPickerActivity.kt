package com.ue.colorful.feature.pickscreen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.util.QMUIDrawableHelper
import kotlinx.android.synthetic.main.activity_screen_color_picker.*


class ScreenColorPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_color_picker)
    }

    fun onBtnClick(v: View) {
        val contentView: View = window.decorView.findViewById(android.R.id.content)
        val createFromViewBitmap = QMUIDrawableHelper.createBitmapFromView(contentView.rootView)
        ivCapture.setImageBitmap(createFromViewBitmap)
    }
}
