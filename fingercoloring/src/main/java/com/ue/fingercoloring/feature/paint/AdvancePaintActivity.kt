package com.ue.fingercoloring.feature.paint

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ue.fingercoloring.R
import com.ue.fingercoloring.constant.Constants
import com.ue.fingercoloring.factory.MyDialogFactory
import com.ue.fingercoloring.listener.OnAddWordsSuccessListener
import com.ue.fingercoloring.listener.OnChangeBorderListener
import com.ue.fingercoloring.util.ShareImageUtil
import com.ue.fingercoloring.view.DragedTextView
import com.ue.fingercoloring.view.TipDialog
import kotlinx.android.synthetic.main.activity_paint_advance.*

/**
 * Created by macpro001 on 20/8/15.
 */
class AdvancePaintActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imageUri: String
    private lateinit var myDialogFactory: MyDialogFactory

    private lateinit var presenter: PaintPresenter
    private lateinit var tipDialog: TipDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val params = window.attributes
        params.width = resources.displayMetrics.widthPixels
        this.window.attributes = params

        setContentView(R.layout.activity_paint_advance)

        presenter = PaintPresenter(this)
        tipDialog = TipDialog.newInstance()
        myDialogFactory = MyDialogFactory(this)

        imageUri = intent.getStringExtra(ARG_PICTURE_PATH)
        current_image.setImageBitmap(BitmapFactory.decodeFile(imageUri))

        addwords.setOnClickListener(this)
        addborder.setOnClickListener(this)
        share.setOnClickListener(this)
        repaint.setOnClickListener(this)
        cancel.setOnClickListener(this)
    }

    private fun repaintPictureDialog() {
        myDialogFactory.showRepaintDialog(View.OnClickListener {
            myDialogFactory.dismissDialog()
            setResult(Constants.REPAINT_RESULT)
            finish()
        })
    }

    private fun shareDrawable() {
        paintview.isDrawingCacheEnabled = true
        paintview.destroyDrawingCache()
        paintview.buildDrawingCache()

        tipDialog.showTip(supportFragmentManager, getString(R.string.savingimage))
        presenter.saveImageLocally(paintview.drawingCache, Constants.SHARE_WORK, object : PaintPresenter.OnSaveImageListener {
            override fun onSaved(path: String) {
                tipDialog.dismiss()
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(this@AdvancePaintActivity, getString(R.string.saveFailed), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AdvancePaintActivity, getString(R.string.saveSuccess) + path, Toast.LENGTH_SHORT).show()
                    ShareImageUtil.getInstance(this@AdvancePaintActivity).shareImg(path)
                }
            }
        })
    }

    private fun addWordsDialog() {
        myDialogFactory.showAddWordsDialog(object : OnAddWordsSuccessListener {
            override fun addWordsSuccess(dragedTextView: DragedTextView) {
                (current_image.parent as ViewGroup).addView(dragedTextView)
            }
        })
    }

    private fun addBorderDialog() {
        myDialogFactory.showAddBorderDialog(object : OnChangeBorderListener {
            override fun changeBorder(drawableid: Int, pt: Int, pd: Int, pl: Int, pr: Int) {
                if (drawableid != 0) {
                    border.setBackgroundResource(drawableid)
                    current_image.setPadding(pl, pt, pr, pd)
                    current_image.requestLayout()
                }
                paintview.requestLayout()
            }
        })
    }

    override fun onClick(v: View) {
        val viewId = v.id
        when (viewId) {
            R.id.addwords -> addWordsDialog()
            R.id.addborder -> addBorderDialog()
            R.id.repaint -> repaintPictureDialog()
            R.id.cancel -> finish()
            R.id.share -> shareDrawable()
        }
    }

    companion object {
        private val ARG_PICTURE_PATH = "arg_picture_path"

        fun startForResult(context: Activity, picturePath: String) {
            val intent = Intent(context, AdvancePaintActivity::class.java)
            intent.putExtra(ARG_PICTURE_PATH, picturePath)
            context.startActivityForResult(intent, Constants.REQ_ADVANCED_PAINT)
        }
    }
}
