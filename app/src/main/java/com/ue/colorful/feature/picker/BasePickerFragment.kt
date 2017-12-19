package com.ue.colorful.feature.picker

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.ue.colorful.R
import com.ue.colorful.event.AddPaletteColorEvent
import com.ue.colorful.event.ShowPaletteEvent
import org.greenrobot.eventbus.EventBus

/**
 * Created by hawk on 2017/12/15.
 */
abstract class BasePickerFragment(private val layoutRes: Int) : Fragment() {
    protected lateinit var rootView: View

    private val mClipboardManager: ClipboardManager by lazy {
        activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        rootView = inflater.inflate(layoutRes, container, false)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_palette, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuPalette) {
            EventBus.getDefault().post(ShowPaletteEvent())
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    val pickerListener = object : View.OnClickListener {
        override fun onClick(view: View) {
            val colorInt = getColorInt()
            if (view.id == R.id.ivCopy) {
                val hex = String.format("#%08X", colorInt)
                val clip = ClipData.newPlainText("copy", hex)
                mClipboardManager.primaryClip = clip

                Toast.makeText(activity, activity.getString(R.string.color_copied, hex), Toast.LENGTH_SHORT).show()
                return
            }
            if (view.id == R.id.ivAddColor) {
                EventBus.getDefault().post(AddPaletteColorEvent(colorInt))
                return
            }
        }
    }

    abstract fun getColorInt(): Int
}