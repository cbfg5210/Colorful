package com.ue.colorful.feature.game_phun

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.ue.colorful.R
import kotlinx.android.synthetic.main.activity_easy_game.*

class EasyGameActivity : BasePhunActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_easy_game)
        setupProgressView()

        POINT_INCREMENT = 2
        TIMER_BUMP = 2

        gameMode = BasePhunActivity.GameMode.EASY

        topButton.setOnClickListener(this)
        bottomButton.setOnClickListener(this)

        // bootstrap game
        resetGame()
        setupGameLoop()
        startGame()
    }

    override fun onClick(view: View) {
        if (!gameStart) return
        calculatePoints(view)
        setColorsOnButtons()
    }

    override fun setColorsOnButtons() {
        val color = Color.parseColor(BetterColor.color)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)

        val alpha1: Int
        val alpha2: Int
        if (Math.random() > 0.5) {
            alpha1 = 255
            alpha2 = 185
        } else {
            alpha1 = 185
            alpha2 = 255
        }

        topButton.setBackgroundColor(Color.argb(alpha1, red, green, blue))
        bottomButton.setBackgroundColor(Color.argb(alpha2, red, green, blue))
    }

    override fun calculatePoints(clickedView: View) {
        val unClickedView = if (clickedView === topButton) bottomButton else topButton
        val clickedColor = clickedView.background as ColorDrawable
        val unClickedColor = unClickedView.background as ColorDrawable

        val alpha1 = Color.alpha(clickedColor.color)
        val alpha2 = Color.alpha(unClickedColor.color)

        // correct guess
        if (alpha1 < alpha2) {
            updatePoints()
        } else { // incorrect guess
            endGame()
        }
    }
}