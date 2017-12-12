package com.ue.colorful.feature.calculate

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ue.colorful.R
import com.ue.colorful.model.AlphaHex
import kotlinx.android.synthetic.main.activity_calculate.*

class CalculateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        rvAlphas.setHasFixedSize(true)
        rvAlphas.adapter = AlphaHexAdapter(this, alphaHexs)
    }

    val alphaHexs: List<AlphaHex>
        get() {
            val alphaHexs = ArrayList<AlphaHex>()
            var i = 0
            while (i < 21) {
                var hex = Integer.toHexString(Math.round(12.75 * i).toInt()).toUpperCase()
                if (hex.length == 1) hex = "0" + hex
                alphaHexs.add(AlphaHex(String.format("%d%%", 100 - i * 5), hex))
                i++
            }
            return alphaHexs
        }
}
