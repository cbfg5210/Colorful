package com.ue.colorful.feature.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import com.ue.colorful.feature.pickscreen.ScreenColorPickerActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBtnClick(v: View) {
        when (v.id) {
            R.id.btnMDPalette -> ContainerActivity.start(this, Constants.FRAG_PICK_FROM_MD_PALETTE)
            R.id.btnPickFromPhoto -> ContainerActivity.start(this, Constants.FRAG_VISION_TEST)
            R.id.btnPickFromPalette -> ContainerActivity.start(this, Constants.FRAG_PICK_FROM_COLOR_PALETTE)
            R.id.btnPickFromScreen -> startActivity(Intent(this, ScreenColorPickerActivity::class.java))
            R.id.btnCalculate -> ContainerActivity.start(this, Constants.FRAG_CALCULATE)
            R.id.btnGame -> ContainerActivity.start(this, Constants.FRAG_GAME_COLOR_DIFF)
            R.id.btnGame1 -> ContainerActivity.start(this, Constants.FRAG_VISION_TEST)
            R.id.btnPhunGame -> ContainerActivity.start(this, Constants.FRAG_GAME_PHUN)
            R.id.btnPhunGame1 -> ContainerActivity.start(this, Constants.FRAG_VISION_TEST)
            R.id.btnTest -> ContainerActivity.start(this, Constants.FRAG_VISION_TEST)
            R.id.btnPickFromARGB -> ContainerActivity.start(this, Constants.FRAG_PICK_FROM_ARGB)
        }
    }
}
