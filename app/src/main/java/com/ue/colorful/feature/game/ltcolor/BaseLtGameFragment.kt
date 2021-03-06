package com.ue.colorful.feature.game.ltcolor

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.feature.main.BaseFragment
import kotlinx.android.synthetic.main.progress_area_layout.view.*
import java.util.*

abstract class BaseLtGameFragment(private val layoutRes: Int, private val menuRes: Int) : BaseFragment(layoutRes, menuRes), View.OnClickListener {
    private lateinit var pointAnim: AnimatorSet
    private lateinit var levelAnim: AnimatorSet

    protected var gameMode: Int = 0
    protected var gameStart = false
    private var level: Int = 0
    private var points: Int = 0
    private var restTime: Int = 0
    private var paused = false

    protected lateinit var handler: Handler
    private var timer: Timer? = null

    protected lateinit var ivStartGame: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
    }

    protected fun setupProgressView() {
        // setting up animations
        pointAnim = AnimatorInflater.loadAnimator(activity as Context, R.animator.points_animations) as AnimatorSet
        pointAnim.setTarget(rootView.tvPointsValue)
        levelAnim = AnimatorInflater.loadAnimator(activity, R.animator.level_animations) as AnimatorSet
        levelAnim.setTarget(rootView.tvLevelValue)
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
        ivStartGame.visibility = View.GONE
        gameStart = true
        restTime = START_TIMER

        setColorsOnButtons()

        timer?.cancel()
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                if (paused) return

                handler.post { rootView.pbTimerProgress.progress = restTime }
                restTime--
                if (restTime < 0) {
                    handler.post { endGame() }
                }
            }
        }, 0, FPS)
    }

    protected fun endGame() {
        timer?.cancel()
        gameStart = false
        ivStartGame.visibility = View.VISIBLE

        containerCallback?.gameOver(gameMode, points.toLong())
    }

    // called on correct guess
    fun updatePoints() {
        points += 1
        rootView.tvPointsValue.text = points.toString()
        pointAnim.start()

        if (points > level * LEVEL) {
            level++
            rootView.tvLevelValue.text = level.toString()
            levelAnim.start()
        }
    }

    override fun onPause() {
        super.onPause()
        paused = true
    }

    override fun onResume() {
        super.onResume()
        paused = false
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    // ABSTRACT METHODS
    protected abstract fun setColorsOnButtons()

    protected abstract fun calculatePoints(view: View)

    companion object {
        protected val START_TIMER = 300
        protected val FPS = 150L
        protected val LEVEL = 15
    }
}