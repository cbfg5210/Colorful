package com.ue.colorful.feature.game.ltcolor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import com.ue.colorful.R
import kotlinx.android.synthetic.main.fragment_hard_game.*
import kotlinx.android.synthetic.main.fragment_hard_game.view.*
import java.util.*


class HardGameFragment : BaseLtGameFragment(R.layout.fragment_hard_game, R.menu.menu_game_ltcolor) {
    private lateinit var buttonList: ArrayList<Button>

    override fun initViews() {
        setupProgressView()

        POINT_INCREMENT = 4
        TIMER_BUMP = 2

        gameMode = BaseLtGameFragment.GameMode.HARD

        rootView.button_1.setOnClickListener(this)
        rootView.button_2.setOnClickListener(this)
        rootView.button_3.setOnClickListener(this)
        rootView.button_4.setOnClickListener(this)

        buttonList = ArrayList()
        buttonList.add(button_1)
        buttonList.add(button_2)
        buttonList.add(button_3)
        buttonList.add(button_4)

        // bootstrap game
        resetGame()
        setupGameLoop()
        startGame()
    }

    override fun setColorsOnButtons() {
        val color = Color.parseColor(BetterColor.color)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alphas = shuffledColors()

        for (i in alphas.indices) {
            val button = buttonList[i]
            button.setBackgroundColor(Color.argb(alphas[i], red, green, blue))
        }
    }

    override fun calculatePoints(clickedView: View) {
        val clickedColor = clickedView.background as ColorDrawable
        val clickedAlpha = Color.alpha(clickedColor.color)

        var lightestColor = clickedAlpha
        for (button in buttonList) {
            val color = button.background as ColorDrawable
            val alpha = Color.alpha(color.color)
            if (alpha < lightestColor) {
                lightestColor = alpha
            }
        }

        // correct guess
        if (lightestColor == clickedAlpha) {
            updatePoints()
        } else {
            // false - hard mode
            endGame()
        }
    }

    override fun onClick(view: View) {
        if (!gameStart) return
        calculatePoints(view)
        setColorsOnButtons()
    }


    // Fisher Yates shuffling algorithm
    private fun shuffledColors(): IntArray {
        val random = Random()
        val arr = intArrayOf(255, 185, 155, 225)
        for (i in arr.size - 1 downTo 1) {
            val j = random.nextInt(i)
            // swap i and j
            val tmp: Int
            tmp = arr[i]
            arr[i] = arr[j]
            arr[j] = tmp
        }
        return arr
    }
}
