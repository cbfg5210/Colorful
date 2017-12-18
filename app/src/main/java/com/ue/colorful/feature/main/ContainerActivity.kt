package com.ue.colorful.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.ue.colorful.feature.calculate.CalcARGBFragment
import com.ue.colorful.feature.calculate.CalcAlphaFragment
import com.ue.colorful.feature.game.TimeTrialModeFragment
import com.ue.colorful.feature.game_phun.EasyGameFragment
import com.ue.colorful.feature.material.MDPaletteFragment
import com.ue.colorful.feature.pickargb.ARGBPickerFragment
import com.ue.colorful.feature.pickpalette.PalettePickerFragment
import com.ue.colorful.feature.pickphoto.PhotoPickerFragment
import com.ue.colorful.feature.pickscreen.ScreenPickerFragment
import com.ue.colorful.feature.test.ColorVisionTestFragment
import com.ue.colorful.model.ColorFunction
import com.ue.colorful.util.GsonHolder
import com.ue.colorful.util.SPUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ContainerActivity : AppCompatActivity() {
    private lateinit var paletteColors: ArrayList<Int>

    companion object {
        private val ARG_COLOR_FUNCTION = "arg_frag_flag"

        fun start(context: Context, colorFunction: ColorFunction) {
            val intent = Intent(context, ContainerActivity::class.java)
            intent.putExtra(ARG_COLOR_FUNCTION, colorFunction)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        if (!intent.hasExtra(ARG_COLOR_FUNCTION)) {
            finish()
            return
        }
        EventBus.getDefault().register(this)

        val colorFunction = intent.getParcelableExtra<ColorFunction>(ARG_COLOR_FUNCTION)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = colorFunction.funName

        val paletteColorsStr = SPUtils.getString(SPKeys.PALETTE_COLORS, "")
        paletteColors =
                if (TextUtils.isEmpty(paletteColorsStr)) ArrayList<Int>()
                else GsonHolder.gson.fromJson(paletteColorsStr, object : TypeToken<ArrayList<Int>>() {}.type)

        lateinit var fragment: Fragment

        when (colorFunction.funFlag) {
            FunFlags.PICKER_MD_PALETTE -> fragment = MDPaletteFragment()
            FunFlags.PICKER_COLOR_PALETTE -> fragment = PalettePickerFragment()
            FunFlags.PICKER_PHOTO -> fragment = PhotoPickerFragment()
            FunFlags.PICKER_ARGB -> fragment = ARGBPickerFragment()
            FunFlags.PICKER_SCREEN -> fragment = ScreenPickerFragment()
            FunFlags.GAME_COLOR_DIFF -> fragment = TimeTrialModeFragment()
            FunFlags.GAME_PHUN -> fragment = EasyGameFragment()
            FunFlags.CALC_ALPHA -> fragment = CalcAlphaFragment()
            FunFlags.CALC_ARGB -> fragment = CalcARGBFragment()
            FunFlags.VISION_TEST -> fragment = ColorVisionTestFragment()
            else -> fragment = MDPaletteFragment()
        }
        supportFragmentManager.beginTransaction()
                .add(R.id.vgFragmentContainer, fragment)
                .commit()
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
