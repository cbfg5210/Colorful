package com.ue.colorful.feature.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.ue.colorful.R
import com.ue.colorful.model.ColorFunCategory
import com.ue.colorful.model.ColorFunction
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvFunctions.setHasFixedSize(true)
        rvFunctions.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvFunctions.adapter = ColorFunCategoryAdapter(this, funCategories)
    }

    private val funCategories: List<ColorFunCategory>
        get() {
            val colorCats = ArrayList<ColorFunCategory>()
            //配色
            var colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction("MaterialDesign配色", 0))
            colorCats.add(ColorFunCategory("配色", colorFuns, "99000000"))
            //取色
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction("图片取色", 1))
            colorFuns.add(ColorFunction("色板取色", 1))
            colorFuns.add(ColorFunction("屏幕取色", 1))
            colorFuns.add(ColorFunction("argb取色", 1))
            colorCats.add(ColorFunCategory("取色", colorFuns, "7F000000"))
            //小游戏
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction("ColorDiff", 0))
            colorFuns.add(ColorFunction("ColorPhun", 0))
            colorCats.add(ColorFunCategory("小游戏", colorFuns, "7F000000"))
            //计算
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction("透明度计算", 0))
            colorFuns.add(ColorFunction("argb计算", 0))
            colorCats.add(ColorFunCategory("计算", colorFuns, "7F000000"))
            //其它
            colorFuns = ArrayList<ColorFunction>()
            colorFuns.add(ColorFunction("色觉测试", 0))
            colorCats.add(ColorFunCategory("其它", colorFuns, "7F000000"))

            return colorCats
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuPalette -> Toast.makeText(this, "palette", Toast.LENGTH_SHORT).show()
            R.id.menuAbout -> Toast.makeText(this, "about", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}
