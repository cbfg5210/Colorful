package com.ue.colorful.feature.game.diffcolor

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.ue.colorful.R
import kotlinx.android.synthetic.main.fragment_time_trial_mode.*
import kotlinx.android.synthetic.main.fragment_time_trial_mode.view.*
import java.util.*

class TimeTrialModeFragment : Fragment(), View.OnClickListener {
    private lateinit var rootView: View

    private var buttonsInRow: Int = 0
    private var randomButton: Int = 0
    private var width: Int = 0
    private var level: Int = 0

    private val arrays = Arrays()
    private val r = Random()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_time_trial_mode, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view

        rootView.tvLevel.text = "10"
        rootView.btnRedraw.setOnClickListener {
            linearLayoutTags.removeAllViews()
            drawMap(HARD, 7)
        }

        rootView.btnHalf.setOnClickListener { halfTiles(49) }
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
                drawable.setColor(Color.parseColor("#" + color0))
                btn.setOnClickListener(this)
                row.addView(btn)
            }

            rootView.linearLayoutTags.addView(row)
        }
        val b = rootView.linearLayoutTags.findViewById<View>(randomButton) as Button
        val drawable2 = b.background as GradientDrawable
        drawable2.setColor(Color.parseColor("#" + color1))
    }

    override fun onClick(view: View) {
        (view as Button).text = "*"
        view.setEnabled(false)

        val myId = view.getId()
        if (myId == randomButton) {
            rootView.linearLayoutTags.removeAllViews()

            level--
            if (level > 7) {
                drawMap(MEDIUM, 7)
            } else if (level > 0 && level <= 7) {
                drawMap(HARD, 7)
            } else {
                gameOver()
            }

            rootView.tvLevel.text = level.toString()
        } else {
            val b3 = rootView.linearLayoutTags.findViewById<View>(myId) as Button
            val drawable3 = b3.background as GradientDrawable
            drawable3.setColor(Color.BLACK)
        }
    }

    fun halfTiles(num: Int) {
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
                    val b2 = rootView.linearLayoutTags.findViewById<View>(list[i]) as Button
                    b2.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun gameOver() {
        rootView.chronometerText.stop()

        val dialog = TimeTrialResultDialog.newInstance(rootView.chronometerText.text.toString())
        dialog.setPlayAgainListener(View.OnClickListener { startGame() })
        dialog.show(childFragmentManager, "")
    }

    fun startGame() {
        rootView.chronometerText.start()

        width = resources.displayMetrics.widthPixels * 9 / 10
        rootView.linearLayoutTags.removeAllViews()

        level = 10
        rootView.tvLevel.text = level.toString()
        drawMap(HARD, 7)
    }

    override fun onPause() {
        super.onPause()
        rootView.chronometerText.stop()
    }

    override fun onStop() {
        super.onStop()
        rootView.chronometerText.stop()
    }

    override fun onResume() {
        super.onResume()
        if (rootView.chronometerText.timeElapsed > 0) {
            rootView.chronometerText.resume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        rootView.chronometerText.stop()
    }

    companion object {
        private val MEDIUM = 2
        private val HARD = 3
    }
}
