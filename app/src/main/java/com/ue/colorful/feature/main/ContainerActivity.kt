package com.ue.colorful.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import com.ue.colorful.R
import com.ue.colorful.constant.FunFlags
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.event.AddPaletteColorEvent
import com.ue.colorful.event.RemovePaletteColorEvent
import com.ue.colorful.event.ShowPaletteEvent
import com.ue.colorful.feature.calculate.CalculateFragment
import com.ue.colorful.feature.game.TimeTrialModeFragment
import com.ue.colorful.feature.game_phun.EasyGameFragment
import com.ue.colorful.feature.material.MDPaletteFragment
import com.ue.colorful.feature.pickargb.PickARGBFragment
import com.ue.colorful.feature.pickpalette.PaletteColorPickerFragment
import com.ue.colorful.feature.pickphoto.PhotoColorPickerFragment
import com.ue.colorful.feature.pickscreen.ScreenColorPickerFragment
import com.ue.colorful.feature.test.ColorVisionTestFragment
import com.ue.colorful.util.GsonHolder
import com.ue.colorful.util.SPUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ContainerActivity : AppCompatActivity() {
    private lateinit var paletteColors: ArrayList<Int>

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
        EventBus.getDefault().register(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val paletteColorsStr = SPUtils.getString(SPKeys.PALETTE_COLORS, "")
        paletteColors =
                if (TextUtils.isEmpty(paletteColorsStr)) ArrayList<Int>()
                else GsonHolder.gson.fromJson(paletteColorsStr, object : TypeToken<ArrayList<Int>>() {}.type)

        val fragFlag = intent.getIntExtra(ARG_FRAGMENT_FLAG, 0)
        when (fragFlag) {
            FunFlags.PICKER_MD_PALETTE -> {
                supportActionBar?.title = "MDPalette"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, MDPaletteFragment())
                        .commit()
            }
            FunFlags.PICKER_COLOR_PALETTE -> {
                supportActionBar?.title = "ColorPalette"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, PaletteColorPickerFragment())
                        .commit()
            }
            FunFlags.PICKER_PHOTO -> {
                supportActionBar?.title = "PickFromPhoto"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, PhotoColorPickerFragment())
                        .commit()
            }
            FunFlags.PICKER_ARGB -> {
                supportActionBar?.title = "ARGB"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, PickARGBFragment())
                        .commit()
            }
            FunFlags.PICKER_SCREEN -> {
                supportActionBar?.title = "PickFromScreen"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, ScreenColorPickerFragment())
                        .commit()
            }
            FunFlags.GAME_COLOR_DIFF -> {
                supportActionBar?.title = "ColorDiff"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, TimeTrialModeFragment())
                        .commit()
            }
            FunFlags.GAME_PHUN -> {
                supportActionBar?.title = "Phun"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, EasyGameFragment())
                        .commit()
            }
            FunFlags.CALCULATE -> {
                supportActionBar?.title = "Calculate"
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, CalculateFragment())
                        .commit()
            }
            FunFlags.VISION_TEST -> {
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

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onAddPaletteColorEvent(event: AddPaletteColorEvent) {
        paletteColors.add(event.color)
        SPUtils.putString(SPKeys.PALETTE_COLORS, paletteColors.toString())
        Toast.makeText(this, getString(R.string.add_color_ok), Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    fun onRemovePaletteColorEvent(event: RemovePaletteColorEvent) {
        paletteColors.remove(event.position)
        SPUtils.putString(SPKeys.PALETTE_COLORS, paletteColors.toString())
    }

    @Subscribe
    fun onShowPaletteEvent(event: ShowPaletteEvent) {
        if (paletteColors.size == 0) {
            Toast.makeText(this, getString(R.string.no_palette_color), Toast.LENGTH_SHORT).show()
            return
        }
        val dialog = ColorPaletteDialog.newInstance(paletteColors)
        dialog.show(supportFragmentManager, "")
    }
}
