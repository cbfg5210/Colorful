package com.ue.colorful.feature.game_phun

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.activity_game_over.*

class GameOverActivity : AppCompatActivity() {
    private var points: Int = 0
    private var best: Int = 0
    private var level: Int = 0
    private var newScore: Boolean = false
    private var shown = false
    private var mode: BasePhunActivity.GameMode? = null

    companion object {
        private val ARG_POINTS = "arg_points"
        private val ARG_LEVEL = "arg_level"
        private val ARG_BEST = "arg_best"
        private val ARG_NEW_SCORE = "arg_new_score"
        private val ARG_MODE = "arg_mode"

        fun start(context: Context, points: Int, level: Int, best: Int, newScore: Boolean, gameMode: String) {
            val intent = Intent(context, GameOverActivity::class.java)
            intent.putExtra(ARG_POINTS, points)
            intent.putExtra(ARG_LEVEL, level)
            intent.putExtra(ARG_BEST, best)
            intent.putExtra(ARG_NEW_SCORE, newScore)
            intent.putExtra(ARG_MODE, gameMode)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        val timesPlayed = SPUtils.getInt(SPKeys.TIMES_PLAYED, 0)
        SPUtils.putInt(SPKeys.TIMES_PLAYED, timesPlayed + 1)

        // get data
        val bundle = intent.extras
        points = bundle!!.getInt(ARG_POINTS)
        level = bundle.getInt(ARG_LEVEL)
        best = bundle.getInt(ARG_BEST)
        newScore = bundle.getBoolean(ARG_NEW_SCORE)
        mode = BasePhunActivity.GameMode.valueOf(bundle.getString(ARG_MODE))

        // set data
        tvPointsBox.text = String.format("%03d", points)
        tvBestBox.text = String.format("%03d", best)
        tvLevelIndicator.text = "Level " + Integer.toString(level)

        tvHighScore.visibility = if (newScore) View.VISIBLE else View.INVISIBLE
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus && !shown) {
            shown = true
            val pointsAnim = getCounterAnimator(tvPointsBox, points)
            pointsAnim.duration = 1200

            // animate high score text
            if (newScore) {
                val highScoreAnim = ObjectAnimator.ofFloat(tvHighScore, "alpha", 0f, 1f)
                highScoreAnim.duration = 600
                highScoreAnim.start()
            }
            pointsAnim.start()
        }
    }

    internal fun getCounterAnimator(view: TextView, maxValue: Int): ValueAnimator {
        val anim = ValueAnimator.ofInt(0, 1)
        anim.interpolator = DecelerateInterpolator()
        anim.addUpdateListener { valueAnimator ->
            val value = (maxValue * valueAnimator.animatedFraction).toInt()
            view.text = String.format("%03d", value)
        }
        return anim
    }

    fun playGame(view: View) {
        val intent =
                if (mode == BasePhunActivity.GameMode.EASY) Intent(this, EasyGameActivity::class.java)
                else Intent(this, HardGameActivity::class.java)

        startActivity(intent)
        finish()
    }
}
