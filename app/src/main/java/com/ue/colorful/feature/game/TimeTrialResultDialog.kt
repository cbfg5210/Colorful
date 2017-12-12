package com.ue.colorful.feature.game

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ue.colorful.R
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
        val arguments = arguments
        if (arguments != null) {
            chronometerText = arguments.getString(ARG_CHRONOMETER_TEXT, "")
        }
    }

    fun setPlayAgainListener(playAgainListener: View.OnClickListener) {
        this.playAgainListener = playAgainListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_result_timetrial, null)

        val tv_result_1 = mDialogView.findViewById<View>(R.id.tv_result_1) as TextView
        val tv_result_2 = mDialogView.findViewById<View>(R.id.tv_result_2) as TextView
        val tv_result_4 = mDialogView.findViewById<View>(R.id.tv_result_4) as TextView
        val tv_result_3b = mDialogView.findViewById<View>(R.id.tv_result_3b) as TextView

        val iv_star1 = mDialogView.findViewById<View>(R.id.iv_star1) as ImageView
        val iv_star2 = mDialogView.findViewById<View>(R.id.iv_star2) as ImageView
        val iv_star3 = mDialogView.findViewById<View>(R.id.iv_star3) as ImageView
        val iv_flag = mDialogView.findViewById<View>(R.id.iv_flag) as ImageView
        val lv_ach_unlocked = mDialogView.findViewById<View>(R.id.lv_ach_unlocked) as LinearLayout

        val minutes = Integer.valueOf(chronometerText!!.substring(0, 2))!!
        val seconds = Integer.valueOf(chronometerText!!.substring(3, 5))!!
        val millis = Integer.valueOf(chronometerText!!.substring(6))!!
        tv_result_1.text = chronometerText

        val alltime = (60 * minutes + seconds) * 1000 + millis * 100

        //Log.i("alltime", alltime + "");

        val sharedpref = activity.getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE)
        //Log.i("actual record", sharedpref.getInt("TIMERECORD", 1000000) + "");
        val actual_record = sharedpref.getInt("TIMERECORD", 1000000)
        //Log.i("actual record and alltime", actual_record + " ," + alltime);

        if (alltime < actual_record) { //if new record was made
            //Log.i("NO TIMERECORD", "alltime < actual");

            val c = Calendar.getInstance()
            //Log.i("Current date", c.getTime() + "");

            val df = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = df.format(c.time)
            //Log.i("Current formattedDate", formattedDate + "");

            //Save the record
            val editor = activity.getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit()
            editor.putInt("TIMERECORD", alltime)
            editor.putString("TIMERECORD_DATE", formattedDate)
            editor.commit()

            if (actual_record == 1000000) { //if this was the first game
                //Log.i("RECORD STATUS", "NEW record - First game");
                tv_result_2.text = "That's a new record!"  //just for motivational reasons to make user play more
                tv_result_2.visibility = View.VISIBLE
                iv_flag.visibility = View.VISIBLE
                tv_result_3b.visibility = View.GONE
            } else { //if this was the second, third etc. game
                //Log.i("RECORD STATUS", "NEW record - Not the first game");

                //Let's calculate the difference between the actual_record and the current result
                val diff = actual_record - alltime
                val diff_formatted = format_time(diff)

                tv_result_2.text = getString(R.string.newrecord)
                tv_result_3b.visibility = View.VISIBLE
                tv_result_3b.setTextColor(Color.parseColor("#6DDC0C"))
                tv_result_3b.text = "(-$diff_formatted)"
                iv_flag.visibility = View.VISIBLE
            }
        } else if (alltime == actual_record) {
            if (actual_record == 1000000) { //if this was the first game
                //Log.i("RECORD STATUS", "DEUCE - First game");
                tv_result_2.text = "Almost!"  //just for motivational reasons to make user play more
                tv_result_2.visibility = View.VISIBLE
                iv_flag.visibility = View.GONE
                tv_result_3b.visibility = View.GONE
            } else { //if this was the second, third etc. game
                tv_result_2.text = getString(R.string.almost)
                tv_result_3b.visibility = View.VISIBLE
                tv_result_3b.setTextColor(Color.parseColor("#0BB3DC"))
                tv_result_3b.text = "(=" + format_time(actual_record) + ")"
                iv_flag.visibility = View.GONE
            }

        } else { //if no record was made
            if (actual_record != 1000000) { //if not the first game
                //Log.i("RECORD STATUS", "NO record - Not the first game");

                //Let's calculate the difference between the actual_record and the current result
                val diff = Math.abs(actual_record - alltime) //othwerwise the diff will be negative what we wouldn't want
                val diff_formatted = format_time(diff)

                tv_result_3b.visibility = View.VISIBLE
                tv_result_3b.setTextColor(Color.parseColor("#EE5F27"))
                tv_result_3b.text = "(+$diff_formatted)"

                tv_result_2.text = getString(R.string.youcandobetterthanthat)
                tv_result_2.visibility = View.VISIBLE
                iv_flag.visibility = View.GONE
            } else { //if first game
                //Log.i("RECORD STATUS", "NO record - First game");
                tv_result_2.text = getString(R.string.youcandobetterthanthat)
                tv_result_3b.visibility = View.GONE
                iv_flag.visibility = View.GONE
            }
        }

        val ach_str_1 = "00:14:00"
        val ach_str_2 = "00:11:00"
        val ach_str_3 = "00:09:00"

        val mach_1 = Integer.valueOf(ach_str_1.substring(0, 2))!!
        val sach_1 = Integer.valueOf(ach_str_1.substring(3, 5))!!
        val msach_1 = Integer.valueOf(ach_str_1.substring(6))!!
        val alltime_ach1 = (60 * mach_1 + sach_1) * 1000 + msach_1

        val mach_2 = Integer.valueOf(ach_str_2.substring(0, 2))!!
        val sach_2 = Integer.valueOf(ach_str_2.substring(3, 5))!!
        val msach_2 = Integer.valueOf(ach_str_2.substring(6))!!
        val alltime_ach2 = (60 * mach_2 + sach_2) * 1000 + msach_2

        val mach_3 = Integer.valueOf(ach_str_3.substring(0, 2))!!
        val sach_3 = Integer.valueOf(ach_str_3.substring(3, 5))!!
        val msach_3 = Integer.valueOf(ach_str_3.substring(6))!!
        val alltime_ach3 = (60 * mach_3 + sach_3) * 1000 + msach_3

        if (alltime < alltime_ach3) {
            //user did under 9 seconds so we give him/her all the three badges
            if (!(sharedpref.getBoolean("ACH4", false) && sharedpref.getBoolean("ACH5", false) && sharedpref.getBoolean("ACH6", false))) {
                iv_star1.visibility = View.VISIBLE
                iv_star3.visibility = View.VISIBLE
                iv_star2.visibility = View.VISIBLE
                lv_ach_unlocked.visibility = View.VISIBLE
            }

            if (!sharedpref.getBoolean("ACH6", false)) {
                val editor = activity.getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit()
                editor.putBoolean("ACH6", true)
                editor.commit()
                tv_result_4.visibility = View.VISIBLE
                lv_ach_unlocked.visibility = View.VISIBLE
            }
            if (!sharedpref.getBoolean("ACH5", false)) {
                val editor = activity.getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit()
                editor.putBoolean("ACH5", true)
                editor.commit()
                tv_result_4.visibility = View.VISIBLE
                lv_ach_unlocked.visibility = View.VISIBLE
            }
            if (!sharedpref.getBoolean("ACH4", false)) {
                val editor = activity.getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit()
                editor.putBoolean("ACH4", true)
                editor.commit()
                tv_result_4.visibility = View.VISIBLE
                lv_ach_unlocked.visibility = View.VISIBLE
            }
        } else if (alltime >= alltime_ach3 && alltime < alltime_ach2) {
            //user did between 9 and 11 seconds so we give him/her the silver and bronze badges
            if (!(sharedpref.getBoolean("ACH4", false) && sharedpref.getBoolean("ACH5", false))) {
                iv_star1.visibility = View.VISIBLE
                iv_star2.visibility = View.VISIBLE
                lv_ach_unlocked.visibility = View.VISIBLE
            }

            if (!sharedpref.getBoolean("ACH5", false)) {
                val editor = activity.getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit()
                editor.putBoolean("ACH5", true)
                editor.commit()
                tv_result_4.visibility = View.VISIBLE
                lv_ach_unlocked.visibility = View.VISIBLE
            }
            if (!sharedpref.getBoolean("ACH4", false)) {
                val editor = activity.getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit()
                editor.putBoolean("ACH4", true)
                editor.commit()
                tv_result_4.visibility = View.VISIBLE
                lv_ach_unlocked.visibility = View.VISIBLE
            }

        } else if (alltime >= alltime_ach2 && alltime < alltime_ach1) {
            //user did between 11 and 14 seconds so we give him/her the bronze badge
            if (!sharedpref.getBoolean("ACH4", false)) {
                val editor = activity.getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit()
                editor.putBoolean("ACH4", true)
                editor.commit()
                tv_result_4.visibility = View.VISIBLE
                iv_star1.visibility = View.VISIBLE
                lv_ach_unlocked.visibility = View.VISIBLE
            }
        }

        mDialogView.findViewById<View>(R.id.btn_again)
                .setOnClickListener { view ->
                    dismiss()
                    if (playAgainListener != null) {
                        playAgainListener!!.onClick(view)
                    }
                }

        return AlertDialog.Builder(context)
                .setView(mDialogView)
                .create()
    }

    private fun format_time(time: Int): String {
        val df = DecimalFormat("00")
        val df2 = DecimalFormat("0")
        val h = (time / (3600 * 1000)).toInt()
        var r = (time % (3600 * 1000)).toInt()
        val m = (r / (60 * 1000)).toInt()
        r = (r % (60 * 1000)).toInt()
        val s = (r / 1000).toInt()
        val ms = (time - (60 * m + s) * 1000) / 100 //3034-3000=34 -- 2994-2000=994
        var text = ""
        if (h > 0) text += df.format(h.toLong()) + ":"
        text += df.format(m.toLong()) + ":"
        text += df.format(s.toLong()) + "."
        text += df2.format(ms.toLong())
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
