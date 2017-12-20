package com.ue.colorful.feature.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.dialog_game_result.view.*


/**
 * Created by hawk on 2017/12/19.
 */
class GameResultDialog : DialogFragment() {
    private var gameMode: Int = 0
    private var gameResult: Long = 0

    companion object {
        private val ARG_GAME_MODE = "arg_game_mode"
        private val ARG_GAME_RESULT = "arg_game_result"

        fun newInstance(gameMode: Int, gameResult: Long): GameResultDialog {
            val dialog = GameResultDialog()
            dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0)

            val arguments = Bundle()
            arguments.putInt(ARG_GAME_MODE, gameMode)
            arguments.putLong(ARG_GAME_RESULT, gameResult)
            dialog.arguments = arguments
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameMode = arguments.getInt(ARG_GAME_MODE)
        gameResult = arguments.getLong(ARG_GAME_RESULT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_game_result, null)

        rootView.simple.setOnValueChangeListener({ value ->
            rootView.number.text = value.toInt().toString()
        })

        when (gameMode) {
            Constants.GAME_DIFF_CLASSIC -> showDiffClassicResult(rootView)
            Constants.GAME_DIFF_TEN_TIMES -> showDiffTimeResult(rootView)
            Constants.GAME_LT_EASY -> showLtResult(rootView, SPKeys.GAME_LT_EASY_RECORD, SPKeys.GAME_LT_EASY_LAST)
            Constants.GAME_LT_HARD -> showLtResult(rootView, SPKeys.GAME_LT_HARD_RECORD, SPKeys.GAME_LT_HARD_LAST)
        }

        val dialog = AlertDialog.Builder(context)
                .setView(rootView)
                .create()

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog

    }

    private fun showLtResult(rootView: View, recordKey: String, lastKey: String) {
        var bestRecord = SPUtils.getLong(recordKey, 0L)
        if (gameResult > bestRecord) {
            bestRecord = gameResult
            SPUtils.putLong(recordKey, bestRecord)
        }
        rootView.tvBestRecord.text = getString(R.string.game_lt_best, bestRecord)

        val lastResult = SPUtils.getLong(lastKey, gameResult)
        val diff = gameResult - lastResult
        SPUtils.putLong(lastKey, gameResult)

        if (diff < 0) rootView.tvCompareResult.text = getString(R.string.game_lt_compare, diff.toString())
        else if (diff == 0L) rootView.tvCompareResult.text = getString(R.string.game_lt_compare, getString(R.string.equal))
        else rootView.tvCompareResult.text = getString(R.string.game_lt_compare, "+$diff")

        rootView.simple.setValue(if (gameResult > 76) 100F else gameResult * 1.3F)
    }

    private fun showDiffTimeResult(rootView: View) {
        var bestRecord = SPUtils.getLong(SPKeys.GAME_DIFF_TIME_RECORD, 0L)
        if (gameResult < bestRecord) {
            bestRecord = gameResult
            SPUtils.putLong(SPKeys.GAME_DIFF_TIME_RECORD, bestRecord)
        }
        rootView.tvBestRecord.text = getString(R.string.game_diff_time_best, bestRecord)

        val lastResult = SPUtils.getLong(SPKeys.GAME_DIFF_TIME_LAST, gameResult)
        val diff = gameResult - lastResult
        SPUtils.putLong(SPKeys.GAME_DIFF_TIME_LAST, gameResult)

        if (diff < 0) rootView.tvCompareResult.text = getString(R.string.game_diff_time_compare, diff.toString())
        else if (diff == 0L) rootView.tvCompareResult.text = getString(R.string.game_diff_time_compare, getString(R.string.equal))
        else rootView.tvCompareResult.text = getString(R.string.game_diff_time_compare, "+$diff")

        val score = (100000F - gameResult) / 1000
        rootView.simple.setValue(if (score < 0) 0F else score)
    }

    private fun showDiffClassicResult(rootView: View) {
        var bestRecord = SPUtils.getLong(SPKeys.GAME_DIFF_CLASSIC_RECORD, 0L)
        if (gameResult > bestRecord) {
            bestRecord = gameResult
            SPUtils.putLong(SPKeys.GAME_DIFF_CLASSIC_RECORD, bestRecord)
        }
        rootView.tvBestRecord.text = getString(R.string.game_diff_classic_best, bestRecord)

        val lastResult = SPUtils.getLong(SPKeys.GAME_DIFF_CLASSIC_LAST, 0L)
        val diff = gameResult - lastResult
        SPUtils.putLong(SPKeys.GAME_DIFF_CLASSIC_LAST, gameResult)

        if (diff < 0) rootView.tvCompareResult.text = getString(R.string.game_diff_classic_compare, diff.toString())
        else if (diff == 0L) rootView.tvCompareResult.text = getString(R.string.game_diff_classic_compare, getString(R.string.equal))
        else rootView.tvCompareResult.text = getString(R.string.game_diff_classic_compare, "+$diff")

        rootView.simple.setValue(if (gameResult > 16) 100F else gameResult * 6F)
    }
}