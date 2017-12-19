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
import com.ue.colorful.constant.Constants
import com.ue.colorful.constant.FunFlags
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.event.AddPaletteColorEvent
import com.ue.colorful.event.RemovePaletteColorEvent
import com.ue.colorful.feature.calculate.CalcARGBFragment
import com.ue.colorful.feature.calculate.CalcAlphaFragment
import com.ue.colorful.feature.coloring.md.MDPaletteFragment
import com.ue.colorful.feature.game.diffcolor.TimeTrialModeFragment
import com.ue.colorful.feature.game.ltcolor.EasyGameFragment
import com.ue.colorful.feature.picker.argb.ARGBPickerFragment
import com.ue.colorful.feature.picker.palette.PalettePickerFragment
import com.ue.colorful.feature.picker.photo.PhotoPickerFragment
import com.ue.colorful.feature.picker.screen.ScreenPickerFragment
import com.ue.colorful.feature.test.ColorVisionTestFragment
import com.ue.colorful.model.ColorFunction
import com.ue.colorful.util.GsonHolder
import com.ue.colorful.util.SPUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class ContainerActivity : AppCompatActivity() {
    private lateinit var paletteColors: ArrayList<Int>
    private lateinit var fragment: Fragment

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
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menuPalette -> showPalette()
            R.id.menuAlbum -> startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), getString(R.string.choose_photo)), Constants.REQ_PICK_PHOTO)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (fragment != null && fragment.isAdded) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
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

    fun showPalette() {
        if (paletteColors.size == 0) {
            Toast.makeText(this, getString(R.string.no_palette_color), Toast.LENGTH_SHORT).show()
            return
        }
        val dialog = ColorPaletteDialog.newInstance(paletteColors)
        dialog.show(supportFragmentManager, "")
    }
}
