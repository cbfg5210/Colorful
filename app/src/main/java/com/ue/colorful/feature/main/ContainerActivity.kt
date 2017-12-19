package com.ue.colorful.feature.main

import android.content.ClipData
import android.content.ClipboardManager
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
import com.ue.colorful.event.ContainerCallback
import com.ue.colorful.event.RemovePaletteColorEvent
import com.ue.colorful.feature.calculate.CalcARGBFragment
import com.ue.colorful.feature.calculate.CalcAlphaFragment
import com.ue.colorful.feature.coloring.md.MDPaletteFragment
import com.ue.colorful.feature.game.diffcolor.ClassicModeFragment
import com.ue.colorful.feature.game.diffcolor.TenTimesModeFragment
import com.ue.colorful.feature.game.ltcolor.EasyGameFragment
import com.ue.colorful.feature.game.ltcolor.HardGameFragment
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

class ContainerActivity : AppCompatActivity(), ContainerCallback {
    private lateinit var paletteColors: ArrayList<Int>
    private var fragment: Fragment? = null
    private lateinit var colorFunction: ColorFunction

    private val mClipboardManager: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

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

        colorFunction = intent.getParcelableExtra<ColorFunction>(ARG_COLOR_FUNCTION)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = colorFunction.funName

        when (colorFunction.funFlag) {
            FunFlags.PICKER_MD_PALETTE -> showFragment(MDPaletteFragment())
            FunFlags.PICKER_COLOR_PALETTE -> showFragment(PalettePickerFragment())
            FunFlags.PICKER_PHOTO -> showFragment(PhotoPickerFragment())
            FunFlags.PICKER_ARGB -> showFragment(ARGBPickerFragment())
            FunFlags.PICKER_SCREEN -> showFragment(ScreenPickerFragment())
            FunFlags.GAME_DIFF_COLOR -> {
                val mode = SPUtils.getInt(SPKeys.GAME_DIFF_MODE, Constants.GAME_DIFF_CLASSIC)
                if (mode == Constants.GAME_DIFF_CLASSIC) {
                    supportActionBar?.title = "${colorFunction.funName}-${getString(R.string.mode_classic)}"
                    showFragment(ClassicModeFragment())
                } else {
                    supportActionBar?.title = "${colorFunction.funName}-${getString(R.string.mode_ten_times)}"
                    showFragment(TenTimesModeFragment())
                }
            }
            FunFlags.GAME_LT_COLOR -> {
                val mode = SPUtils.getInt(SPKeys.GAME_LT_MODE, Constants.GAME_LT_EASY)
                if (mode == Constants.GAME_LT_EASY) {
                    supportActionBar?.title = "${colorFunction.funName}-${getString(R.string.mode_easy)}"
                    showFragment(EasyGameFragment())
                } else {
                    supportActionBar?.title = "${colorFunction.funName}-${getString(R.string.mode_hard)}"
                    showFragment(HardGameFragment())
                }
            }
            FunFlags.CALC_ALPHA -> showFragment(CalcAlphaFragment())
            FunFlags.CALC_ARGB -> showFragment(CalcARGBFragment())
            FunFlags.VISION_TEST -> showFragment(ColorVisionTestFragment())
        }

        val paletteColorsStr = SPUtils.getString(SPKeys.PALETTE_COLORS, "")
        paletteColors =
                if (TextUtils.isEmpty(paletteColorsStr)) ArrayList<Int>()
                else GsonHolder.gson.fromJson(paletteColorsStr, object : TypeToken<ArrayList<Int>>() {}.type)
    }

    private fun showFragment(mFragment: Fragment) {
        fragment = mFragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.vgFragmentContainer, fragment)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menuPalette -> showPalette()
            R.id.menuAlbum -> startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), getString(R.string.choose_photo)), Constants.REQ_PICK_PHOTO)

            R.id.menuClassicMode -> if (fragment !is ClassicModeFragment) {
                supportActionBar?.title = "${colorFunction.funName}-${getString(R.string.mode_classic)}"
                showFragment(ClassicModeFragment())
                SPUtils.putInt(SPKeys.GAME_DIFF_MODE, Constants.GAME_DIFF_CLASSIC)
            }
            R.id.menuTenTimesMode -> if (fragment !is TenTimesModeFragment) {
                supportActionBar?.title = "${colorFunction.funName}-${getString(R.string.mode_ten_times)}"
                showFragment(TenTimesModeFragment())
                SPUtils.putInt(SPKeys.GAME_DIFF_MODE, Constants.GAME_DIFF_TEN_TIMES)
            }
            R.id.menuEasyMode -> if (fragment !is EasyGameFragment) {
                supportActionBar?.title = "${colorFunction.funName}-${getString(R.string.mode_easy)}"
                showFragment(EasyGameFragment())
                SPUtils.putInt(SPKeys.GAME_LT_MODE, Constants.GAME_LT_EASY)
            }
            R.id.menuHardMode -> if (fragment !is HardGameFragment) {
                supportActionBar?.title = "${colorFunction.funName}-${getString(R.string.mode_hard)}"
                showFragment(HardGameFragment())
                SPUtils.putInt(SPKeys.GAME_LT_MODE, Constants.GAME_LT_HARD)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (fragment != null && fragment!!.isAdded) fragment!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onRemovePaletteColorEvent(event: RemovePaletteColorEvent) {
        paletteColors.remove(event.position)
        SPUtils.putString(SPKeys.PALETTE_COLORS, paletteColors.toString())
    }

    private fun showPalette() {
        if (paletteColors.size == 0) {
            Toast.makeText(this, getString(R.string.no_palette_color), Toast.LENGTH_SHORT).show()
            return
        }
        val dialog = ColorPaletteDialog.newInstance(paletteColors)
        dialog.show(supportFragmentManager, "")
    }

    override fun copyColor(color: Int) {
        val hex = String.format("#%08X", color)
        val clip = ClipData.newPlainText("copy", hex)
        mClipboardManager.primaryClip = clip

        Toast.makeText(this, getString(R.string.color_copied, hex), Toast.LENGTH_SHORT).show()
    }

    override fun addPaletteColor(color: Int) {
        paletteColors.add(color)
        SPUtils.putString(SPKeys.PALETTE_COLORS, paletteColors.toString())
        Toast.makeText(this, getString(R.string.add_color_ok), Toast.LENGTH_SHORT).show()
    }

    override fun gameOver(gameMode: Int, gameResult: Long) {
        GameResultDialog.newInstance(gameMode,gameResult).show(supportFragmentManager, "")
    }
}
