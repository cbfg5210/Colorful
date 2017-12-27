package com.ue.fingercoloring.factory

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.ue.fingercoloring.R
import com.ue.fingercoloring.constant.SPKeys
import com.ue.fingercoloring.listener.OnAddWordsSuccessListener
import com.ue.fingercoloring.listener.OnChangeBorderListener
import com.ue.fingercoloring.util.DensityUtil
import com.ue.fingercoloring.util.SPUtils
import com.ue.fingercoloring.view.ColorPickerSeekBar
import com.ue.fingercoloring.view.MyDialogStyle

/**
 * Created by Swifty.Wang on 2015/6/12.
 */
class MyDialogFactory(context: Context) : MyDialogStyle(context) {

    //just for add border
    internal var drawableid: Int = 0

    fun FinishSaveImageDialog(savelistener: View.OnClickListener, quitlistener: View.OnClickListener) {
        showTwoButtonDialog(context.getString(R.string.quitorsave), context.getString(R.string.save), context.getString(R.string.quit), savelistener, quitlistener, true)
    }

    fun showPaintFirstOpenDialog() {
        val buffer = StringBuffer()
        buffer.append(context.getString(R.string.paintHint))
        showOnceTimesContentDialog(context.getString(R.string.welcomeusethis), buffer, SPKeys.PaintHint)
    }

    fun showPaintFirstOpenSaveDialog() {
        val buffer = StringBuffer()
        buffer.append(context.getString(R.string.paintHint2))
        showOnceTimesContentDialog(context.getString(R.string.welcomeusethis), buffer, SPKeys.PaintHint2)
    }

    fun showAddWordsDialog(onAddWordsSuccessListener: OnAddWordsSuccessListener) {
        val layout = LayoutInflater.from(context).inflate(R.layout.view_addwords, null)
        val editText = layout.findViewById<View>(R.id.addeditwords) as EditText
        val radioGroup = layout.findViewById<View>(R.id.radiogroup) as RadioGroup
        radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.small -> editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().SmallTextSize.toFloat())
                R.id.middle -> editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().MiddleTextSize.toFloat())
                R.id.large -> editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().BigTextSize.toFloat())
                R.id.huge -> editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, DragTextViewFactory.getInstance().HugeSize.toFloat())
            }
        }
        val colorPicker = layout.findViewById<View>(R.id.seekcolorpicker) as ColorPickerSeekBar
        colorPicker.setOnColorSeekbarChangeListener(object : ColorPickerSeekBar.OnColorSeekBarChangeListener {
            override fun onColorChanged(seekBar: SeekBar, color: Int, b: Boolean) {
                editText.setTextColor(color)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        val listener = View.OnClickListener {
            if (!editText.text.toString().trim { it <= ' ' }.isEmpty()) {
                dismissDialog()
                onAddWordsSuccessListener.addWordsSuccess(DragTextViewFactory.getInstance().createUserWordTextView(context, editText.text.toString(), editText.currentTextColor, editText.textSize.toInt()))
            } else {
                Toast.makeText(context, context.getString(R.string.nowords), Toast.LENGTH_SHORT).show()
            }
        }
        showBlankDialog(context.getString(R.string.addtext), layout, listener)
    }

    fun showRepaintDialog(confirm: View.OnClickListener) {
        val buffer = StringBuffer()
        buffer.append(context.getString(R.string.confirmRepaint) + "\n")
        val listener2 = View.OnClickListener { dismissDialog() }
        showTwoButtonDialog(buffer, context.getString(R.string.repaint), context.getString(R.string.cancel), confirm, listener2, true)
    }

    fun showAddBorderDialog(onChangeBorderListener: OnChangeBorderListener) {
        val layout = LayoutInflater.from(context).inflate(R.layout.view_addborder, null)
        val border1 = layout.findViewById<View>(R.id.xiangkuang1) as ImageView
        val border2 = layout.findViewById<View>(R.id.xiangkuang2) as ImageView
        drawableid = 1
        val changeBorderOnclickListener = View.OnClickListener { view ->
            if (view.id == border1.id) {
                border1.setBackgroundResource(R.drawable.maincolor_border_bg)
                drawableid = 1
                border2.setBackgroundResource(0)
            } else {
                border2.setBackgroundResource(R.drawable.maincolor_border_bg)
                drawableid = 2
                border1.setBackgroundResource(0)
            }
        }
        border1.setOnClickListener(changeBorderOnclickListener)
        border2.setOnClickListener(changeBorderOnclickListener)
        val listener = View.OnClickListener {
            dismissDialog()
            if (drawableid == 1) {
                onChangeBorderListener.changeBorder(R.drawable.xiangkuang, DensityUtil.dip2px(context, 36f), DensityUtil.dip2px(context, 36f), DensityUtil.dip2px(context, 21f), DensityUtil.dip2px(context, 21f))
            } else {
                onChangeBorderListener.changeBorder(R.drawable.xiangkuang2, DensityUtil.dip2px(context, 16f), DensityUtil.dip2px(context, 16f), DensityUtil.dip2px(context, 16f), DensityUtil.dip2px(context, 16f))
            }
        }
        showBlankDialog(context.getString(R.string.addborder), layout, listener)
    }

    fun showBuxianButtonClickDialog() {
        showOnceTimesContentDialog(context.getString(R.string.buxianfunction), context.getString(R.string.buxianfunctionhint), SPKeys.BuXianButtonClickDialogEnable)
    }

    fun showBuxianFirstPointSetDialog() {
        showOnceTimesContentDialog(context.getString(R.string.buxianfunction), context.getString(R.string.buxianfirstpointsethint), SPKeys.BuXianFirstPointDialogEnable)
    }

    fun showBuxianNextPointSetDialog() {
        showOnceTimesContentDialog(context.getString(R.string.buxianfunction), context.getString(R.string.buxiannextpointsethint), SPKeys.BuXianNextPointDialogEnable)
    }

    fun showPickColorHintDialog() {
        showOnceTimesContentDialog(context.getString(R.string.pickcolor), context.getString(R.string.pickcolorhint), SPKeys.PickColorDialogEnable)
    }

    private fun showOnceTimesContentDialog(title: String, contentstr: CharSequence, whichDialog: String) {
        if (SPUtils.getBoolean(whichDialog, true)) {
            val layout = LayoutInflater.from(context).inflate(R.layout.view_dialog_with_checkbox, null)
            val content = layout.findViewById<View>(R.id.content) as TextView
            val checkBox = layout.findViewById<View>(R.id.checkbox_dontshow) as CheckBox
            content.text = contentstr
            val listener = View.OnClickListener {
                if (checkBox.isChecked) {
                    SPUtils.putBoolean(whichDialog, false)
                }
                dismissDialog()
            }
            showBlankDialog(title, layout, listener)
        }
    }

    fun showPaintHintDialog() {
        showPaintFirstOpenDialog()
        if (!dialog.isShowing) {
            showPaintFirstOpenSaveDialog()
        }
    }

    fun showGradualHintDialog() {
        showOnceTimesContentDialog(context.getString(R.string.gradualModel), context.getString(R.string.gradualModelHint), SPKeys.GradualModel)
    }
}
