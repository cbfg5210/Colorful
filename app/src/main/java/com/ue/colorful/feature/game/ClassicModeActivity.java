package com.ue.colorful.feature.game;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ue.colorful.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassicModeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int EASY = 1;
    private static final int MEDIUM = 2;
    private static final int HARD = 3;
    private static final int VERY_HARD = 4;

    private TextView tv_time, tv_level, tv_pause;

    private CountDownTimer countDownTimer;
    private int seconds;
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
        setContentView(R.layout.activity_classic_mode);

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_level = (TextView) findViewById(R.id.tv_level);

        layout = (LinearLayout) findViewById(R.id.linear_layout_tags);
        tv_pause = (TextView) findViewById(R.id.tv_pause);

        tv_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCountDownSwitch(!tv_pause.isSelected());
            }
        });


        findViewById(R.id.btn_redraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTimer(-1);

                layout.removeAllViews();

                if (level == 1) {
                    drawMap(EASY, 2);
                } else if (level == 2) {
                    drawMap(EASY, 3);
                } else if (level == 3) {
                    drawMap(MEDIUM, 4);
                } else if (level == 4) {
                    drawMap(MEDIUM, 5);
                } else if (level == 5) {
                    drawMap(MEDIUM, 6);
                } else if (level >= 6 && level < 10) {
                    drawMap(HARD, 7);
                } else {
                    drawMap(VERY_HARD, 7);
                }
            }
        });

        findViewById(R.id.btn_half).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (level == 1) {
                    halfTiles(4);
                } else if (level == 2) {
                    halfTiles(9);
                } else if (level == 3) {
                    halfTiles(16);
                } else if (level == 4) {
                    halfTiles(25);
                } else if (level == 5) {
                    halfTiles(36);
                } else if (level >= 6 && level < 12) {
                    halfTiles(49);
                } else {
                    halfTiles(49);
                }
            }
        });
    }

    private void drawMap(int level, int buttons) {
        String color1;
        String color2;

        if (level == EASY) {
            int random_color = r.nextInt(arrays.getEasyColors().length);
            color1 = arrays.getEasyColor0(random_color);
            color2 = arrays.getEasyColor1(random_color);
        } else if (level == MEDIUM) {
            int random_color = r.nextInt(arrays.getMediumColors().length);
            color1 = arrays.getMediumColor0(random_color);
            color2 = arrays.getMediumColor1(random_color);
        } else if (level == HARD) {
            int random_color = r.nextInt(arrays.getHardColors().length);
            color1 = arrays.getHardColor0(random_color);
            color2 = arrays.getHardColor1(random_color);
        } else {
            int random_color = r.nextInt(arrays.getVeryHardColors().length);
            color1 = arrays.getVeryHardColor0(random_color);
            color2 = arrays.getVeryHardColor1(random_color);
        }

        buttons_in_row = buttons;
        random_button = (r.nextInt(buttons_in_row * buttons_in_row) + 1);

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
                drawable.setColor(Color.parseColor("#" + color1));
                btn.setOnClickListener(this);
                row.addView(btn);
            }

            layout.addView(row);
        }
        Button b = (Button) layout.findViewById(random_button);
        GradientDrawable drawable2 = (GradientDrawable) b.getBackground();
        drawable2.setColor(Color.parseColor("#" + color2));
    }

    public void onClick(View view) {
        ((Button) view).setText("*");
        view.setEnabled(false);

        int myId = view.getId();
        if (myId == random_button) {
            layout.removeAllViews();

            level++;
            if (level == 1) {
                drawMap(EASY, 2);
            } else if (level == 2) {
                drawMap(EASY, 3);
            } else if (level == 3) {
                drawMap(EASY, 4);
            } else if (level == 4) {
                drawMap(MEDIUM, 5);
            } else if (level == 5) {
                drawMap(MEDIUM, 6);
            } else if (level >= 6 && level < 12) {
                drawMap(MEDIUM, 7);
            } else if (level >= 12 && level < 22) {
                drawMap(HARD, 7);
            } else {
                drawMap(VERY_HARD, 7);
            }
            tv_level.setText(String.valueOf(level));
        } else {
            Button b3 = (Button) layout.findViewById(myId);
            GradientDrawable drawable3 = (GradientDrawable) b3.getBackground();
            drawable3.setColor(Color.BLACK);
        }
    }

    public void halfTiles(int num) {
        changeTimer(0);

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
                    layout.findViewById(list.get(i)).setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void changeTimer(int sec) {
        seconds += sec;
        toggleCountDownSwitch(true);
    }

    public void gameOver() {
        cancelCountDown();
        level--;
        tv_time.setText("00:00");

        ClassicResultDialog dialog = ClassicResultDialog.Companion.newInstance(level);
        dialog.setPlayAgainListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }

    private void toggleCountDownSwitch(boolean startCountDown) {
        cancelCountDown();
        tv_pause.setSelected(startCountDown);
        if (!startCountDown) {
            tv_pause.setText("Play");
            return;
        }
        tv_pause.setText("Pause");
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_time.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
                seconds--;
            }

            public void onFinish() {
                gameOver();
            }
        }.start();
    }

    public void startGame() {
        seconds = 30;
        toggleCountDownSwitch(true);

        width = (getResources().getDisplayMetrics().widthPixels * 9) / 10;
        layout.removeAllViews();

        level = 1;
        tv_level.setText(String.valueOf(level));
        drawMap(HARD, 2);
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelCountDown();
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelCountDown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (seconds > 0) {
            toggleCountDownSwitch(true);
        } else {
            startGame();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelCountDown();
    }

    private void cancelCountDown() {
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
