package com.ue.colorful.feature.game

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.*

/**
 * Created by hawk on 2017/12/19.
 */
abstract class BaseGameFragment(private val layoutRes: Int, private val menuRes: Int) : Fragment() {
    protected lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(layoutRes, container, false)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menuRes > 0) inflater.inflate(menuRes, menu)
    }

    fun gameOver(score: Int) {
        AlertDialog.Builder(activity)
                .setTitle("Game Over")
                .setMessage("颜值:$score")
                .create()
    }
}