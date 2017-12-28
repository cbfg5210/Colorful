package com.ue.fingercoloring.feature.paint

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.fingercoloring.R
import com.ue.fingercoloring.factory.MyDialogFactory
import com.ue.fingercoloring.listener.SimpleTarget
import com.ue.fingercoloring.util.FileUtils
import com.ue.fingercoloring.util.PicassoUtils
import com.ue.fingercoloring.util.ShareImageUtil
import com.ue.fingercoloring.view.ColorPicker
import com.ue.fingercoloring.view.ColourImageView
import com.ue.fingercoloring.view.TipDialog
import kotlinx.android.synthetic.main.activity_paint.*


class PaintActivity : AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private var isFromThemes: Boolean = false
    private var picturePath: String = ""
    private var pictureName: String = ""

    private lateinit var myDialogFactory: MyDialogFactory
    private lateinit var presenter: PaintPresenter
    private lateinit var tipDialog: TipDialog

    private lateinit var pickedColorAdapter: PickedColorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint)

        val intent = intent
        isFromThemes = intent.getBooleanExtra(ARG_IS_FROM_THEMES, true)
        pictureName = intent.getStringExtra(ARG_PICTURE_NAME)
        picturePath = intent.getStringExtra(ARG_PICTURE_PATH)

        presenter = PaintPresenter(this)
        tipDialog = TipDialog.newInstance()
        myDialogFactory = MyDialogFactory(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        loadPicture()
    }

    override fun onDestroy() {
        super.onDestroy()
        civColoring.onRecycleBitmaps()
    }

    private fun loadPicture() {
        tipDialog.showTip(supportFragmentManager, getString(R.string.loadpicture))
        PicassoUtils.displayImage(this, civColoring, picturePath, object : SimpleTarget() {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom) {
                tipDialog.dismiss()
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                tipDialog.dismiss()
                Toast.makeText(this@PaintActivity, getString(R.string.loadpicturefailed), Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun initViews() {
        undo.isEnabled = false
        redo.isEnabled = false
        ivToggleActionBar.isSelected = true

        initBottomColors()

        undo.setOnClickListener(this)
        redo.setOnClickListener(this)
        tvTogglePalette.setOnClickListener(this)
        ivToggleActionBar.setOnClickListener(this)
        tvAfterEffect.setOnClickListener(this)

        rbPickColor.setOnCheckedChangeListener(this)
        jianbian_color.setOnCheckedChangeListener(this)

        cpPaletteColorPicker.setOnChangedListener(object : ColorPicker.OnColorChangedListener {
            override fun colorChangedListener(color: Int) {
                changeCurrentColor(color)
            }
        })

        civColoring.setOnRedoUndoListener(object : ColourImageView.OnRedoUndoListener {
            override fun onRedoUndo(undoSize: Int, redoSize: Int) {
                undo.isEnabled = undoSize != 0
                redo.isEnabled = redoSize != 0
            }
        })
    }

    private fun initBottomColors() {
        //paintColors
        val adapter = ColorOptionAdapter(this)
        adapter.setColorSelectedListener(OnDelegateClickListener { _, color ->
            cpPaletteColorPicker.color = color
            changeCurrentColor(color)
        })
        rvColors.setHasFixedSize(true)
        rvColors.adapter = adapter

        //如果直接paintColors.subList(0, 8)的话，由于Int是对象类型会造成数据影响
        pickedColorAdapter = PickedColorAdapter(this)
        pickedColorAdapter.setPickColorListener(OnDelegateClickListener { view, newColor -> changeCurrentColor(newColor) })

        rvPickedColors.setHasFixedSize(true)
        rvPickedColors.adapter = pickedColorAdapter

        //初始化选中的颜色,especial for picker palette
        changeCurrentColor(pickedColorAdapter.getPickedColor())
    }

    private fun onPickColorCheckChanged(isChecked: Boolean) {
        if (!isChecked) {
            backToColorModel()
            return
        }

        myDialogFactory.showPickColorHintDialog()
        civColoring.model = ColourImageView.Model.PICKCOLOR
        civColoring.setOnColorPickListener(object : ColourImageView.OnColorPickListener {
            override fun onColorPick(status: Boolean, color: Int) {
                if (status == true) {
                    changeCurrentColor(color)
                    rbPickColor.isChecked = false
                } else {
                    Toast.makeText(this@PaintActivity, getString(R.string.pickcolorerror), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onJianBianColorCheckChanged(checked: Boolean) {
        if (checked) {
            myDialogFactory.showGradualHintDialog()
            civColoring.model = ColourImageView.Model.FILLGRADUALCOLOR
            jianbian_color.setText(R.string.jianbian_color)
        } else {
            civColoring.model = ColourImageView.Model.FILLCOLOR
            jianbian_color.setText(R.string.normal_color)
        }
    }

    override fun onCheckedChanged(view: CompoundButton, checked: Boolean) {
        val viewId = view.id
        when (viewId) {
            R.id.rbPickColor -> onPickColorCheckChanged(checked)
            R.id.jianbian_color -> onJianBianColorCheckChanged(checked)
        }
    }

    override fun onClick(view: View) {
        val viewId = view.id
        when (viewId) {
            R.id.undo -> civColoring.undo()
            R.id.redo -> civColoring.redo()
            R.id.tvAfterEffect -> saveToLocal(FLAG_EFFECT)

            R.id.tvTogglePalette ->
                cpPaletteColorPicker.visibility =
                        if (cpPaletteColorPicker.visibility == View.VISIBLE) View.GONE
                        else View.VISIBLE

            R.id.ivToggleActionBar -> {
                if (supportActionBar == null) return
                ivToggleActionBar.isSelected = !supportActionBar!!.isShowing
                if (ivToggleActionBar.isSelected) supportActionBar!!.show()
                else supportActionBar!!.hide()
            }
        }
    }

    private fun backToColorModel() {
        civColoring.model = ColourImageView.Model.FILLCOLOR
        jianbian_color.isChecked = false
    }

    private fun changeCurrentColor(newColor: Int) {
        rbPickColor.isChecked = false
        pickedColorAdapter.updateColor(newColor)

        cpPaletteColorPicker.color = newColor
        civColoring.setColor(newColor)
    }

    private fun saveToLocal(saveFlag: Int) {
        tipDialog.showTip(supportFragmentManager, getString(R.string.savingimage))

        val picName =
                if (isFromThemes) pictureName.replace(".png", "_") + System.currentTimeMillis() + ".png"
                else pictureName

        presenter.saveImageLocally(civColoring.getBitmap()!!, picName,
                object : PaintPresenter.OnSaveImageListener {
                    override fun onSaved(path: String) {
                        tipDialog.dismiss()
                        if (TextUtils.isEmpty(path)) {
                            Toast.makeText(this@PaintActivity, getString(R.string.saveFailed), Toast.LENGTH_SHORT).show()
                            return
                        }
                        Toast.makeText(this@PaintActivity, getString(R.string.saveSuccess) + path, Toast.LENGTH_SHORT).show()
                        if (saveFlag == FLAG_EXIT)
                            finish()
                        else if (saveFlag == FLAG_SHARE)
                            ShareImageUtil.getInstance(this@PaintActivity).shareImg(path)
                        else if (saveFlag == FLAG_EFFECT)
                            showEffectDialog(path)
                    }
                })
    }

    private fun showEffectDialog(path: String) {
        val dialog = AfterEffectDialog.newInstance(path)
        dialog.setEffectListener(object : AfterEffectDialog.OnCompleteEffectListener {
            override fun onEffectCompleted(effectBitmap: Bitmap) {
                Toast.makeText(this@PaintActivity, "ok", Toast.LENGTH_SHORT).show()
            }
        })
        AfterEffectDialog.newInstance(path).show(supportFragmentManager, "")
    }

    private fun repaint() {
        if (isFromThemes) {
            if (FileUtils.deleteFile(picturePath)) finish()
            else Toast.makeText(this, getString(R.string.deletePaintFailed), Toast.LENGTH_SHORT).show()
            return
        }
        tipDialog.showTip(supportFragmentManager, getString(R.string.loadpicture))
        civColoring.clearStack()

        PicassoUtils.displayImage(this, civColoring, picturePath, object : SimpleTarget() {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom) {
                tipDialog.dismiss()
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                tipDialog.dismiss()
                Toast.makeText(this@PaintActivity, getString(R.string.loadpicturefailed), Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_paint, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.menuSave -> saveToLocal(FLAG_SAVE)
            R.id.menuShare -> saveToLocal(FLAG_SHARE)
            R.id.menuHelp -> Toast.makeText(this, "help", Toast.LENGTH_SHORT).show()
            R.id.menuDelete ->
                myDialogFactory.showRepaintDialog(View.OnClickListener {
                    myDialogFactory.dismissDialog()
                    repaint()
                })
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        pickedColorAdapter.savePickedColors()
    }

    override fun onBackPressed() {
        if (!civColoring.isUndoable()) {
            super.onBackPressed()
            return
        }
        myDialogFactory.showExitPaintDialog(
                View.OnClickListener { saveToLocal(FLAG_EXIT) },
                View.OnClickListener {
                    myDialogFactory.dismissDialog()
                    finish()
                })
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == Constants.REQ_ADVANCED_PAINT && resultCode == Constants.REPAINT_RESULT) {
//            repaint()
//        }
//    }

    companion object {
        private val ARG_IS_FROM_THEMES = "arg_is_from_themes"
        private val ARG_PICTURE_NAME = "arg_picture_name"
        private val ARG_PICTURE_PATH = "arg_picture_path"
        private val FLAG_SAVE = 0
        private val FLAG_EXIT = 1
        private val FLAG_SHARE = 2
        private val FLAG_EFFECT = 3

        fun start(context: Context, isFromThemes: Boolean, pictureName: String, picturePath: String) {
            val intent = Intent(context, PaintActivity::class.java)
            intent.putExtra(ARG_IS_FROM_THEMES, isFromThemes)
            intent.putExtra(ARG_PICTURE_NAME, pictureName)
            intent.putExtra(ARG_PICTURE_PATH, picturePath)
            context.startActivity(intent)
        }
    }
}