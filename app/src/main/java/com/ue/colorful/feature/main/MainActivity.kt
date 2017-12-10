package com.ue.colorful.feature.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.feature.material.MDPaletteActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBtnClick(v: View) {
        when (v.id) {
            R.id.btnMDPalette -> startActivity(Intent(this, MDPaletteActivity::class.java))
        }
    }
}
