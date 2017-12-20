package com.ue.colorful.feature.main

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.ue.colorful.R
import kotlinx.android.synthetic.main.dialog_color_palette.view.*

/**
 * Created by hawk on 2017/12/14.
 */
class ColorPaletteDialog : DialogFragment() {
    private lateinit var paletteColors: ArrayList<Int>

    companion object {
        private val ARG_PALETTE_COLORS = "arg_palette_colors"

        fun newInstance(paletteColors: ArrayList<Int>): ColorPaletteDialog {
            val dialog = ColorPaletteDialog()
            val arguments = Bundle()
            arguments.putIntegerArrayList(ARG_PALETTE_COLORS, paletteColors)
            dialog.arguments = arguments
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paletteColors = arguments.getIntegerArrayList(ARG_PALETTE_COLORS)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_color_palette, null)
        val adapter = ColorPaletteAdapter(activity, paletteColors)

        rootView.rvPaletteColors.setHasFixedSize(true)
        rootView.rvPaletteColors.adapter = adapter

        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.btnClear -> {
                    (rootView.rvPaletteColors.adapter as ColorPaletteAdapter).clear()
                    dismiss()
                }
                R.id.btnRemove -> adapter.removeSelection()
                R.id.btnCopy -> adapter.copyColor()
            }
        }

        rootView.btnClear.setOnClickListener(listener)
        rootView.btnRemove.setOnClickListener(listener)
        rootView.btnCopy.setOnClickListener(listener)

        return AlertDialog.Builder(context)
                .setTitle(R.string.color_collections)
                .setView(rootView)
                .create()
    }
}