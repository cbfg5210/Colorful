package com.ue.colorful.feature.pickphoto

import android.os.Bundle
import android.view.*
import com.ue.colorful.R
import com.ue.colorful.event.ColorListener
import com.ue.colorful.feature.main.BasePickerFragment
import kotlinx.android.synthetic.main.fragment_photo_picker.*
import kotlinx.android.synthetic.main.fragment_photo_picker.view.*

class PhotoPickerFragment : BasePickerFragment(R.layout.fragment_photo_picker) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        rootView.pcpPhotoColorPicker.setColorListener(object : ColorListener {
            override fun onColorSelected(color: Int) {
                rootView.tvHex.text = "#${String.format("%06X", 0xFFFFFF and color)}"
                rootView.vColorEffect.setBackgroundColor(color)
            }
        })

        val directionListener = object : View.OnClickListener {
            override fun onClick(v: View) {
                when (v.id) {
                    R.id.ivThumbToggle -> {
                        rootView.ivThumbToggle.isSelected = !ivThumbToggle.isSelected
                        rootView.pcpPhotoColorPicker.toggleThumbColor()
                    }
                    R.id.ivMoveUp -> rootView.pcpPhotoColorPicker.offsetXY(0F, -3F)
                    R.id.ivMoveDown -> rootView.pcpPhotoColorPicker.offsetXY(0F, 3F)
                    R.id.ivMoveLeft -> rootView.pcpPhotoColorPicker.offsetXY(-3F, 0F)
                    R.id.ivMoveRight -> rootView.pcpPhotoColorPicker.offsetXY(3F, 0F)
                }
            }
        }
        rootView.ivThumbToggle.setOnClickListener(directionListener)
        rootView.ivMoveUp.setOnClickListener(directionListener)
        rootView.ivMoveDown.setOnClickListener(directionListener)
        rootView.ivMoveLeft.setOnClickListener(directionListener)
        rootView.ivMoveRight.setOnClickListener(directionListener)

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