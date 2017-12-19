package com.ue.colorful.feature.picker.argb

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatSeekBar
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import com.ue.colorful.R
import com.ue.colorful.feature.main.BaseFragment
import kotlinx.android.synthetic.main.fragment_argb_picker.view.*

/**
 * Created by hawk on 2017/10/12.
 */

class ARGBPickerFragment : BaseFragment(R.layout.fragment_argb_picker, R.menu.menu_palette), SeekBar.OnSeekBarChangeListener {
    internal lateinit var ivColor: AppCompatImageView
    internal lateinit var etHex: AppCompatEditText
    internal lateinit var etA: EditText
    internal lateinit var etR: EditText
    internal lateinit var etG: EditText
    internal lateinit var etB: EditText
    internal lateinit var seekBarA: AppCompatSeekBar
    internal lateinit var seekBarR: AppCompatSeekBar
    internal lateinit var seekBarG: AppCompatSeekBar
    internal lateinit var seekBarB: AppCompatSeekBar

    private var changeHex: Boolean = false
    private var changeA: Boolean = false
    private var changeR: Boolean = false
    private var changeG: Boolean = false
    private var changeB: Boolean = false
    private var changeProgress: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        ivColor = rootView.ivColor
        etHex = rootView.etHex
        etA = rootView.etA
        etR = rootView.etR
        etG = rootView.etG
        etB = rootView.etB
        seekBarA = rootView.seekBarA
        seekBarR = rootView.seekBarR
        seekBarG = rootView.seekBarG
        seekBarB = rootView.seekBarB

        //输入字母转为大写
        etHex.filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(8))
        etA.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
        etR.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
        etG.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))
        etB.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(3))

        etHex.addTextChangedListener(getTextWatcher(FLAG_HEX))
        etA.addTextChangedListener(getTextWatcher(FLAG_A))
        etR.addTextChangedListener(getTextWatcher(FLAG_R))
        etG.addTextChangedListener(getTextWatcher(FLAG_G))
        etB.addTextChangedListener(getTextWatcher(FLAG_B))

        seekBarA.setOnSeekBarChangeListener(this)
        seekBarR.setOnSeekBarChangeListener(this)
        seekBarG.setOnSeekBarChangeListener(this)
        seekBarB.setOnSeekBarChangeListener(this)

        changeHex = true
        etHex.setText("FF000000")

        rootView.ivAddColor.setOnClickListener({ containerCallbck?.addPaletteColor(getColorInt()) })
        rootView.ivCopy.setOnClickListener({ containerCallbck?.copyColor(getColorInt()) })

        return rootView
    }

    private fun getTextWatcher(etFlag: Int): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //hex
                if (etFlag == FLAG_HEX) {
                    onHexChanged(charSequence.toString())
                    return
                }
                //rgb
                onRGBTextChanged(etFlag)
            }
        }
    }

    private fun onRGBTextChanged(etFlag: Int) {
        when (etFlag) {
            FLAG_A -> if (changeA) seekBarA.progress = getCheckedVal(etA) else changeA = true
            FLAG_R -> if (changeR) seekBarR.progress = getCheckedVal(etR) else changeR = true
            FLAG_G -> if (changeG) seekBarG.progress = getCheckedVal(etG) else changeG = true
            FLAG_B -> if (changeB) seekBarB.progress = getCheckedVal(etB) else changeB = true
        }

        resetStatus(true)
        changeHex = false
        val colorInt = getColorInt()
        etHex.setText(String.format("%08X", colorInt))
        ivColor.setBackgroundColor(colorInt)
    }

    private fun onARGBProgressChanged(etFlag: Int) {
        resetStatus(false)

        when (etFlag) {
            FLAG_A -> etA.setText(seekBarA.progress.toString())
            FLAG_R -> etR.setText(seekBarR.progress.toString())
            FLAG_G -> etG.setText(seekBarG.progress.toString())
            FLAG_B -> etB.setText(seekBarB.progress.toString())
        }

        val colorInt = getColorInt()
        etHex.setText(String.format("%08X", colorInt))
        ivColor.setBackgroundColor(colorInt)
    }

    private fun onHexChanged(tCharSequence: String) {
        //如果是由于rgb改变而改变则不用处理自身TextChange事件
        if (!changeHex) {
            changeHex = true
            return
        }
        var charSequence = tCharSequence
        if (charSequence.length < 8) {
            charSequence = "00000000$charSequence"
            charSequence = charSequence.substring(charSequence.length - 8)
        }
        resetStatus(false)
        changeHex = true

        val colorInt = Color.parseColor("#${charSequence}")
        val aVal = Color.alpha(colorInt)
        val rVal = Color.red(colorInt)
        val gVal = Color.green(colorInt)
        val bVal = Color.blue(colorInt)

        etA.setText(aVal.toString())
        etR.setText(rVal.toString())
        etG.setText(gVal.toString())
        etB.setText(bVal.toString())
        seekBarA.progress = aVal
        seekBarR.progress = rVal
        seekBarG.progress = gVal
        seekBarB.progress = bVal

        ivColor.setBackgroundColor(colorInt)
    }

    fun getColorInt(): Int {
        return Color.argb(getCheckedVal(etA), getCheckedVal(etR), getCheckedVal(etG), getCheckedVal(etB))
    }

    private fun getCheckedVal(valueEt: EditText): Int {
        val value = valueEt.text.toString()
        val tempStr = if (value.isEmpty()) "0" else value
        val tempInt = tempStr.toInt()
        return if (tempInt > 255) 255 else tempInt
    }

    private fun resetStatus(status: Boolean) {
        changeHex = status
        changeA = status
        changeR = status
        changeG = status
        changeB = status
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
        if (!changeProgress) {
            return
        }
        when (seekBar.id) {
            R.id.seekBarA -> onARGBProgressChanged(FLAG_A)
            R.id.seekBarR -> onARGBProgressChanged(FLAG_R)
            R.id.seekBarG -> onARGBProgressChanged(FLAG_G)
            R.id.seekBarB -> onARGBProgressChanged(FLAG_B)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        resetStatus(false)
        changeProgress = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        changeProgress = false
    }

    companion object {
        private val FLAG_HEX = 0x1
        private val FLAG_A = 0x2
        private val FLAG_R = 0x3
        private val FLAG_G = 0x4
        private val FLAG_B = 0x5
    }
}
