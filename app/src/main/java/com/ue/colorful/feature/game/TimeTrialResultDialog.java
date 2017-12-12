package com.ue.colorful.feature.game;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ue.colorful.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hawk on 2017/12/12.
 */

public class TimeTrialResultDialog extends DialogFragment {
    private static final String ARG_CHRONOMETER_TEXT = "chronometer_text";
    private View.OnClickListener playAgainListener;
    private String chronometerText;

    public static TimeTrialResultDialog newInstance(String chronometerText) {
        TimeTrialResultDialog dialog = new TimeTrialResultDialog();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_CHRONOMETER_TEXT, chronometerText);
        dialog.setArguments(arguments);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            chronometerText = arguments.getString(ARG_CHRONOMETER_TEXT, "");
        }
    }

    public void setPlayAgainListener(View.OnClickListener playAgainListener) {
        this.playAgainListener = playAgainListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View mDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_result_timetrial, null);

        TextView tv_result_1 = (TextView) mDialogView.findViewById(R.id.tv_result_1);
        TextView tv_result_2 = (TextView) mDialogView.findViewById(R.id.tv_result_2);
        TextView tv_result_4 = (TextView) mDialogView.findViewById(R.id.tv_result_4);
        TextView tv_result_3b = (TextView) mDialogView.findViewById(R.id.tv_result_3b);

        ImageView iv_star1 = (ImageView) mDialogView.findViewById(R.id.iv_star1);
        ImageView iv_star2 = (ImageView) mDialogView.findViewById(R.id.iv_star2);
        ImageView iv_star3 = (ImageView) mDialogView.findViewById(R.id.iv_star3);
        ImageView iv_flag = (ImageView) mDialogView.findViewById(R.id.iv_flag);
        LinearLayout lv_ach_unlocked = (LinearLayout) mDialogView.findViewById(R.id.lv_ach_unlocked);

        int minutes = Integer.valueOf(chronometerText.substring(0, 2));
        int seconds = Integer.valueOf(chronometerText.substring(3, 5));
        int millis = Integer.valueOf(chronometerText.substring(6));
        tv_result_1.setText(chronometerText);

        int alltime = (60 * minutes + seconds) * 1000 + millis * 100;

        //Log.i("alltime", alltime + "");

        SharedPreferences sharedpref = getActivity().getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE);
        //Log.i("actual record", sharedpref.getInt("TIMERECORD", 1000000) + "");
        int actual_record = sharedpref.getInt("TIMERECORD", 1000000);
        //Log.i("actual record and alltime", actual_record + " ," + alltime);

        if (alltime < actual_record) { //if new record was made
            //Log.i("NO TIMERECORD", "alltime < actual");

            Calendar c = Calendar.getInstance();
            //Log.i("Current date", c.getTime() + "");

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());
            //Log.i("Current formattedDate", formattedDate + "");

            //Save the record
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit();
            editor.putInt("TIMERECORD", alltime);
            editor.putString("TIMERECORD_DATE", formattedDate);
            editor.commit();

            if (actual_record == 1000000) { //if this was the first game
                //Log.i("RECORD STATUS", "NEW record - First game");
                tv_result_2.setText("That's a new record!");  //just for motivational reasons to make user play more
                tv_result_2.setVisibility(View.VISIBLE);
                iv_flag.setVisibility(View.VISIBLE);
                tv_result_3b.setVisibility(View.GONE);
            } else { //if this was the second, third etc. game
                //Log.i("RECORD STATUS", "NEW record - Not the first game");

                //Let's calculate the difference between the actual_record and the current result
                int diff = actual_record - alltime;
                String diff_formatted = format_time(diff);

                tv_result_2.setText(getString(R.string.newrecord));
                tv_result_3b.setVisibility(View.VISIBLE);
                tv_result_3b.setTextColor(Color.parseColor("#6DDC0C"));
                tv_result_3b.setText("(-" + diff_formatted + ")");
                iv_flag.setVisibility(View.VISIBLE);
            }
        } else if (alltime == actual_record) {
            if (actual_record == 1000000) { //if this was the first game
                //Log.i("RECORD STATUS", "DEUCE - First game");
                tv_result_2.setText("Almost!");  //just for motivational reasons to make user play more
                tv_result_2.setVisibility(View.VISIBLE);
                iv_flag.setVisibility(View.GONE);
                tv_result_3b.setVisibility(View.GONE);
            } else { //if this was the second, third etc. game
                tv_result_2.setText(getString(R.string.almost));
                tv_result_3b.setVisibility(View.VISIBLE);
                tv_result_3b.setTextColor(Color.parseColor("#0BB3DC"));
                tv_result_3b.setText("(=" + format_time(actual_record) + ")");
                iv_flag.setVisibility(View.GONE);
            }

        } else { //if no record was made
            if (actual_record != 1000000) { //if not the first game
                //Log.i("RECORD STATUS", "NO record - Not the first game");

                //Let's calculate the difference between the actual_record and the current result
                int diff = Math.abs(actual_record - alltime); //othwerwise the diff will be negative what we wouldn't want
                String diff_formatted = format_time(diff);

                tv_result_3b.setVisibility(View.VISIBLE);
                tv_result_3b.setTextColor(Color.parseColor("#EE5F27"));
                tv_result_3b.setText("(+" + diff_formatted + ")");

                tv_result_2.setText(getString(R.string.youcandobetterthanthat));
                tv_result_2.setVisibility(View.VISIBLE);
                iv_flag.setVisibility(View.GONE);
            } else { //if first game
                //Log.i("RECORD STATUS", "NO record - First game");
                tv_result_2.setText(getString(R.string.youcandobetterthanthat));
                tv_result_3b.setVisibility(View.GONE);
                iv_flag.setVisibility(View.GONE);
            }
        }

        String ach_str_1 = "00:14:00";
        String ach_str_2 = "00:11:00";
        String ach_str_3 = "00:09:00";

        int mach_1 = Integer.valueOf(ach_str_1.substring(0, 2));
        int sach_1 = Integer.valueOf(ach_str_1.substring(3, 5));
        int msach_1 = Integer.valueOf(ach_str_1.substring(6));
        int alltime_ach1 = (60 * mach_1 + sach_1) * 1000 + msach_1;

        int mach_2 = Integer.valueOf(ach_str_2.substring(0, 2));
        int sach_2 = Integer.valueOf(ach_str_2.substring(3, 5));
        int msach_2 = Integer.valueOf(ach_str_2.substring(6));
        int alltime_ach2 = (60 * mach_2 + sach_2) * 1000 + msach_2;

        int mach_3 = Integer.valueOf(ach_str_3.substring(0, 2));
        int sach_3 = Integer.valueOf(ach_str_3.substring(3, 5));
        int msach_3 = Integer.valueOf(ach_str_3.substring(6));
        int alltime_ach3 = (60 * mach_3 + sach_3) * 1000 + msach_3;

        if (alltime < alltime_ach3) {
            //user did under 9 seconds so we give him/her all the three badges
            if (!(sharedpref.getBoolean("ACH4", false) && sharedpref.getBoolean("ACH5", false) && sharedpref.getBoolean("ACH6", false))) {
                iv_star1.setVisibility(View.VISIBLE);
                iv_star3.setVisibility(View.VISIBLE);
                iv_star2.setVisibility(View.VISIBLE);
                lv_ach_unlocked.setVisibility(View.VISIBLE);
            }

            if (!sharedpref.getBoolean("ACH6", false)) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit();
                editor.putBoolean("ACH6", true);
                editor.commit();
                tv_result_4.setVisibility(View.VISIBLE);
                lv_ach_unlocked.setVisibility(View.VISIBLE);
            }
            if (!sharedpref.getBoolean("ACH5", false)) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit();
                editor.putBoolean("ACH5", true);
                editor.commit();
                tv_result_4.setVisibility(View.VISIBLE);
                lv_ach_unlocked.setVisibility(View.VISIBLE);
            }
            if (!sharedpref.getBoolean("ACH4", false)) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit();
                editor.putBoolean("ACH4", true);
                editor.commit();
                tv_result_4.setVisibility(View.VISIBLE);
                lv_ach_unlocked.setVisibility(View.VISIBLE);
            }
        } else if (alltime >= alltime_ach3 && alltime < alltime_ach2) {
            //user did between 9 and 11 seconds so we give him/her the silver and bronze badges
            if (!(sharedpref.getBoolean("ACH4", false) && sharedpref.getBoolean("ACH5", false))) {
                iv_star1.setVisibility(View.VISIBLE);
                iv_star2.setVisibility(View.VISIBLE);
                lv_ach_unlocked.setVisibility(View.VISIBLE);
            }

            if (!sharedpref.getBoolean("ACH5", false)) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit();
                editor.putBoolean("ACH5", true);
                editor.commit();
                tv_result_4.setVisibility(View.VISIBLE);
                lv_ach_unlocked.setVisibility(View.VISIBLE);
            }
            if (!sharedpref.getBoolean("ACH4", false)) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit();
                editor.putBoolean("ACH4", true);
                editor.commit();
                tv_result_4.setVisibility(View.VISIBLE);
                lv_ach_unlocked.setVisibility(View.VISIBLE);
            }

        } else if (alltime >= alltime_ach2 && alltime < alltime_ach1) {
            //user did between 11 and 14 seconds so we give him/her the bronze badge
            if (!sharedpref.getBoolean("ACH4", false)) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS_NAME", Activity.MODE_PRIVATE).edit();
                editor.putBoolean("ACH4", true);
                editor.commit();
                tv_result_4.setVisibility(View.VISIBLE);
                iv_star1.setVisibility(View.VISIBLE);
                lv_ach_unlocked.setVisibility(View.VISIBLE);
            }
        }

        mDialogView.findViewById(R.id.btn_again)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        if (playAgainListener != null) {
                            playAgainListener.onClick(view);
                        }
                    }
                });

        return new AlertDialog.Builder(getContext())
                .setView(mDialogView)
                .create();
    }

    private String format_time(int time) {
        DecimalFormat df = new DecimalFormat("00");
        DecimalFormat df2 = new DecimalFormat("0");
        int h = (int) (time / (3600 * 1000));
        int r = (int) (time % (3600 * 1000));
        int m = (int) (r / (60 * 1000));
        r = (int) (r % (60 * 1000));
        int s = (int) (r / 1000);
        int ms = (time - ((60 * m + s) * 1000)) / 100; //3034-3000=34 -- 2994-2000=994
        String text = "";
        if (h > 0) text += df.format(h) + ":";
        text += df.format(m) + ":";
        text += df.format(s) + ".";
        text += df2.format(ms);
        return text;
    }
}
