package com.ue.colorful.feature.game

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.ue.colorful.R
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
        val arguments = arguments
        if (arguments != null) {
            level = arguments.getInt(ARG_LEVEL)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_result_classic, null)

        val tv_result_1 = mDialogView.findViewById<View>(R.id.tv_result_1) as TextView
        val tv_result_3b = mDialogView.findViewById<View>(R.id.tv_result_3b) as TextView

        tv_result_1.text = level.toString()

        val actual_record = activity.getPreferences(Activity.MODE_PRIVATE).getInt("RECORD", 0)

        if (level > actual_record) {
            val c = Calendar.getInstance()

            val df = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = df.format(c.time)

            activity.getPreferences(Activity.MODE_PRIVATE)
                    .edit()
                    .putInt("RECORD", level)
                    .putString("RECORD_DATE", formattedDate)
                    .apply()

            if (actual_record > 0) {  //There is already a saved result from a previous game
                tv_result_3b.visibility = View.VISIBLE
                tv_result_3b.setTextColor(Color.parseColor("#6DDC0C"))
                tv_result_3b.text = " (+" + (level - actual_record).toString() + ")"
            } else {
                tv_result_3b.visibility = View.GONE
            }
            //If new record and we have the user's email either because they are signed in or because they were signed in and saved
            //their email in SharedPreferences
        } else if (level == actual_record) {
            if (actual_record > 0) {  //There is already a saved result from a previous game
                tv_result_3b.visibility = View.VISIBLE
                tv_result_3b.setTextColor(Color.parseColor("#0BB3DC"))
                tv_result_3b.text = " (=$level)"
            } else {
                tv_result_3b.visibility = View.GONE
            }
        } else {
            if (actual_record > 0) {  //There is already a saved result from a previous game
                tv_result_3b.visibility = View.VISIBLE
                tv_result_3b.setTextColor(Color.parseColor("#EE5F27"))
                tv_result_3b.text = " (" + (level - actual_record).toString() + ")"
            } else {
                tv_result_3b.visibility = View.GONE
            }
        }

        mDialogView.findViewById<View>(R.id.btn_again).setOnClickListener { view ->
            dismiss()
            if (playAgainListener != null) {
                playAgainListener!!.onClick(view)
            }
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