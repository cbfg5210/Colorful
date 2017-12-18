package com.ue.colorful.feature.calculate

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import kotlinx.android.synthetic.main.fragment_calc_argb.view.*

/**
 * Created by hawk on 2017/12/13.
 */
class CalcARGBFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_calc_argb, container, false)
        rootView.tvCalcArgb.movementMethod = ScrollingMovementMethod.getInstance()
        return rootView
    }
}