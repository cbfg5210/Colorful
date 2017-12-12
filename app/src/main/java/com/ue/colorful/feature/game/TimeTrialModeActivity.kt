package com.ue.colorful.feature.game

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.ue.colorful.R
import kotlinx.android.synthetic.main.activity_time_trial_mode.*
import java.util.*

class TimeTrialModeActivity : AppCompatActivity(), View.OnClickListener {
    private var buttonsInRow: Int = 0
    private var randomButton: Int = 0
    private var width: Int = 0
    private var level: Int = 0

    private val arrays = Arrays()
    private val r = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_trial_mode)

        tvLevel.text = "10"

        btnRedraw.setOnClickListener {
            linearLayoutTags.removeAllViews()
            drawMap(HARD, 7)
        }

        btnHalf.setOnClickListener { halfTiles(49) }
        startGame()
    }

    private fun drawMap(level: Int, buttons: Int) {
        val color0: String
        val color1: String

        buttonsInRow = buttons
        randomButton = r.nextInt(buttonsInRow * buttonsInRow) + 1

        when (level) {
            MEDIUM -> {
                val randomColor = r.nextInt(arrays.mediumColors.size)
                color0 = arrays.getMediumColor0(randomColor)
                color1 = arrays.getMediumColor1(randomColor)
            }
            else -> {
                val randomColor = r.nextInt(arrays.hardColors.size)
                color0 = arrays.getHardColor0(randomColor)
                color1 = arrays.getHardColor1(randomColor)
            }
        }

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
                drawable.setColor(Color.parseColor("#" + color0))
                btn.setOnClickListener(this)
                row.addView(btn)
            }

            linearLayoutTags.addView(row)
        }
        val b = linearLayoutTags.findViewById<View>(randomButton) as Button
        val drawable2 = b.background as GradientDrawable
        drawable2.setColor(Color.parseColor("#" + color1))
    }

    override fun onClick(view: View) {
        (view as Button).text = "*"
        view.setEnabled(false)

        val myId = view.getId()
        if (myId == randomButton) {
            linearLayoutTags.removeAllViews()

            level--
            if (level > 7) {
                drawMap(MEDIUM, 7)
            } else if (level > 0 && level <= 7) {
                drawMap(HARD, 7)
            } else {
                gameOver()
            }

            tvLevel.text = level.toString()
        } else {
            val b3 = linearLayoutTags.findViewById<View>(myId) as Button
            val drawable3 = b3.background as GradientDrawable
            drawable3.setColor(Color.BLACK)
        }
    }

    fun halfTiles(num: Int) {
        val r = Random()
        var random_tile: Int

        val list = ArrayList<Int>()
        for (i in 1..num) {
            list.add(i)
        }

        val x = list.size / 2 + 1

        val list2 = ArrayList<Int>()
        run {
            var i = 1
            while (i < x) {
                random_tile = r.nextInt(list.size) + 1
                if (random_tile == randomButton) {
                    i--
                } else if (!list2.contains(random_tile)) {
                    list2.add(random_tile)
                } else {
                    i--
                }
                i++
            }
        }

        for (i in list.indices) {
            for (j in list2.indices) {
                if (list[i] === list2[j]) {
                    val b2 = linearLayoutTags.findViewById<View>(list[i]) as Button
                    b2.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun gameOver() {
        chronometerText.stop()

        val dialog = TimeTrialResultDialog.newInstance(chronometerText.text.toString())
        dialog.setPlayAgainListener(View.OnClickListener { startGame() })
        dialog.show(supportFragmentManager, "")
    }

    fun startGame() {
        chronometerText.start()

        width = resources.displayMetrics.widthPixels * 9 / 10
        linearLayoutTags.removeAllViews()

        level = 10
        tvLevel.text = level.toString()
        drawMap(HARD, 7)
    }

    public override fun onPause() {
        super.onPause()
        chronometerText.stop()
    }

    public override fun onStop() {
        super.onStop()
        chronometerText.stop()
    }

    public override fun onResume() {
        super.onResume()
        if (chronometerText.timeElapsed > 0) {
            chronometerText.resume()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        chronometerText.stop()
    }

    companion object {
        private val MEDIUM = 2
        private val HARD = 3
    }
}
