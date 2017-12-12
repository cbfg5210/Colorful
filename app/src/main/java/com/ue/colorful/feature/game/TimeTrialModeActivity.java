package com.ue.colorful.feature.game;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ue.colorful.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimeTrialModeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MEDIUM = 2;
    private static final int HARD = 3;

    private TextView tv_level;
    private Chronometer chronometer;
    private Button btn_redraw, btn_half;

    private int buttons_in_row;
    private int random_button;
    private int width;
    private int level;
    private LinearLayout layout;

    private Arrays arrays = new Arrays();
    private Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_trial_mode);

        chronometer = (Chronometer) findViewById(R.id.chronometer1);
        tv_level = (TextView) findViewById(R.id.tv_level);
        tv_level.setText("10");
        btn_redraw = (Button) findViewById(R.id.btn_redraw);
        btn_half = (Button) findViewById(R.id.btn_half);

        layout = (LinearLayout) findViewById(R.id.linear_layout_tags);

        btn_redraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.removeAllViews();
                drawMap(HARD, 7);

            }
        });

        btn_half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                halfTiles(49);
            }
        });
        startGame();
    }

    private void drawMap(int level, int buttons) {
        String color0, color1;

        buttons_in_row = buttons;
        random_button = (r.nextInt(buttons_in_row * buttons_in_row) + 1);

        if (level == MEDIUM) {
            int random_color = r.nextInt(arrays.colors_medium.length);
            color0 = arrays.getMediumColor0(random_color);
            color1 = arrays.getMediumColor1(random_color);
        } else {
            int random_color = r.nextInt(arrays.colors_hard.length);
            color0 = arrays.getHardColor0(random_color);
            color1 = arrays.getHardColor1(random_color);
        }

        for (int i = 0; i < buttons_in_row; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < buttons_in_row; j++) {
                Button btn = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                params.setMargins(5, 5, 5, 5);
                btn.setLayoutParams(params);
                btn.setId(j + 1 + (i * buttons_in_row));
                btn.setWidth(width / buttons_in_row);
                btn.setHeight(width / buttons_in_row);
                btn.setBackgroundResource(R.drawable.button_wrong);
                GradientDrawable drawable = (GradientDrawable) btn.getBackground();
                drawable.setColor(Color.parseColor("#" + color0));
                btn.setOnClickListener(this);
                row.addView(btn);
            }

            layout.addView(row);
        }
        Button b = (Button) layout.findViewById(random_button);
        GradientDrawable drawable2 = (GradientDrawable) b.getBackground();
        drawable2.setColor(Color.parseColor("#" + color1));
    }

    public void onClick(View view) {
        ((Button) view).setText("*");
        view.setEnabled(false);

        int myId = view.getId();
        if (myId == random_button) {

            layout.removeAllViews();

            level--;
            if (level > 7) {
                drawMap(MEDIUM, 7);
            } else if (level > 0 && level <= 7) {
                drawMap(HARD, 7);
            } else {
                gameOver();
            }

            tv_level.setText(String.valueOf(level));
        } else {
            Button b3 = (Button) layout.findViewById(myId);
            GradientDrawable drawable3 = (GradientDrawable) b3.getBackground();
            drawable3.setColor(Color.BLACK);
        }

    }

    public void halfTiles(int num) {

        Random r = new Random();
        int random_tile;

        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            list.add(i);
        }

        int x = list.size() / 2 + 1;

        List<Integer> list2 = new ArrayList<>();
        for (int i = 1; i < x; i++) {
            random_tile = (r.nextInt(list.size()) + 1);
            if (random_tile == random_button) {
                i--;
            } else if (!list2.contains(random_tile)) {
                list2.add(random_tile);
            } else {
                i--;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list.get(i) == list2.get(j)) {
                    Button b2 = (Button) layout.findViewById(list.get(i));
                    b2.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void gameOver() {
        if (chronometer != null) chronometer.stop();

        TimeTrialResultDialog dialog = TimeTrialResultDialog.newInstance(chronometer.getText().toString());
        dialog.setPlayAgainListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }

    public void startGame() {
        if (chronometer != null) chronometer.start();

        width = (getResources().getDisplayMetrics().widthPixels * 9) / 10;
        layout.removeAllViews();

        level = 10;
        tv_level.setText(String.valueOf(level));
        drawMap(HARD, 7);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("onononT", "onPause");
        //if user opens another app then we need to set this otherwise Connected to Google+ msg will be displayed
        if (chronometer != null) chronometer.stop();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("onononT", "onStop");
        if (chronometer != null) chronometer.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onononT", "onResume");
        //for now when the user comes back from another app we don't stop the clock
        if (chronometer.getTimeElapsed() > 0) {
            chronometer.resume();
            //Log.i("getTimeElapsed", String.valueOf(chronometer.getTimeElapsed())); //11178 = 11 s 178 ms, 8301 = 8s301ms
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chronometer != null) chronometer.stop();
    }
}
