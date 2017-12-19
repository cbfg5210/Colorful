package com.ue.colorful.feature.game.diffcolor

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import com.ue.colorful.feature.main.BaseFragment
import kotlinx.android.synthetic.main.fragment_classic_mode.*
import kotlinx.android.synthetic.main.fragment_classic_mode.view.*
import java.util.*

class ClassicModeFragment : BaseFragment(R.layout.fragment_classic_mode, R.menu.menu_game_diffcolor), View.OnClickListener {
    private val TOTAL_TIME = 30
    private var countDownTimer: CountDownTimer? = null
    private var seconds: Int = 0
    private var buttonsInRow: Int = 0
    private var randomButton: Int = 0
    private var width: Int = 0
    private var level: Int = 0

    private val arrays = Arrays()
    private val r = Random()

    override fun initViews() {
        width = resources.displayMetrics.widthPixels * 9 / 10

        rootView.tvStartGame.setOnClickListener { startGame() }

        drawMap(HARD, 2)
        rootView.linearLayoutTags.findViewById<View>(randomButton).isEnabled = false
    }

    private fun drawMap(level: Int, buttons: Int) {
        rootView.linearLayoutTags.removeAllViews()

        val color1: String
        val color2: String

        when (level) {
            EASY -> {
                val randomColor = r.nextInt(arrays.easyColors.size)
                color1 = arrays.getEasyColor0(randomColor)
                color2 = arrays.getEasyColor1(randomColor)
            }
            MEDIUM -> {
                val randomColor = r.nextInt(arrays.mediumColors.size)
                color1 = arrays.getMediumColor0(randomColor)
                color2 = arrays.getMediumColor1(randomColor)
            }
            HARD -> {
                val randomColor = r.nextInt(arrays.hardColors.size)
                color1 = arrays.getHardColor0(randomColor)
                color2 = arrays.getHardColor1(randomColor)
            }
            else -> {
                val randomColor = r.nextInt(arrays.veryHardColors.size)
                color1 = arrays.getVeryHardColor0(randomColor)
                color2 = arrays.getVeryHardColor1(randomColor)
            }
        }

        buttonsInRow = buttons
        randomButton = r.nextInt(buttonsInRow * buttonsInRow) + 1

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

                val drawable = btn.background as GradientDrawable
                drawable.setColor(Color.parseColor("#$color1"))

                btn.setOnClickListener(this)

                row.addView(btn)
            }

            rootView.linearLayoutTags.addView(row)
        }
        val b = rootView.linearLayoutTags.findViewById<View>(randomButton) as Button
        val drawable2 = b.background as GradientDrawable
        drawable2.setColor(Color.parseColor("#$color2"))
    }

    override fun onClick(view: View) {
        (view as Button).text = "*"
        view.isEnabled = false

        if (view.id == randomButton) {
            level++
            rootView.tvLevel.text = level.toString()
            when (level) {
                1 -> drawMap(EASY, 2)
                2 -> drawMap(EASY, 3)
                3 -> drawMap(EASY, 4)
                4 -> drawMap(MEDIUM, 5)
                5 -> drawMap(MEDIUM, 6)
                12 - 21 -> drawMap(HARD, 7)
                else -> drawMap(VERY_HARD, 7)
            }
        } else (view.background as GradientDrawable).setColor(Color.BLACK)
    }

    private fun gameOver() {
        cancelCountDown()
        rootView.linearLayoutTags.findViewById<View>(randomButton).isEnabled = false
        rootView.tvStartGame.visibility = View.VISIBLE

        level--
        tvTime.text = "00:00"

        containerCallback?.gameOver(Constants.Companion.GAME_DIFF_CLASSIC, level.toLong())
    }

    private fun startGame() {
        rootView.tvStartGame.visibility = View.INVISIBLE
        seconds = TOTAL_TIME
        level = 1

        rootView.tvLevel.text = level.toString()
        drawMap(HARD, 2)

        cancelCountDown()
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTime.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
                seconds--
            }

            override fun onFinish() {
                gameOver()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelCountDown()
    }

    private fun cancelCountDown() {
        countDownTimer?.cancel()
    }

    companion object {
        private val EASY = 1
        private val MEDIUM = 2
        private val HARD = 3
        private val VERY_HARD = 4
    }
}