package com.ue.colorful.feature.calculate

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.adapterdelegate.Item
import com.ue.colorful.R
import com.ue.colorful.model.AlphaHex
import com.ue.colorful.model.AlphaHexTitle
import kotlinx.android.synthetic.main.fragment_calc_alpha.view.*

/**
 * Created by hawk on 2017/12/13.
 */
class CalcAlphaFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_calc_alpha, container, false)

        rootView.rvAlphas.setHasFixedSize(true)
        rootView.rvAlphas.adapter = AlphaHexAdapter(activity, alphaHexs)

        return rootView
    }

    val alphaHexs: List<Item>
        get() {
            val alphaHexs = ArrayList<Item>()
            alphaHexs.add(AlphaHexTitle(getString(R.string.alpha_title), getString(R.string.hex_title)))
            var i = 0
            while (i < 100) {
                var hex = Integer.toHexString(Math.round(2.55F * i)).toUpperCase()
                if (hex.length == 1) hex = "0$hex"
                alphaHexs.add(AlphaHex("${100 - i}%", hex))
                i++
            }
            return alphaHexs
        }
}