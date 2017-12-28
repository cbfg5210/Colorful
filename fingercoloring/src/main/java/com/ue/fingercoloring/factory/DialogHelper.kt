package com.ue.fingercoloring.factory

import android.content.Context
import android.support.v7.app.AlertDialog
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
import kotlinx.android.synthetic.main.layout_check_box.view.*

/**
 * Created by Swifty.Wang on 2015/6/12.
 */
class DialogHelper(context: Context) : MyDialogStyle(context) {

    //just for add border
    internal var drawableid: Int = 0

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
        val colorPicker = layout.findViewById<View>(R.id.cpPaletteColorPicker) as ColorPickerSeekBar
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

    private fun showOnceHintDialog(titleRes: Int, hintRes: Int, positiveRes: Int, positiveListener: View.OnClickListener?, negativeRes: Int, checkedSpKey: String) {
        val showHint = SPUtils.getBoolean(checkedSpKey, true)
        if (!showHint) {
            positiveListener?.onClick(null)
            return
        }
        val checkBoxLayout = LayoutInflater.from(context).inflate(R.layout.layout_check_box, null)
        val negativeBtnTxt = if (negativeRes == 0) null else context.getString(negativeRes)

        AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setMessage(hintRes)
                .setPositiveButton(positiveRes) { _, which ->
                    if (checkBoxLayout.cbCheck.isChecked) SPUtils.putBoolean(checkedSpKey, false)
                    positiveListener?.onClick(null)
                }
                .setNegativeButton(negativeBtnTxt, null)
                .setView(checkBoxLayout)
                .create()
                .show()
    }

    fun showEffectHintDialog(listener: View.OnClickListener?) {
        showOnceHintDialog(R.string.after_effect, R.string.effect_hint, R.string.go_on, listener, R.string.cancel, SPKeys.SHOW_EFFECT_HINT)
    }

    fun showPickColorHintDialog() {
        showOnceHintDialog(R.string.pickcolor, R.string.pickcolorhint, R.string.got_it, null, 0, SPKeys.PickColorDialogEnable)
    }

    fun showGradualHintDialog() {
        showOnceHintDialog(R.string.gradualModel, R.string.gradualModelHint, R.string.got_it, null, 0, SPKeys.GradualModel)
    }

    fun showEnterHintDialog() {
        showOnceHintDialog(R.string.finger_coloring, R.string.paintHint, R.string.got_it, null, 0, SPKeys.SHOW_ENTER_HINT)
    }

    fun showExitPaintDialog(saveListener: View.OnClickListener?, quitListener: View.OnClickListener?) {
        AlertDialog.Builder(context)
                .setTitle(R.string.is_exit)
                .setMessage(R.string.quitorsave)
                .setPositiveButton(R.string.cancel, null)
                .setNegativeButton(R.string.save_exit) { _, _ -> saveListener?.onClick(null) }
                .setNeutralButton(R.string.quit_exit) { _, _ -> quitListener?.onClick(null) }
                .create()
                .show()
    }
}
