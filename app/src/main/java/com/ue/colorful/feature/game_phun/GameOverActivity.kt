package com.ue.colorful.feature.game_phun

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView

import com.ue.colorful.R

class GameOverActivity : FragmentActivity() {

    private var points: Int = 0
    private var best: Int = 0
    private var level: Int = 0
    private var newScore: Boolean = false
    private var shown = false
    private var pointsBox: TextView? = null
    private var highScoreText: TextView? = null
    private var sharedPreferences: SharedPreferences? = null
    private var mode: MainGameActivity.GameMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        val levelIndicator = findViewById<View>(R.id.level_indicator) as TextView
        pointsBox = findViewById<View>(R.id.points_box) as TextView
        val bestLabel = findViewById<View>(R.id.best_label) as TextView
        val bestBox = findViewById<View>(R.id.best_box) as TextView
        highScoreText = findViewById<View>(R.id.highscore_txt) as TextView
        val replayBtn = findViewById<View>(R.id.replay_btn) as Button

        // set a simple game counter in shared pref
        sharedPreferences = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val editor = sharedPreferences!!.edit()
        val timesPlayed = sharedPreferences!!.getInt("TIMESPLAYED", 0)
        editor.putInt("TIMESPLAYED", timesPlayed + 1)
        editor.apply()

        // get data
        val bundle = intent.extras
        points = bundle!!.getInt("points")
        level = bundle.getInt("level")
        best = bundle.getInt("best")
        newScore = bundle.getBoolean("newScore")
        mode = MainGameActivity.GameMode.valueOf(bundle.getString("gameMode"))

        // set data
        pointsBox!!.text = String.format("%03d", points)
        bestBox.text = String.format("%03d", best)
        levelIndicator.text = "Level " + Integer.toString(level)

        // show high score
        if (newScore) {
            highScoreText!!.visibility = View.VISIBLE
        } else {
            highScoreText!!.visibility = View.INVISIBLE
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus && !shown) {
            shown = true
            val pointsAnim = getCounterAnimator(pointsBox, points)
            pointsAnim.duration = 1200

            // animate high score text
            if (newScore) {
                val highScoreAnim = ObjectAnimator.ofFloat(highScoreText, "alpha", 0f, 1f)
                highScoreAnim.duration = 600
                highScoreAnim.start()
            }
            pointsAnim.start()
        }
    }

    internal fun getCounterAnimator(view: TextView?, maxValue: Int): ValueAnimator {
        val anim = ValueAnimator.ofInt(0, 1)
        anim.interpolator = DecelerateInterpolator()
        anim.addUpdateListener { valueAnimator ->
            val `val` = (maxValue * valueAnimator.animatedFraction).toInt()
            view!!.text = String.format("%03d", `val`)
        }
        return anim
    }

    fun playGame(view: View) {
        if (mode == MainGameActivity.GameMode.EASY) {
            startActivity(Intent(this, EasyGameActivity::class.java))
        } else {
            startActivity(Intent(this, HardGameActivity::class.java))
        }
        finish()
    }
}
