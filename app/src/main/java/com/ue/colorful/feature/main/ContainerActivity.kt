package com.ue.colorful.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import com.ue.colorful.feature.calculate.CalculateFragment
import com.ue.colorful.feature.game.TimeTrialModeFragment
import com.ue.colorful.feature.game_phun.EasyGameFragment
import com.ue.colorful.feature.material.MDPaletteFragment
import com.ue.colorful.feature.pickargb.PickARGBFragment
import com.ue.colorful.feature.pickpalette.PaletteColorPickerFragment
import com.ue.colorful.feature.pickphoto.PhotoColorPickerFragment
import com.ue.colorful.feature.test.ColorVisionTestFragment

class ContainerActivity : AppCompatActivity() {

    companion object {
        private val ARG_FRAGMENT_FLAG = "arg_frag_flag"

        fun start(context: Context, fragFlag: Int) {
            val intent = Intent(context, ContainerActivity::class.java)
            intent.putExtra(ARG_FRAGMENT_FLAG, fragFlag)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragFlag = intent.getIntExtra(ARG_FRAGMENT_FLAG, 0)
        when (fragFlag) {
            Constants.FRAG_PICK_FROM_MD_PALETTE -> {
                supportActionBar?.title = "MDPalette"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, MDPaletteFragment())
                        .commit()
            }
            Constants.FRAG_PICK_FROM_COLOR_PALETTE -> {
                supportActionBar?.title = "ColorPalette"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, PaletteColorPickerFragment())
                        .commit()
            }
            Constants.FRAG_PICK_FROM_PHOTO -> {
                supportActionBar?.title = "PickFromPhoto"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, PhotoColorPickerFragment())
                        .commit()
            }
            Constants.FRAG_PICK_FROM_ARGB -> {
                supportActionBar?.title = "ARGB"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, PickARGBFragment())
                        .commit()
            }
            Constants.FRAG_PICK_FROM_SCREEN -> {
            }
            Constants.FRAG_GAME_COLOR_DIFF -> {
                supportActionBar?.title = "ColorDiff"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, TimeTrialModeFragment())
                        .commit()
            }
            Constants.FRAG_GAME_PHUN -> {
                supportActionBar?.title = "Phun"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, EasyGameFragment())
                        .commit()
            }
            Constants.FRAG_CALCULATE -> {
                supportActionBar?.title = "Calculate"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, CalculateFragment())
                        .commit()
            }
            Constants.FRAG_VISION_TEST -> {
                supportActionBar?.title = "VisionTest"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, ColorVisionTestFragment())
                        .commit()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
