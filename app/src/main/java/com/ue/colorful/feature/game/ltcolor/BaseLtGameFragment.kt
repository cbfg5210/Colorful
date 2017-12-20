package com.ue.colorful.feature.game.ltcolor

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import com.ue.colorful.feature.main.BaseFragment
import kotlinx.android.synthetic.main.progress_area_layout.view.*

abstract class BaseLtGameFragment(private val layoutRes: Int, private val menuRes: Int) : BaseFragment(layoutRes, menuRes), View.OnClickListener {

    protected lateinit var pointAnim: AnimatorSet
    protected lateinit var levelAnim: AnimatorSet

    protected var level: Int = 0
    protected var points: Int = 0
    protected var gameStart = false
    protected lateinit var runnable: Runnable
    protected var timer: Int = 0
    protected var gameMode: Int = Constants.GAME_LT_EASY

    protected lateinit var handler: Handler
    private lateinit var thread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
    }

    protected fun setupProgressView() {
        // setting up animations
        pointAnim = AnimatorInflater.loadAnimator(activity, R.animator.points_animations) as AnimatorSet
        pointAnim.setTarget(rootView.tvPointsValue)
        levelAnim = AnimatorInflater.loadAnimator(activity, R.animator.level_animations) as AnimatorSet
        levelAnim.setTarget(rootView.tvLevelValue)
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
                            Log.e("THREAD ERROR", "e=$e")
                        }

                        timer--
                    }
                    handler.post { rootView.pbTimerProgress.progress = timer }
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
        rootView.tvPointsValue.text = points.toString()
        rootView.tvLevelValue.text = level.toString()
        rootView.pbTimerProgress.progress = 0
    }

    protected fun startGame() {
        gameStart = true

        setColorsOnButtons()

        // start timer
        timer = START_TIMER
        thread = Thread(runnable)
        thread.start()
    }

    protected fun endGame() {
        gameStart = false
        if (!thread.isInterrupted) {
            thread.interrupt()
        }
        containerCallback?.gameOver(gameMode, points.toLong())
    }

    // called on correct guess
    fun updatePoints() {
        points = points++
        rootView.tvPointsValue.text = points.toString()
        pointAnim.start()

        if (points > level * LEVEL) {
            level++
            rootView.tvLevelValue.text = level.toString()
            levelAnim.start()
        }
    }

    // ABSTRACT METHODS
    protected abstract fun setColorsOnButtons()

    protected abstract fun calculatePoints(view: View)

    companion object {
        protected val START_TIMER = 200
        protected val FPS = 200
        protected val LEVEL = 15
    }
}