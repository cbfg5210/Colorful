package com.ue.colorful.feature.pickphoto

import android.graphics.Color
import android.os.Bundle
import android.view.*
import com.ue.colorful.R
import com.ue.colorful.event.ColorListener
import com.ue.colorful.feature.main.BasePickerFragment
import kotlinx.android.synthetic.main.fragment_photo_picker.view.*
import kotlinx.android.synthetic.main.layout_common_picker.view.*

class PhotoPickerFragment : BasePickerFragment(R.layout.fragment_photo_picker) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        rootView.pcpPhotoColorPicker.setColorListener(object : ColorListener {
            override fun onColorSelected(color: Int) {
                rootView.ivColorEffect.setBackgroundColor(color)
                rootView.tvColorHex.text =
                        if (color == Color.TRANSPARENT) "#${String.format("%08X", color)}"
                        else "#${String.format("%06X", 0xFFFFFF and color)}"
            }
        })

        rootView.ivAddColor.setOnClickListener(pickerListener)
        rootView.ivCopy.setOnClickListener(pickerListener)

        return rootView
    }

    override fun getColorInt(): Int {
        return rootView.pcpPhotoColorPicker.color
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_photo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuAlbum) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}