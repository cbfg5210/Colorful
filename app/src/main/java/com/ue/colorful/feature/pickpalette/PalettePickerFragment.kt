package com.ue.colorful.feature.pickpalette

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.event.AddPaletteColorEvent
import com.ue.colorful.event.ShowPaletteEvent
import com.ue.colorful.util.SPUtils
import com.ue.colorful.widget.PaletteColorPickerView
import kotlinx.android.synthetic.main.fragment_palette_picker.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by hawk on 2017/12/14.
 */
class PalettePickerFragment : Fragment(), PaletteColorPickerView.OnColorChangedListener, View.OnClickListener {
    private lateinit var rootView: View

    private val mClipboardManager: ClipboardManager by lazy {
        activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        rootView = inflater.inflate(R.layout.fragment_palette_picker, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialColor = SPUtils.getInt(SPKeys.LAST_PALETTE_COLOR, Color.BLACK)

        rootView.paletteColorPicker.setOnColorChangedListener(this)
        rootView.paletteColorPicker.setColor(initialColor, true)

        rootView.ivAddColor.setOnClickListener(this)
        rootView.ivCopy.setOnClickListener(this)

        onColorChanged(initialColor)
    }

    override fun onColorChanged(newColor: Int) {
        rootView.ivColorEffect.setBackgroundColor(newColor)
        rootView.tvColorHex.text = String.format("#%08X", newColor)
    }

    override fun onClick(view: View) {
        val hex = rootView.tvColorHex.text.toString()

        if (view.id == R.id.ivCopy) {
            val clip = ClipData.newPlainText("copy", hex)
            mClipboardManager.primaryClip = clip

            Toast.makeText(activity, activity.getString(R.string.color_copied, hex), Toast.LENGTH_SHORT).show()
            return
        }
        if (view.id == R.id.ivAddColor) {
            EventBus.getDefault().post(AddPaletteColorEvent(Color.parseColor(hex)))
            return
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_palette, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuPalette -> {
                EventBus.getDefault().post(ShowPaletteEvent())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        SPUtils.putInt(SPKeys.LAST_PALETTE_COLOR, rootView.paletteColorPicker.color)
        super.onDestroy()
    }
}