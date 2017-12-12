package com.ue.colorful.feature.game

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.ue.colorful.R
import kotlinx.android.synthetic.main.activity_classic_mode.*
import java.util.*

class ClassicModeActivity : AppCompatActivity(), View.OnClickListener {
    private var countDownTimer: CountDownTimer? = null
    private var seconds: Int = 0
    private var buttonsInRow: Int = 0
    private var randomButton: Int = 0
    private var width: Int = 0
    private var level: Int = 0

    private val arrays = Arrays()
    private val r = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classic_mode)

        tvPause.setOnClickListener { toggleCountDownSwitch(!tvPause.isSelected) }

        btnRedraw.setOnClickListener {
            changeTimer(-1)

            linearLayoutTags.removeAllViews()

            when (level) {
                1 -> drawMap(EASY, 2)
                2 -> drawMap(EASY, 3)
                3 -> drawMap(MEDIUM, 4)
                4 -> drawMap(MEDIUM, 5)
                5 -> drawMap(MEDIUM, 6)
                6 - 9 -> drawMap(HARD, 7)
                else -> drawMap(VERY_HARD, 7)
            }
        }

        btnHalf.setOnClickListener {
            when (level) {
                1 -> halfTiles(4)
                2 -> halfTiles(9)
                3 -> halfTiles(16)
                4 -> halfTiles(25)
                5 -> halfTiles(36)
                6 - 11 -> halfTiles(49)
                else -> halfTiles(49)
            }
        }
    }

    private fun drawMap(level: Int, buttons: Int) {
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
            val row = LinearLayout(this)
            row.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            for (j in 0 until buttonsInRow) {
                val btn = Button(this)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                params.setMargins(5, 5, 5, 5)
                btn.layoutParams = params
                btn.id = j + 1 + i * buttonsInRow
                btn.width = width / buttonsInRow
                btn.height = width / buttonsInRow
                btn.setBackgroundResource(R.drawable.button_wrong)
                val drawable = btn.background as GradientDrawable
                drawable.setColor(Color.parseColor("#" + color1))
                btn.setOnClickListener(this)
                row.addView(btn)
            }

            linearLayoutTags.addView(row)
        }
        val b = linearLayoutTags.findViewById<View>(randomButton) as Button
        val drawable2 = b.background as GradientDrawable
        drawable2.setColor(Color.parseColor("#" + color2))
    }

    override fun onClick(view: View) {
        (view as Button).text = "*"
        view.setEnabled(false)

        val myId = view.getId()
        if (myId == randomButton) {
            linearLayoutTags.removeAllViews()

            level++
            tvLevel.text = level.toString()
            when (level) {
                1 -> drawMap(EASY, 2)
                2 -> drawMap(EASY, 3)
                3 -> drawMap(EASY, 4)
                4 -> drawMap(MEDIUM, 5)
                5 -> drawMap(MEDIUM, 6)
                12 - 21 -> drawMap(HARD, 7)
                else -> drawMap(VERY_HARD, 7)
            }
        } else {
            val b3 = linearLayoutTags.findViewById<View>(myId) as Button
            val drawable3 = b3.background as GradientDrawable
            drawable3.setColor(Color.BLACK)
        }
    }

    fun halfTiles(num: Int) {
        changeTimer(0)

        val r = Random()
        var randomTile: Int

        val list = ArrayList<Int>()
        for (i in 1..num) {
            list.add(i)
        }
        val x = list.size / 2 + 1

        val list2 = ArrayList<Int>()
        run {
            var i = 1
            while (i < x) {
                randomTile = r.nextInt(list.size) + 1
                if (randomTile == randomButton) {
                    i--
                } else if (!list2.contains(randomTile)) {
                    list2.add(randomTile)
                } else {
                    i--
                }
                i++
            }
        }

        for (i in list.indices) {
            for (j in list2.indices) {
                if (list[i] === list2[j]) {
                    linearLayoutTags.findViewById<View>(list[i]).visibility = View.INVISIBLE
                }
            }
        }
    }

    fun changeTimer(sec: Int) {
        seconds += sec
        toggleCountDownSwitch(true)
    }

    fun gameOver() {
        cancelCountDown()
        level--
        tvTime.text = "00:00"

        val dialog = ClassicResultDialog.newInstance(level)
        dialog.setPlayAgainListener(View.OnClickListener { startGame() })
        dialog.show(supportFragmentManager, "")
    }

    private fun toggleCountDownSwitch(startCountDown: Boolean) {
        cancelCountDown()
        tvPause.isSelected = startCountDown
        if (!startCountDown) {
            tvPause.text = "Play"
            return
        }
        tvPause.text = "Pause"
        countDownTimer = object : CountDownTimer((seconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTime.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
                seconds--
            }

            override fun onFinish() {
                gameOver()
            }
        }.start()
    }

    fun startGame() {
        seconds = 30
        toggleCountDownSwitch(true)

        width = resources.displayMetrics.widthPixels * 9 / 10
        linearLayoutTags.removeAllViews()

        level = 1
        tvLevel.text = level.toString()
        drawMap(HARD, 2)
    }

    public override fun onPause() {
        super.onPause()
        cancelCountDown()
    }

    public override fun onStop() {
        super.onStop()
        cancelCountDown()
    }

    override fun onResume() {
        super.onResume()
        if (seconds > 0) {
            toggleCountDownSwitch(true)
        } else {
            startGame()
        }
    }

    public override fun onDestroy() {
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
