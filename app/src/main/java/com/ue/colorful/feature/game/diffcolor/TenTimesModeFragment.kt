package com.ue.colorful.feature.game.diffcolor

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import com.ue.colorful.feature.main.BaseFragment
import kotlinx.android.synthetic.main.fragment_ten_times_mode.*
import kotlinx.android.synthetic.main.fragment_ten_times_mode.view.*
import java.util.*

class TenTimesModeFragment : BaseFragment(R.layout.fragment_ten_times_mode, R.menu.menu_game_diffcolor), View.OnClickListener {
    private val TOTAL_LEVEL = 10
    private var buttonsInRow: Int = 0
    private var randomButton: Int = 0
    private var width: Int = 0
    private var level: Int = 0

    private val arrays = Arrays()
    private val r = Random()

    override fun initViews() {
        width = resources.displayMetrics.widthPixels * 9 / 10
        rootView.tvLevel.text = TOTAL_LEVEL.toString()

        rootView.tvStartGame.setOnClickListener { startGame() }

        drawMap(HARD, 7)
        rootView.linearLayoutTags.findViewById<View>(randomButton).isEnabled = false
    }

    private fun drawMap(level: Int, buttons: Int) {
        rootView.linearLayoutTags.removeAllViews()

        val color0: String
        val color1: String

        buttonsInRow = buttons
        randomButton = r.nextInt(buttonsInRow * buttonsInRow) + 1

        if (level == MEDIUM) {
            val randomColor = r.nextInt(arrays.mediumColors.size)
            color0 = arrays.getMediumColor0(randomColor)
            color1 = arrays.getMediumColor1(randomColor)
        } else {
            val randomColor = r.nextInt(arrays.hardColors.size)
            color0 = arrays.getHardColor0(randomColor)
            color1 = arrays.getHardColor1(randomColor)
        }

        for (i in 0 until buttonsInRow) {
            val row = LinearLayout(activity)
            row.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            for (j in 0 until buttonsInRow) {
                val btn = Button(activity)

                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                params.setMargins(5, 5, 5, 5)
                btn.layoutParams = params

                btn.id = j + 1 + i * buttonsInRow
                btn.width = width / buttonsInRow
                btn.height = width / buttonsInRow

                btn.setBackgroundResource(R.drawable.button_wrong)
                (btn.background as GradientDrawable).setColor(Color.parseColor("#$color0"))

                btn.setOnClickListener(this)
                row.addView(btn)
            }

            rootView.linearLayoutTags.addView(row)
        }
        (rootView.linearLayoutTags.findViewById<View>(randomButton).background as GradientDrawable).setColor(Color.parseColor("#$color1"))
    }

    override fun onClick(view: View) {
        (view as Button).text = "*"
        view.isEnabled = false

        if (view.id == randomButton) {
            level--

            if (level > 7) drawMap(MEDIUM, 7)
            else if (level in 1..7) drawMap(HARD, 7)
            else gameOver()

            rootView.tvLevel.text = level.toString()
        } else (view.background as GradientDrawable).setColor(Color.BLACK)
    }

    private fun gameOver() {
        rootView.tvStartGame.visibility = View.VISIBLE
        rootView.chronometerText.stop()
        containerCallback?.gameOver(Constants.GAME_DIFF_TEN_TIMES, chronometerText.timeElapsed)
    }

    private fun startGame() {
        rootView.tvStartGame.visibility = View.INVISIBLE
        level = TOTAL_LEVEL
        rootView.chronometerText.start()
        rootView.tvLevel.text = level.toString()
        drawMap(HARD, 7)
    }

    override fun onDestroy() {
        super.onDestroy()
        rootView.chronometerText.stop()
    }

    companion object {
        private val MEDIUM = 2
        private val HARD = 3
    }
}