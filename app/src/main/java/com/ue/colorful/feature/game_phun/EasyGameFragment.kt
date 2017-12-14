package com.ue.colorful.feature.game_phun

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import kotlinx.android.synthetic.main.fragment_easy_game.view.*

class EasyGameFragment : BasePhunFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_easy_game, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProgressView()

        POINT_INCREMENT = 2
        TIMER_BUMP = 2

        gameMode = BasePhunFragment.GameMode.EASY

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
        if (alpha1 < alpha2) {
            updatePoints()
        } else { // incorrect guess
            endGame()
        }
    }
}