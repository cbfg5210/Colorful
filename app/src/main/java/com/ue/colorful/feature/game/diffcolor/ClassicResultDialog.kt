package com.ue.colorful.feature.game.diffcolor

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.dialog_result_classic.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by hawk on 2017/12/12.
 */

class ClassicResultDialog : DialogFragment() {
    private var level: Int = 0
    private var playAgainListener: View.OnClickListener? = null

    fun setPlayAgainListener(playAgainListener: View.OnClickListener) {
        this.playAgainListener = playAgainListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            level = arguments.getInt(ARG_LEVEL)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_result_classic, null)

        mDialogView.tvResult1.text = level.toString()

        val actualRecord = SPUtils.getInt(SPKeys.RECORD, 0)

        if (level > actualRecord) {
            val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)

            SPUtils.putInt(SPKeys.RECORD, level)
            SPUtils.putString(SPKeys.RECORD_DATE, formattedDate)

            if (actualRecord > 0) {  //There is already a saved result from a previous game
                mDialogView.tvResult3b.visibility = View.VISIBLE
                mDialogView.tvResult3b.setTextColor(Color.parseColor("#6DDC0C"))
                mDialogView.tvResult3b.text = " (+${(level - actualRecord).toString()})"
            } else {
                mDialogView.tvResult3b.visibility = View.GONE
            }
            //If new record and we have the user's email either because they are signed in or because they were signed in and saved
            //their email in SharedPreferences
        } else if (level == actualRecord) {
            if (actualRecord > 0) {  //There is already a saved result from a previous game
                mDialogView.tvResult3b.visibility = View.VISIBLE
                mDialogView.tvResult3b.setTextColor(Color.parseColor("#0BB3DC"))
                mDialogView.tvResult3b.text = " (=$level)"
            } else {
                mDialogView.tvResult3b.visibility = View.GONE
            }
        } else {
            if (actualRecord > 0) {  //There is already a saved result from a previous game
                mDialogView.tvResult3b.visibility = View.VISIBLE
                mDialogView.tvResult3b.setTextColor(Color.parseColor("#EE5F27"))
                mDialogView.tvResult3b.text = " (${(level - actualRecord).toString()})"
            } else {
                mDialogView.tvResult3b.visibility = View.GONE
            }
        }

        mDialogView.btnAgain.setOnClickListener { view ->
            dismiss()
            playAgainListener?.onClick(view)
        }
        return AlertDialog.Builder(activity)
                .setView(mDialogView)
                .create()
    }

    companion object {
        private val ARG_LEVEL = "arg_level"

        fun newInstance(level: Int): ClassicResultDialog {
            val dialog = ClassicResultDialog()
            val arguments = Bundle()
            arguments.putInt(ARG_LEVEL, level)
            dialog.arguments = arguments
            return dialog
        }
    }
}