package com.ue.colorful.feature.game.ltcolor

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.dialog_game_over.view.*

class GameOverDialog : DialogFragment() {
    private var points: Int = 0
    private var best: Int = 0
    private var level: Int = 0
    private var newScore: Boolean = false
    private var shown = false
    private var mode: BasePhunFragment.GameMode? = null
    private var replayListener: View.OnClickListener? = null

    companion object {
        private val ARG_POINTS = "arg_points"
        private val ARG_LEVEL = "arg_level"
        private val ARG_BEST = "arg_best"
        private val ARG_NEW_SCORE = "arg_new_score"
        private val ARG_MODE = "arg_mode"

        fun newInstance(points: Int, level: Int, best: Int, newScore: Boolean, gameMode: String): GameOverDialog {
            val dialog = GameOverDialog()
            val arguments = Bundle()
            arguments.putInt(ARG_POINTS, points)
            arguments.putInt(ARG_LEVEL, level)
            arguments.putInt(ARG_BEST, best)
            arguments.putBoolean(ARG_NEW_SCORE, newScore)
            arguments.putString(ARG_MODE, gameMode)
            dialog.arguments = arguments
            return dialog
        }
    }

    fun setReplayListener(replayListener: View.OnClickListener) {
        this.replayListener = replayListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val timesPlayed = SPUtils.getInt(SPKeys.TIMES_PLAYED, 0)
        SPUtils.putInt(SPKeys.TIMES_PLAYED, timesPlayed + 1)

        // get data
        points = arguments.getInt(ARG_POINTS)
        level = arguments.getInt(ARG_LEVEL)
        best = arguments.getInt(ARG_BEST)
        newScore = arguments.getBoolean(ARG_NEW_SCORE)
        mode = BasePhunFragment.GameMode.valueOf(arguments.getString(ARG_MODE))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_game_over, null)
        // set data
        rootView.tvPointsBox.text = String.format("%03d", points)
        rootView.tvBestBox.text = String.format("%03d", best)
        rootView.tvLevelIndicator.text = "Level $level"

        rootView.tvHighScore.visibility = if (newScore) View.VISIBLE else View.INVISIBLE
        rootView.btnReplay.setOnClickListener { replayListener?.onClick(null) }

        if (!shown) {
            shown = true
            val pointsAnim = getCounterAnimator(rootView.tvPointsBox, points)
            pointsAnim.duration = 1200

            // animate high score text
            if (newScore) {
                val highScoreAnim = ObjectAnimator.ofFloat(rootView.tvHighScore, "alpha", 0f, 1f)
                highScoreAnim.duration = 600
                highScoreAnim.start()
            }
            pointsAnim.start()
        }

        return AlertDialog.Builder(context)
                .setView(rootView)
                .create()
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
}
