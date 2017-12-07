package com.ue.colorful

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBtnClick(v: View) {
        if (v.id == R.id.ivColorPicker) {
            startActivity(Intent(this, ColorPickerActivity::class.java));
        } else {
            startActivity(Intent(this, MultiColorPickerActivity::class.java));
        }
    }
}
