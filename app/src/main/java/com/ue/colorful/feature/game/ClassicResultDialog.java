package com.ue.colorful.feature.game;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ue.colorful.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hawk on 2017/12/12.
 */

public class ClassicResultDialog extends DialogFragment {
    private static final String ARG_LEVEL = "arg_level";
    private int level;
    private View.OnClickListener playAgainListener;

    public static ClassicResultDialog newInstance(int level) {
        ClassicResultDialog dialog = new ClassicResultDialog();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_LEVEL, level);
        dialog.setArguments(arguments);
        return dialog;
    }

    public void setPlayAgainListener(View.OnClickListener playAgainListener) {
        this.playAgainListener = playAgainListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            level = arguments.getInt(ARG_LEVEL);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View mDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_result_classic, null);

        TextView tv_result_1 = (TextView) mDialogView.findViewById(R.id.tv_result_1);
        TextView tv_result_3b = (TextView) mDialogView.findViewById(R.id.tv_result_3b);

        tv_result_1.setText(String.valueOf(level));

        int actual_record = getActivity().getPreferences(Activity.MODE_PRIVATE).getInt("RECORD", 0);

        if (level > actual_record) {
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            getActivity().getPreferences(Activity.MODE_PRIVATE)
                    .edit()
                    .putInt("RECORD", level)
                    .putString("RECORD_DATE", formattedDate)
                    .apply();

            if (actual_record > 0) {  //There is already a saved result from a previous game
                tv_result_3b.setVisibility(View.VISIBLE);
                tv_result_3b.setTextColor(Color.parseColor("#6DDC0C"));
                tv_result_3b.setText(" (+" + String.valueOf(level - actual_record) + ")");
            } else {
                tv_result_3b.setVisibility(View.GONE);
            }
            //If new record and we have the user's email either because they are signed in or because they were signed in and saved
            //their email in SharedPreferences
        } else if (level == actual_record) {
            if (actual_record > 0) {  //There is already a saved result from a previous game
                tv_result_3b.setVisibility(View.VISIBLE);
                tv_result_3b.setTextColor(Color.parseColor("#0BB3DC"));
                tv_result_3b.setText(" (=" + level + ")");
            } else {
                tv_result_3b.setVisibility(View.GONE);
            }
        } else {
            if (actual_record > 0) {  //There is already a saved result from a previous game
                tv_result_3b.setVisibility(View.VISIBLE);
                tv_result_3b.setTextColor(Color.parseColor("#EE5F27"));
                tv_result_3b.setText(" (" + String.valueOf(level - actual_record) + ")");
            } else {
                tv_result_3b.setVisibility(View.GONE);
            }
        }

        mDialogView.findViewById(R.id.btn_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (playAgainListener != null) {
                    playAgainListener.onClick(view);
                }
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(mDialogView)
                .create();
    }
}