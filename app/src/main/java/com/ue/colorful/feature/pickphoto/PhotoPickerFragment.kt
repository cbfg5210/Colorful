package com.ue.colorful.feature.pickphoto

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.event.ColorListener
import com.ue.colorful.feature.main.BasePickerFragment
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.fragment_photo_picker.view.*
import kotlinx.android.synthetic.main.layout_common_picker.view.*


class PhotoPickerFragment : BasePickerFragment(R.layout.fragment_photo_picker) {
    private val REQ_PICK_PHOTO = 11

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

        val pickerPhotoPath = SPUtils.getString(SPKeys.PICKER_PHOTO_PATH, "")
        if (TextUtils.isEmpty(pickerPhotoPath)) Toast.makeText(activity, R.string.pick_photo, Toast.LENGTH_LONG).show()
        else loadPhoto(pickerPhotoPath)

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
            startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), getString(R.string.choose_photo)), REQ_PICK_PHOTO)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != REQ_PICK_PHOTO) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        val photoPath = if (TextUtils.isEmpty(data?.dataString)) "" else data!!.dataString
        SPUtils.putString(SPKeys.PICKER_PHOTO_PATH, photoPath)

        loadPhoto(photoPath)
    }

    private fun loadPhoto(photoPath: String) {
        if (rootView.pcpPhotoColorPicker.tag == null || !(rootView.pcpPhotoColorPicker.tag is Target)) {
            //picasso对target是弱引用,容易被销毁,这是为了保存target
            rootView.pcpPhotoColorPicker.tag = object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {
                    Toast.makeText(activity, R.string.load_photo_failed, Toast.LENGTH_SHORT).show()
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    rootView.pcpPhotoColorPicker.setPhoto(bitmap)
                }
            }
        }
        Picasso.with(activity)
                .load(photoPath)
                .into(rootView.pcpPhotoColorPicker.tag as Target)
    }
}