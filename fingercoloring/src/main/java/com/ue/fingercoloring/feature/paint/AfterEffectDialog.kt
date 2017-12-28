package com.ue.fingercoloring.feature.paint

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.ue.fingercoloring.R
import com.ue.fingercoloring.factory.DialogHelper
import com.ue.fingercoloring.listener.OnAddWordsSuccessListener
import com.ue.fingercoloring.listener.OnChangeBorderListener
import com.ue.fingercoloring.util.PicassoUtils
import com.ue.fingercoloring.view.DragedTextView
import com.ue.fingercoloring.view.TipDialog
import kotlinx.android.synthetic.main.dialog_after_effect.view.*

/**
 * Created by hawk on 2017/12/28.
 */
class AfterEffectDialog : DialogFragment() {

    private lateinit var rootView: View
    private lateinit var imageUri: String
    private lateinit var mDialogHelper: DialogHelper
    private lateinit var presenter: PaintPresenter
    private lateinit var tipDialog: TipDialog

    private var effectListener: PaintPresenter.OnSaveImageListener? = null
    private var hasEffectAdded = false

    fun setEffectListener(listener: PaintPresenter.OnSaveImageListener) {
        effectListener = listener
    }

    companion object {
        private val ARG_PICTURE_PATH = "arg_picture_path"

        fun newInstance(picturePath: String): AfterEffectDialog {
            val dialog = AfterEffectDialog()
            dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0)
            dialog.isCancelable = false

            val arguments = Bundle()
            arguments.putString(ARG_PICTURE_PATH, picturePath)
            dialog.arguments = arguments
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = PaintPresenter(context as AppCompatActivity)
        tipDialog = TipDialog.newInstance()
        mDialogHelper = DialogHelper(context)

        imageUri = arguments.getString(ARG_PICTURE_PATH)
    }

    override fun onStart() {
        super.onStart()
        if (dialog == null || dialog.window == null) return

        val params = dialog.window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window.attributes = params
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dialog_after_effect, null)

        PicassoUtils.displayImage(context, rootView.current_image, "file://$imageUri")

        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.tvAeCancel -> dismiss()

                R.id.tvAeOk -> {
                    if (!hasEffectAdded) {
                        dismiss()
                        return@OnClickListener
                    }
                    rootView.paintview.isDrawingCacheEnabled = true
                    rootView.paintview.destroyDrawingCache()
                    rootView.paintview.buildDrawingCache()

                    tipDialog.showTip(childFragmentManager, getString(R.string.savingimage))
                    presenter.saveImageLocally(
                            rootView.paintview.drawingCache,
                            getBorderWorkName(imageUri),
                            object : PaintPresenter.OnSaveImageListener {
                                override fun onSaved(path: String) {
                                    effectListener?.onSaved(path)
                                    dismiss()
                                }
                            })
                }

                R.id.addWords ->
                    mDialogHelper.showAddWordsDialog(object : OnAddWordsSuccessListener {
                        override fun addWordsSuccess(dragedTextView: DragedTextView) {
                            (rootView.current_image.parent as ViewGroup).addView(dragedTextView)
                            hasEffectAdded = true
                        }
                    })

                R.id.addBorder ->
                    mDialogHelper.showAddBorderDialog(object : OnChangeBorderListener {
                        override fun changeBorder(drawableId: Int, pt: Int, pd: Int, pl: Int, pr: Int) {
                            if (drawableId != 0) {
                                rootView.border.setBackgroundResource(drawableId)
                                rootView.current_image.setPadding(pl, pt, pr, pd)
                                rootView.current_image.requestLayout()
                                hasEffectAdded = true
                            }
                            rootView.paintview.requestLayout()
                        }
                    })
            }
        }

        rootView.addWords.setOnClickListener(listener)
        rootView.addBorder.setOnClickListener(listener)
        rootView.tvAeCancel.setOnClickListener(listener)
        rootView.tvAeOk.setOnClickListener(listener)

        return rootView
    }

    private fun getBorderWorkName(path: String): String {
        val startIndex = path.lastIndexOf("/") + 1
        var name = path.substring(startIndex)
        name = name.replace(".png", "_bd.png")
        return name
    }
}