package com.ue.colorful.feature.game_phun

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ue.colorful.R
import kotlinx.android.synthetic.main.progress_area_layout.*

abstract class MainGameActivity : Activity(), View.OnClickListener {

    protected lateinit var pointAnim: AnimatorSet
    protected lateinit var levelAnim: AnimatorSet

    protected var level: Int = 0
    protected var points: Int = 0
    protected var gameStart = false
    protected lateinit var runnable: Runnable
    protected var timer: Int = 0
    protected var gameMode: GameMode? = null

    protected var POINT_INCREMENT: Int = 0
    protected var TIMER_BUMP: Int = 0

    protected lateinit var handler: Handler

    enum class GameMode {
        EASY, HARD
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
    }

    protected fun setupProgressView() {
        // setting up animations
        pointAnim = AnimatorInflater.loadAnimator(this, R.animator.points_animations) as AnimatorSet
        pointAnim.setTarget(tvPointsValue)
        levelAnim = AnimatorInflater.loadAnimator(this, R.animator.level_animations) as AnimatorSet
        levelAnim.setTarget(tvLevelValue)
    }

    override fun onRestart() {
        super.onRestart()
        endGame()
    }

    override fun onStop() {
        super.onStop()
        gameStart = false
    }

    protected fun setupGameLoop() {
        runnable = object : Runnable {
            override fun run() {
                while (timer > 0 && gameStart) {
                    synchronized(this) {
                        try {
                            (runnable as java.lang.Object).wait(FPS.toLong())
                        } catch (e: InterruptedException) {
                            Log.i("THREAD ERROR", e.message)
                        }

                        timer = timer + TIMER_DELTA
                        if (TIMER_DELTA > 0) {
                            TIMER_DELTA = -TIMER_DELTA / TIMER_BUMP
                        }
                    }
                    handler.post { pbTimerProgress.progress = timer }
                }
                if (gameStart) {
                    handler.post { endGame() }
                }
            }
        }

    }

    protected fun resetGame() {
        gameStart = false
        level = 1
        points = 0

        // update view
        tvPointsValue.text = Integer.toString(points)
        tvLevelValue.text = Integer.toString(level)
        pbTimerProgress.progress = 0
    }

    protected fun startGame() {
        gameStart = true

        Toast.makeText(this, R.string.game_help, Toast.LENGTH_SHORT).show()
        setColorsOnButtons()

        // start timer
        timer = START_TIMER
        val thread = Thread(runnable)
        thread.start()
    }

    protected fun endGame() {
        gameStart = false
        val highScore = saveAndGetHighScore()
        launchGameOver(highScore)
        finish()
    }

    private fun saveAndGetHighScore(): Int {
        val preferences = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        var highScore = preferences.getInt("HIGHSCORE", 0)

        if (points > highScore) {
            val editor = preferences.edit()
            editor.putInt("HIGHSCORE", points)
            editor.apply()
            highScore = points
        }
        return highScore
    }

    private fun launchGameOver(highScore: Int) {
        // Send data to another activity
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("points", points)
        intent.putExtra("level", level)
        intent.putExtra("best", highScore)
        intent.putExtra("newScore", highScore == points)
        intent.putExtra("gameMode", gameMode!!.name)
        startActivity(intent)
    }

    // called on correct guess
    fun updatePoints() {
        points = points + POINT_INCREMENT
        TIMER_DELTA = -TIMER_BUMP * TIMER_DELTA // give a timer bump
        tvPointsValue.text = Integer.toString(points)
        pointAnim.start()

        if (points > level * LEVEL) {
            incrementLevel()
            tvLevelValue.text = Integer.toString(level)
            levelAnim.start()
        }
    }

    // called when user goes to next level
    fun incrementLevel() {
        level += 1
        TIMER_DELTA = level
    }

    // ABSTRACT METHODS
    protected abstract fun setColorsOnButtons()

    protected abstract fun calculatePoints(view: View)

    companion object {
        protected var TIMER_DELTA = -1
        protected val START_TIMER = 200
        protected val FPS = 100
        protected val LEVEL = 25
    }
}