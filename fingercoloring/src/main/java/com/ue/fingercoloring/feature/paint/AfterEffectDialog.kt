package com.ue.fingercoloring.feature.paint

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.ue.fingercoloring.R
import com.ue.fingercoloring.factory.MyDialogFactory
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
    private lateinit var myDialogFactory: MyDialogFactory
    private lateinit var presenter: PaintPresenter
    private lateinit var tipDialog: TipDialog

    private var effectListener: OnCompleteEffectListener? = null

    fun setEffectListener(effectListener: OnCompleteEffectListener) {
        this.effectListener = effectListener
    }

    companion object {
        private val ARG_PICTURE_PATH = "arg_picture_path"

        fun newInstance(picturePath: String): AfterEffectDialog {
            val dialog = AfterEffectDialog()
            dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0)

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
        myDialogFactory = MyDialogFactory(context)

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
                R.id.tvAEOk -> effectListener?.onEffectCompleted(rootView.current_image.drawingCache)
                R.id.tvAECancel -> dismiss()

                R.id.addwords ->
                    myDialogFactory.showAddWordsDialog(object : OnAddWordsSuccessListener {
                        override fun addWordsSuccess(dragedTextView: DragedTextView) {
                            (rootView.current_image.parent as ViewGroup).addView(dragedTextView)
                        }
                    })

                R.id.addborder ->
                    myDialogFactory.showAddBorderDialog(object : OnChangeBorderListener {
                        override fun changeBorder(drawableId: Int, pt: Int, pd: Int, pl: Int, pr: Int) {
                            if (drawableId != 0) {
                                rootView.border.setBackgroundResource(drawableId)
                                rootView.current_image.setPadding(pl, pt, pr, pd)
                                rootView.current_image.requestLayout()
                            }
                            rootView.paintview.requestLayout()
                        }
                    })
            }
        }

        rootView.addwords.setOnClickListener(listener)
        rootView.addborder.setOnClickListener(listener)
        rootView.tvAECancel.setOnClickListener(listener)

        return rootView
    }

    interface OnCompleteEffectListener {
        fun onEffectCompleted(effectBitmap: Bitmap)
    }
}