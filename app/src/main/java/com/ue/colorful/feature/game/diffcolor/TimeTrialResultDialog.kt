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
import kotlinx.android.synthetic.main.dialog_result_timetrial.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by hawk on 2017/12/12.
 */

class TimeTrialResultDialog : DialogFragment() {
    private var playAgainListener: View.OnClickListener? = null
    private var chronometerText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            chronometerText = arguments.getString(ARG_CHRONOMETER_TEXT, "")
        }
    }

    fun setPlayAgainListener(playAgainListener: View.OnClickListener) {
        this.playAgainListener = playAgainListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_result_timetrial, null)

        val minutes = Integer.valueOf(chronometerText?.substring(0, 2))!!
        val seconds = Integer.valueOf(chronometerText?.substring(3, 5))!!
        val millis = Integer.valueOf(chronometerText?.substring(6))!!

        mDialogView.tvResult1.text = chronometerText

        val allTime = (60 * minutes + seconds) * 1000 + millis * 100
        val actualRecord = SPUtils.getInt(SPKeys.TIME_RECORD, 1000000)

        if (allTime < actualRecord) { //if new record was made
            val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)

            //Save the record
            SPUtils.putInt(SPKeys.TIME_RECORD, allTime)
            SPUtils.putString(SPKeys.TIME_RECORD_DATE, formattedDate)

            if (actualRecord == 1000000) { //if this was the first game
                //Log.i("RECORD STATUS", "NEW record - First game");
                mDialogView.tvResult2.text = "That's a new record!"  //just for motivational reasons to make user play more
                mDialogView.tvResult2.visibility = View.VISIBLE
                mDialogView.ivFlag.visibility = View.VISIBLE
                mDialogView.tvResult3b.visibility = View.GONE
            } else {
                //if this was the second, third etc. game
                //Let's calculate the difference between the actual_record and the current result
                val diff = actualRecord - allTime
                val diffFormatted = formatTime(diff)

                mDialogView.tvResult2.text = getString(R.string.newrecord)
                mDialogView.tvResult3b.visibility = View.VISIBLE
                mDialogView.tvResult3b.setTextColor(Color.parseColor("#6DDC0C"))
                mDialogView.tvResult3b.text = "(-$diffFormatted)"
                mDialogView.ivFlag.visibility = View.VISIBLE
            }
        } else if (allTime == actualRecord) {
            if (actualRecord == 1000000) { //if this was the first game
                mDialogView.tvResult2.text = "Almost!"  //just for motivational reasons to make user play more
                mDialogView.tvResult2.visibility = View.VISIBLE
                mDialogView.ivFlag.visibility = View.GONE
                mDialogView.tvResult3b.visibility = View.GONE
            } else { //if this was the second, third etc. game
                mDialogView.tvResult2.text = getString(R.string.almost)
                mDialogView.tvResult3b.visibility = View.VISIBLE
                mDialogView.tvResult3b.setTextColor(Color.parseColor("#0BB3DC"))
                mDialogView.tvResult3b.text = "(=${formatTime(actualRecord)})"
                mDialogView.ivFlag.visibility = View.GONE
            }

        } else { //if no record was made
            if (actualRecord != 1000000) { //if not the first game
                //Let's calculate the difference between the actual_record and the current result
                val diff = Math.abs(actualRecord - allTime) //othwerwise the diff will be negative what we wouldn't want
                val diffFormatted = formatTime(diff)

                mDialogView.tvResult3b.visibility = View.VISIBLE
                mDialogView.tvResult3b.setTextColor(Color.parseColor("#EE5F27"))
                mDialogView.tvResult3b.text = "(+$diffFormatted)"

                mDialogView.tvResult2.text = getString(R.string.youcandobetterthanthat)
                mDialogView.tvResult2.visibility = View.VISIBLE
                mDialogView.ivFlag.visibility = View.GONE
            } else {
                //if first game
                mDialogView.tvResult2.text = getString(R.string.youcandobetterthanthat)
                mDialogView.tvResult3b.visibility = View.GONE
                mDialogView.ivFlag.visibility = View.GONE
            }
        }

        val achStr1 = "00:14:00"
        val achStr2 = "00:11:00"
        val achStr3 = "00:09:00"

        val mach1 = Integer.valueOf(achStr1.substring(0, 2))!!
        val sach1 = Integer.valueOf(achStr1.substring(3, 5))!!
        val msach1 = Integer.valueOf(achStr1.substring(6))!!
        val allTimeAch1 = (60 * mach1 + sach1) * 1000 + msach1

        val mach2 = Integer.valueOf(achStr2.substring(0, 2))!!
        val sach2 = Integer.valueOf(achStr2.substring(3, 5))!!
        val msach2 = Integer.valueOf(achStr2.substring(6))!!
        val allTimeAch2 = (60 * mach2 + sach2) * 1000 + msach2

        val mach3 = Integer.valueOf(achStr3.substring(0, 2))!!
        val sach3 = Integer.valueOf(achStr3.substring(3, 5))!!
        val msach3 = Integer.valueOf(achStr3.substring(6))!!
        val allTimeAch3 = (60 * mach3 + sach3) * 1000 + msach3

        if (allTime < allTimeAch3) {
            //user did under 9 seconds so we give him/her all the three badges
            if (!SPUtils.getBoolean("ACH4", false)
                    && SPUtils.getBoolean("ACH5", false)
                    && SPUtils.getBoolean("ACH6", false)) {
                mDialogView.ivStar1.visibility = View.VISIBLE
                mDialogView.ivStar3.visibility = View.VISIBLE
                mDialogView.ivStar2.visibility = View.VISIBLE
                mDialogView.lvAchUnlocked.visibility = View.VISIBLE
            }

            if (!SPUtils.getBoolean("ACH6", false)) {
                SPUtils.putBoolean("ACH6", true)
                mDialogView.tvResult4.visibility = View.VISIBLE
                mDialogView.lvAchUnlocked.visibility = View.VISIBLE
            }
            if (!SPUtils.getBoolean("ACH5", false)) {
                SPUtils.putBoolean("ACH5", true)
                mDialogView.tvResult4.visibility = View.VISIBLE
                mDialogView.lvAchUnlocked.visibility = View.VISIBLE
            }
            if (!SPUtils.getBoolean("ACH4", false)) {
                SPUtils.putBoolean("ACH4", true)
                mDialogView.tvResult4.visibility = View.VISIBLE
                mDialogView.lvAchUnlocked.visibility = View.VISIBLE
            }
        } else if (allTime >= allTimeAch3 && allTime < allTimeAch2) {
            //user did between 9 and 11 seconds so we give him/her the silver and bronze badges
            if (!(SPUtils.getBoolean("ACH4", false)
                    && SPUtils.getBoolean("ACH5", false))) {
                mDialogView.ivStar1.visibility = View.VISIBLE
                mDialogView.ivStar2.visibility = View.VISIBLE
                mDialogView.lvAchUnlocked.visibility = View.VISIBLE
            }

            if (!SPUtils.getBoolean("ACH5", false)) {
                SPUtils.putBoolean("ACH5", true)
                mDialogView.tvResult4.visibility = View.VISIBLE
                mDialogView.lvAchUnlocked.visibility = View.VISIBLE
            }
            if (!SPUtils.getBoolean("ACH4", false)) {
                SPUtils.putBoolean("ACH4", true)
                mDialogView.tvResult4.visibility = View.VISIBLE
                mDialogView.lvAchUnlocked.visibility = View.VISIBLE
            }

        } else if (allTime >= allTimeAch2 && allTime < allTimeAch1) {
            //user did between 11 and 14 seconds so we give him/her the bronze badge
            if (!SPUtils.getBoolean("ACH4", false)) {
                SPUtils.putBoolean("ACH4", true)
                mDialogView.tvResult4.visibility = View.VISIBLE
                mDialogView.ivStar1.visibility = View.VISIBLE
                mDialogView.lvAchUnlocked.visibility = View.VISIBLE
            }
        }

        mDialogView.btnAgain.setOnClickListener { view ->
            dismiss()
            playAgainListener?.onClick(view)
        }

        return AlertDialog.Builder(context)
                .setView(mDialogView)
                .create()
    }

    private fun formatTime(time: Int): String {
        val df = DecimalFormat("00")
        val df2 = DecimalFormat("0")
        val h = (time / (3600 * 1000))
        var r = (time % (3600 * 1000))
        val m = (r / (60 * 1000))
        r = (r % (60 * 1000))
        val s = (r / 1000)
        val ms = (time - (60 * m + s) * 1000) / 100 //3034-3000=34 -- 2994-2000=994

        var text = ""
        if (h > 0) text += "${df.format(h.toLong())}:"
        text += "${df.format(m.toLong())}:${df.format(s.toLong())}.${df2.format(ms.toLong())}"

        return text
    }

    companion object {
        private val ARG_CHRONOMETER_TEXT = "chronometer_text"

        fun newInstance(chronometerText: String): TimeTrialResultDialog {
            val dialog = TimeTrialResultDialog()
            val arguments = Bundle()
            arguments.putString(ARG_CHRONOMETER_TEXT, chronometerText)
            dialog.arguments = arguments
            return dialog
        }
    }
}
