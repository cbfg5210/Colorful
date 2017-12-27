package com.ue.colorful.feature.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import com.ue.colorful.R
import com.ue.colorful.constant.FunFlags
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.feature.about.AboutActivity
import com.ue.colorful.model.ColorFunCategory
import com.ue.colorful.model.ColorFunction
import com.ue.colorful.util.BackPressedUtils
import com.ue.colorful.util.GsonHolder
import com.ue.fingercoloring.util.SPUtils
import com.ue.recommend.widget.NBottomSheetBehavior.STATE_EXPANDED
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvFunctions.setHasFixedSize(true)
        rvFunctions.adapter = ColorFunCategoryAdapter(this, funCategories)
    }

    private val funCategories: List<ColorFunCategory>
        get() {
            val colorCats = ArrayList<ColorFunCategory>()
            //配色
            var colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction(getString(R.string.md_colors), FunFlags.MD_PALETTE))
            colorFuns.add(ColorFunction(getString(R.string.impression_colors), FunFlags.IM_PALETTE))
            colorCats.add(ColorFunCategory(getString(R.string.harmonize_colors), colorFuns, "bbdefb"))
            //取色
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction(getString(R.string.pick_photo_color), FunFlags.PICKER_PHOTO))
            colorFuns.add(ColorFunction(getString(R.string.pick_palette_color), FunFlags.PICKER_COLOR_PALETTE))
//            colorFuns.add(ColorFunction(getString(R.string.pick_screen_color), FunFlags.PICKER_SCREEN))
            colorFuns.add(ColorFunction(getString(R.string.pick_argb_color), FunFlags.PICKER_ARGB))
            colorCats.add(ColorFunCategory(getString(R.string.pick_color), colorFuns, "f8bbd0"))
            //小游戏
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction(getString(R.string.game_color_diff), FunFlags.GAME_DIFF_COLOR))
            colorFuns.add(ColorFunction(getString(R.string.game_color_light), FunFlags.GAME_LT_COLOR))
            colorCats.add(ColorFunCategory(getString(R.string.games), colorFuns, "e1bee7"))
            //计算
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction(getString(R.string.transparency), FunFlags.CALC_ALPHA))
            colorFuns.add(ColorFunction(getString(R.string.cal_argb), FunFlags.CALC_ARGB))
            colorCats.add(ColorFunCategory(getString(R.string.calculate), colorFuns, "c8e6c9"))
            //其它
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction(getString(R.string.vision_test), FunFlags.VISION_TEST))
            colorCats.add(ColorFunCategory(getString(R.string.other), colorFuns, "c5cae9"))

            return colorCats
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuPalette -> showPalette()
            R.id.menuAbout -> AboutActivity.start(this)
        }
        return true
    }

    private fun showPalette() {
        val paletteColorsStr = SPUtils.getString(SPKeys.PALETTE_COLORS, "")
        if (TextUtils.isEmpty(paletteColorsStr)) {
            Toast.makeText(this, getString(R.string.no_palette_color), Toast.LENGTH_SHORT).show()
            return
        }
        val paletteColors: ArrayList<Int> = GsonHolder.gson.fromJson(paletteColorsStr, object : TypeToken<ArrayList<Int>>() {}.type)

        val dialog = ColorPaletteDialog.newInstance(paletteColors)
        dialog.show(supportFragmentManager, "")
    }

    override fun onBackPressed() {
        if (mbsRecommendSheet.state == STATE_EXPANDED) {
            mbsRecommendSheet.hideBottomSheet();
            return
        }
        BackPressedUtils.exitIfBackTwice(this, getString(R.string.tap_again_to_exit));
    }
}
