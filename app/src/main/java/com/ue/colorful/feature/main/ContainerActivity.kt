package com.ue.colorful.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ue.colorful.R
import com.ue.colorful.constant.Constants

class ContainerActivity : AppCompatActivity() {

    companion object {
        private val ARG_FRAGMENT_FLAG = "arg_frag_flag"

        fun start(context: Context, fragFlag: Int) {
            val intent = Intent(context, ContainerActivity::class.java)
            intent.putExtra(ARG_FRAGMENT_FLAG, fragFlag)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val fragFlag = intent.getIntExtra(ARG_FRAGMENT_FLAG, 0)
        when (fragFlag) {
            Constants.FRAG_PICK_FROM_PALETTE -> {
            }
            Constants.FRAG_PICK_FROM_PHOTO -> {
            }
            Constants.FRAG_PICK_FROM_ARGB -> {
            }
            Constants.FRAG_PICK_FROM_SCREEN -> {
            }
            Constants.FRAG_GAME_COLOR_DIFF -> {
            }
            Constants.FRAG_GAME_PHUN -> {
            }
            Constants.FRAG_CALCULATE -> {
            }
        }
    }
}
