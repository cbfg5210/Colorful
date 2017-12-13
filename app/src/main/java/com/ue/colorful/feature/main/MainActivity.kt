package com.ue.colorful.feature.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.feature.calculate.CalculateActivity
import com.ue.colorful.feature.game.ClassicModeActivity
import com.ue.colorful.feature.game.TimeTrialModeActivity
import com.ue.colorful.feature.game_phun.EasyGameActivity
import com.ue.colorful.feature.game_phun.HardGameActivity
import com.ue.colorful.feature.material.MDPaletteActivity
import com.ue.colorful.feature.pickpalette.PaletteColorPickerActivity
import com.ue.colorful.feature.pickphoto.PhotoColorPickerActivity
import com.ue.colorful.feature.pickscreen.ScreenColorPickerActivity
import com.ue.colorful.feature.test.ColorVisionTestActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBtnClick(v: View) {
        when (v.id) {
            R.id.btnMDPalette -> startActivity(Intent(this, MDPaletteActivity::class.java))
            R.id.btnPickFromPhoto -> startActivity(Intent(this, PhotoColorPickerActivity::class.java))
            R.id.btnPickFromPalette -> startActivity(Intent(this, PaletteColorPickerActivity::class.java))
            R.id.btnPickFromScreen -> startActivity(Intent(this, ScreenColorPickerActivity::class.java))
            R.id.btnCalculate -> startActivity(Intent(this, CalculateActivity::class.java))
            R.id.btnGame -> startActivity(Intent(this, ClassicModeActivity::class.java))
            R.id.btnGame1 -> startActivity(Intent(this, TimeTrialModeActivity::class.java))
            R.id.btnPhunGame -> startActivity(Intent(this, EasyGameActivity::class.java))
            R.id.btnPhunGame1 -> startActivity(Intent(this, HardGameActivity::class.java))
            R.id.btnTest -> startActivity(Intent(this, ColorVisionTestActivity::class.java))
        }
    }
}
