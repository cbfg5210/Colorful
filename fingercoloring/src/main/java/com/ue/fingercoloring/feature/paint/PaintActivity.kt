package com.ue.fingercoloring.feature.paint

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import com.ue.fingercoloring.R
import com.ue.fingercoloring.constant.Constants
import com.ue.fingercoloring.constant.SPKeys
import com.ue.fingercoloring.factory.MyDialogFactory
import com.ue.fingercoloring.listener.OnDrawLineListener
import com.ue.fingercoloring.listener.SimpleTarget
import com.ue.fingercoloring.util.FileUtils
import com.ue.fingercoloring.util.PicassoUtils
import com.ue.fingercoloring.util.SPUtils
import com.ue.fingercoloring.util.ShareImageUtil
import com.ue.fingercoloring.view.ColorPicker
import com.ue.fingercoloring.view.ColourImageView
import com.ue.fingercoloring.view.TipDialog
import kotlinx.android.synthetic.main.activity_paint.*
import kotlinx.android.synthetic.main.dialog_coloradvance.*
import kotlinx.android.synthetic.main.view_colorpicker.*
import kotlinx.android.synthetic.main.view_dialog_secondlay.*


class PaintActivity : AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var isFromThemes: Boolean = false
    private var picturePath: String = ""
    private var pictureName: String = ""

    private lateinit var myDialogFactory: MyDialogFactory
    private lateinit var presenter: PaintPresenter
    private lateinit var tipDialog: TipDialog

    private var currentColor: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint)

        val intent = intent
        isFromThemes = intent.getBooleanExtra(ARG_IS_FROM_THEMES, true)
        pictureName = intent.getStringExtra(ARG_PICTURE_NAME)
        picturePath = intent.getStringExtra(ARG_PICTURE_PATH)

        presenter = PaintPresenter(this)
        tipDialog = TipDialog.newInstance()

        initViews()
        loadPicture()
    }

    override fun onPause() {
        super.onPause()
        saveIVColors()
    }

    override fun onDestroy() {
        super.onDestroy()
        fillImageview.onRecycleBitmaps()
    }

    private fun loadPicture() {
        tipDialog.showTip(supportFragmentManager, getString(R.string.loadpicture))
        PicassoUtils.displayImage(this, fillImageview, picturePath, object : SimpleTarget() {
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
        myDialogFactory = MyDialogFactory(this)

        undo.isEnabled = false
        redo.isEnabled = false

        initPens()
        initBottomColorPanel()

        undo.setOnClickListener(this)
        redo.setOnClickListener(this)
        save.setOnClickListener(this)
        share.setOnClickListener(this)
        delete.setOnClickListener(this)
        more.setOnClickListener(this)

        pickcolor.setOnCheckedChangeListener(this)
        drawline.setOnCheckedChangeListener(this)
        jianbian_color.setOnCheckedChangeListener(this)

        currentColor = current_pen1
        resetIVColors()

        changeCurrentColor(currentColor!!)
        seekcolorpicker.setOnChangedListener(object : ColorPicker.OnColorChangedListener {
            override fun colorChangedListener(color: Int) {
                changeCurrentColor(color)
            }
        })

        seekcolorpicker.color = ContextCompat.getColor(this, R.color.maincolor)
        fillImageview.setOnRedoUndoListener(object : ColourImageView.OnRedoUndoListener {
            override fun onRedoUndo(undoSize: Int, redoSize: Int) {
                undo.isEnabled = undoSize != 0
                redo.isEnabled = redoSize != 0
            }
        })
    }

    private fun onPickColorCheckChanged(isChecked: Boolean) {
        if (!isChecked) {
            backToColorModel()
            return
        }

        drawline.isChecked = false
        myDialogFactory.showPickColorHintDialog()
        fillImageview.model = ColourImageView.Model.PICKCOLOR
        fillImageview.setOnColorPickListener(object : ColourImageView.OnColorPickListener {
            override fun onColorPick(status: Boolean, color: Int) {
                if (status == true) {
                    changeCurrentColor(color)
                    pickcolor.isChecked = false
                } else {
                    Toast.makeText(this@PaintActivity, getString(R.string.pickcolorerror), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onDrawLineCheckChanged(checked: Boolean) {
        if (!checked) {
            fillImageview.clearPoints()
            backToColorModel()
            return
        }

        pickcolor.isChecked = false
        myDialogFactory.showBuxianButtonClickDialog()
        fillImageview.model = ColourImageView.Model.DRAW_LINE
        fillImageview.setOnDrawLineListener(object : OnDrawLineListener {
            override fun OnDrawFinishedListener(drawed: Boolean, startX: Int, startY: Int, endX: Int, endY: Int) {
                if (drawed)
                    myDialogFactory.showBuxianNextPointSetDialog()
                else
                    Toast.makeText(this@PaintActivity, getString(R.string.drawLineHint_finish), Toast.LENGTH_SHORT).show()
            }

            override fun OnGivenFirstPointListener(startX: Int, startY: Int) {
                myDialogFactory.showBuxianFirstPointSetDialog()
            }

            override fun OnGivenNextPointListener(endX: Int, endY: Int) {}
        })
    }

    private fun onJianBianColorCheckChanged(checked: Boolean) {
        if (checked) {
            myDialogFactory.showGradualHintDialog()
            fillImageview.model = ColourImageView.Model.FILLGRADUALCOLOR
            jianbian_color.setText(R.string.jianbian_color)
        } else {
            fillImageview.model = ColourImageView.Model.FILLCOLOR
            jianbian_color.setText(R.string.normal_color)
        }
    }

    override fun onCheckedChanged(view: CompoundButton, checked: Boolean) {
        val viewId = view.id
        when (viewId) {
            R.id.pickcolor -> onPickColorCheckChanged(checked)
            R.id.drawline -> onDrawLineCheckChanged(checked)
            R.id.jianbian_color -> onJianBianColorCheckChanged(checked)
        }
    }

    override fun onClick(view: View) {
        val viewId = view.id
        when (viewId) {
            R.id.undo -> fillImageview.undo()
            R.id.redo -> fillImageview.redo()
            R.id.save -> onSaveClicked()
            R.id.share -> shareImage()
            R.id.more -> gotoAdvancePaintActivity()

            R.id.delete -> myDialogFactory.showRepaintDialog(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    myDialogFactory.dismissDialog()
                    repaint()
                }
            })
        }
    }

    private fun onSaveClicked() {
        tipDialog.showTip(supportFragmentManager, getString(R.string.savingimage))

        saveImageLocally(object : PaintPresenter.OnSaveImageListener {
            override fun onSaved(path: String) {
                tipDialog.dismiss()
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(this@PaintActivity, getString(R.string.saveFailed), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@PaintActivity, getString(R.string.saveSuccess) + path, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initPens() {
        val checkCurrentColor = View.OnClickListener { view ->
            current_pen1.setBackgroundResource(R.drawable.white_bg)
            current_pen2.setBackgroundResource(R.drawable.white_bg)
            current_pen3.setBackgroundResource(R.drawable.white_bg)
            current_pen4.setBackgroundResource(R.drawable.white_bg)
            view.setBackgroundResource(R.drawable.main_bg)

            currentColor = view as ImageView
            changeCurrentColor(view)
        }

        current_pen1.setOnClickListener(checkCurrentColor)
        current_pen2.setOnClickListener(checkCurrentColor)
        current_pen3.setOnClickListener(checkCurrentColor)
        current_pen4.setOnClickListener(checkCurrentColor)
    }

    private fun initBottomColorPanel() {
        val colorListener = View.OnClickListener { view ->
            val color = (view.background as ColorDrawable).color
            seekcolorpicker.color = color
            changeCurrentColor(color)
        }

        for (i in 0 until colortable.childCount) {
            for (j in 0 until (colortable.getChildAt(i) as TableRow).childCount) {
                if ((colortable.getChildAt(i) as TableRow).getChildAt(j) is Button) {
                    (colortable.getChildAt(i) as TableRow).getChildAt(j).setOnClickListener(colorListener)
                }
            }
        }
    }

    private fun backToColorModel() {
        fillImageview.model = ColourImageView.Model.FILLCOLOR
        jianbian_color.isChecked = false
    }

    private fun gotoAdvancePaintActivity() {
        tipDialog.showTip(supportFragmentManager, getString(R.string.savingimage))

        saveImageLocally(object : PaintPresenter.OnSaveImageListener {
            override fun onSaved(path: String) {
                tipDialog.dismiss()
                AdvancePaintActivity.startForResult(this@PaintActivity, path)
            }
        })
    }

    private fun saveImageLocally(listener: PaintPresenter.OnSaveImageListener) {
        val picName = if (isFromThemes) pictureName.replace(".png", "_") + System.currentTimeMillis() + ".png" else pictureName
        presenter.saveImageLocally(fillImageview.getBitmap()!!, picName, listener)
    }

    private fun shareImage() {
        tipDialog.showTip(supportFragmentManager, getString(R.string.savingimage))

        saveImageLocally(object : PaintPresenter.OnSaveImageListener {
            override fun onSaved(path: String) {
                tipDialog.dismiss()
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(this@PaintActivity, getString(R.string.saveFailed), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@PaintActivity, getString(R.string.saveSuccess) + path, Toast.LENGTH_SHORT).show()
                    ShareImageUtil.getInstance(this@PaintActivity).shareImg(path)
                }
            }
        })
    }

    private fun changeCurrentColor(color: Int) {
        setFillColorModel()
        fillImageview.setColor(color)
        currentColor!!.setImageDrawable(ColorDrawable(color))
    }

    private fun setFillColorModel() {
        pickcolor.isChecked = false
        drawline.isChecked = false
    }

    private fun changeCurrentColor(currentColor: ImageView) {
        setFillColorModel()
        seekcolorpicker.color = (currentColor.drawable as ColorDrawable).color
        fillImageview.setColor((currentColor.drawable as ColorDrawable).color)
    }

    private fun saveToLocalAndExit() {
        tipDialog.showTip(supportFragmentManager, getString(R.string.savingimage))

        saveImageLocally(object : PaintPresenter.OnSaveImageListener {
            override fun onSaved(path: String) {
                tipDialog.dismiss()
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(this@PaintActivity, getString(R.string.saveFailed), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@PaintActivity, getString(R.string.saveSuccess) + path, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })
    }

    private fun repaint() {
        if (isFromThemes) {
            if (FileUtils.deleteFile(picturePath)) {
                finish()
            } else {
                Toast.makeText(this, getString(R.string.deletePaintFailed), Toast.LENGTH_SHORT).show()
            }
        } else {
            tipDialog.showTip(supportFragmentManager, getString(R.string.loadpicture))
            fillImageview.clearStack()

            PicassoUtils.displayImage(this, fillImageview, picturePath, object : SimpleTarget() {
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
    }

    private fun saveIVColors() {
        saveIVColor(current_pen1, SPKeys.SavedColor1)
        saveIVColor(current_pen2, SPKeys.SavedColor2)
        saveIVColor(current_pen3, SPKeys.SavedColor3)
        saveIVColor(current_pen4, SPKeys.SavedColor4)
    }

    private fun saveIVColor(iv: ImageView, key: String) {
        SPUtils.putInt(key, (iv.drawable as ColorDrawable).color)
    }

    private fun resetIVColors() {
        resetIVColor(current_pen1, SPKeys.SavedColor1, R.color.red)
        resetIVColor(current_pen2, SPKeys.SavedColor2, R.color.yellow)
        resetIVColor(current_pen3, SPKeys.SavedColor3, R.color.skyblue)
        resetIVColor(current_pen4, SPKeys.SavedColor4, R.color.green)
    }

    private fun resetIVColor(iv: ImageView, key: String, defColorRes: Int) {
        iv.setImageDrawable(ColorDrawable(SPUtils.getInt(key, ContextCompat.getColor(this, defColorRes))))
    }

    override fun onBackPressed() {
        if (!fillImageview.isUndoable()) {
            super.onBackPressed()
            return;
        }
        myDialogFactory.FinishSaveImageDialog(
                View.OnClickListener { saveToLocalAndExit() },
                View.OnClickListener {
                    myDialogFactory.dismissDialog()
                    finish()
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQ_ADVANCED_PAINT && resultCode == Constants.REPAINT_RESULT) {
            repaint()
        }
    }

    companion object {
        private val ARG_IS_FROM_THEMES = "arg_is_from_themes"
        private val ARG_PICTURE_NAME = "arg_picture_name"
        private val ARG_PICTURE_PATH = "arg_picture_path"

        fun start(context: Context, isFromThemes: Boolean, pictureName: String, picturePath: String) {
            val intent = Intent(context, PaintActivity::class.java)
            intent.putExtra(ARG_IS_FROM_THEMES, isFromThemes)
            intent.putExtra(ARG_PICTURE_NAME, pictureName)
            intent.putExtra(ARG_PICTURE_PATH, picturePath)
            context.startActivity(intent)
        }
    }
}