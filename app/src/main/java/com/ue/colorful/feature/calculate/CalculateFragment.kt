package com.ue.colorful.feature.calculate

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import com.ue.colorful.model.AlphaHex
import kotlinx.android.synthetic.main.fragment_calculate.view.*

/**
 * Created by hawk on 2017/12/13.
 */
class CalculateFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_calculate, container, false)

        rootView.rvAlphas.setHasFixedSize(true)
        rootView.rvAlphas.adapter = AlphaHexAdapter(activity, alphaHexs)

        return rootView
    }

    val alphaHexs: List<AlphaHex>
        get() {
            val alphaHexs = ArrayList<AlphaHex>()
            var i = 0
            while (i < 21) {
                var hex = Integer.toHexString(Math.round(12.75 * i).toInt()).toUpperCase()
                if (hex.length == 1) hex = "0$hex"
                alphaHexs.add(AlphaHex("${100 - i * 5}%", hex))
                i++
            }
            return alphaHexs
        }
}