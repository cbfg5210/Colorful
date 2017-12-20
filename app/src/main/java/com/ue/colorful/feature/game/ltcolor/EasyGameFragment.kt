package com.ue.colorful.feature.game.ltcolor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import kotlinx.android.synthetic.main.fragment_easy_game.view.*

class EasyGameFragment : BaseLtGameFragment(R.layout.fragment_easy_game, R.menu.menu_game_ltcolor) {

    override fun initViews() {
        setupProgressView()

        gameMode = Constants.GAME_LT_EASY

        rootView.topButton.setOnClickListener(this)
        rootView.bottomButton.setOnClickListener(this)

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

        rootView.topButton.setBackgroundColor(Color.argb(alpha1, red, green, blue))
        rootView.bottomButton.setBackgroundColor(Color.argb(alpha2, red, green, blue))
    }

    override fun calculatePoints(clickedView: View) {
        val unClickedView = if (clickedView === rootView.topButton) rootView.bottomButton else rootView.topButton
        val clickedColor = clickedView.background as ColorDrawable
        val unClickedColor = unClickedView.background as ColorDrawable

        val alpha1 = Color.alpha(clickedColor.color)
        val alpha2 = Color.alpha(unClickedColor.color)

        // correct guess
        if (alpha1 < alpha2) updatePoints()
        else endGame()
    }
}