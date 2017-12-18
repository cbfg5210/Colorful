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
import com.ue.colorful.model.ColorFunCategory
import com.ue.colorful.model.ColorFunction
import com.ue.colorful.util.GsonHolder
import com.ue.colorful.util.SPUtils
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
            colorFuns.add(ColorFunction("MaterialDesign配色", FunFlags.PICKER_MD_PALETTE))
            colorCats.add(ColorFunCategory("配色", colorFuns, "bbdefb"))
            //取色
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction("图片取色", FunFlags.PICKER_PHOTO))
            colorFuns.add(ColorFunction("色板取色", FunFlags.PICKER_COLOR_PALETTE))
            colorFuns.add(ColorFunction("屏幕取色", FunFlags.PICKER_SCREEN))
            colorFuns.add(ColorFunction("argb取色", FunFlags.PICKER_ARGB))
            colorCats.add(ColorFunCategory("取色", colorFuns, "f8bbd0"))
            //小游戏
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction("ColorDiff", FunFlags.GAME_COLOR_DIFF))
            colorFuns.add(ColorFunction("ColorPhun", FunFlags.GAME_PHUN))
            colorCats.add(ColorFunCategory("小游戏", colorFuns, "e1bee7"))
            //计算
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction("透明度", FunFlags.CALC_ALPHA))
            colorFuns.add(ColorFunction("颜色值&ARGB", FunFlags.CALC_ARGB))
            colorCats.add(ColorFunCategory("计算", colorFuns, "c8e6c9"))
            //其它
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction("色觉测试", FunFlags.VISION_TEST))
            colorCats.add(ColorFunCategory("其它", colorFuns, "c5cae9"))

            return colorCats
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuPalette -> showPalette()
            R.id.menuAbout -> Toast.makeText(this, "about", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun showPalette() {
        val paletteColorsStr = SPUtils.getString(SPKeys.PALETTE_COLORS, "")
        val paletteColors =
                if (TextUtils.isEmpty(paletteColorsStr)) ArrayList<Int>()
                else GsonHolder.gson.fromJson(paletteColorsStr, object : TypeToken<ArrayList<Int>>() {}.type)

        if (paletteColors.size == 0) {
            Toast.makeText(this, getString(R.string.no_palette_color), Toast.LENGTH_SHORT).show()
            return
        }
        val dialog = ColorPaletteDialog.newInstance(paletteColors)
        dialog.show(supportFragmentManager, "")
    }
}
