package com.ue.colorful.feature.game_phun;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.ue.colorful.R;

public class GameOverActivity extends FragmentActivity {

    private int points, best, level;
    private boolean newScore;
    private boolean shown = false;
    private TextView pointsBox, highScoreText;
    private SharedPreferences sharedPreferences;
    private MainGameActivity.GameMode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView levelIndicator = (TextView) findViewById(R.id.level_indicator);
        pointsBox = (TextView) findViewById(R.id.points_box);
        TextView bestLabel = (TextView) findViewById(R.id.best_label);
        TextView bestBox = (TextView) findViewById(R.id.best_box);
        highScoreText = (TextView) findViewById(R.id.highscore_txt);
        Button replayBtn = (Button) findViewById(R.id.replay_btn);

        // set a simple game counter in shared pref
        sharedPreferences = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        int timesPlayed = sharedPreferences.getInt("TIMESPLAYED", 0);
        editor.putInt("TIMESPLAYED", timesPlayed + 1);
        editor.apply();

        // get data
        Bundle bundle = getIntent().getExtras();
        points = bundle.getInt("points");
        level = bundle.getInt("level");
        best = bundle.getInt("best");
        newScore = bundle.getBoolean("newScore");
        mode = MainGameActivity.GameMode.valueOf(bundle.getString("gameMode"));

        // set data
        pointsBox.setText(String.format("%03d", points));
        bestBox.setText(String.format("%03d", best));
        levelIndicator.setText("Level " + Integer.toString(level));

        // show high score
        if (newScore) {
            highScoreText.setVisibility(View.VISIBLE);
        } else {
            highScoreText.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && !shown) {
            shown = true;
            ValueAnimator pointsAnim = getCounterAnimator(pointsBox, points);
            pointsAnim.setDuration(1200);

            // animate high score text
            if (newScore) {
                ObjectAnimator highScoreAnim = ObjectAnimator.ofFloat(highScoreText, "alpha", 0f, 1f);
                highScoreAnim.setDuration(600);
                highScoreAnim.start();
            }
            pointsAnim.start();
        }
    }

    ValueAnimator getCounterAnimator(final TextView view, final int maxValue) {
        ValueAnimator anim = ValueAnimator.ofInt(0, 1);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (int) (maxValue * valueAnimator.getAnimatedFraction());
                view.setText(String.format("%03d", val));
            }
        });
        return anim;
    }

    public void playGame(View view) {
        if (mode == MainGameActivity.GameMode.EASY) {
            startActivity(new Intent(this, EasyGameActivity.class));
        } else {
            startActivity(new Intent(this, HardGameActivity.class));
        }
        finish();
    }
}
