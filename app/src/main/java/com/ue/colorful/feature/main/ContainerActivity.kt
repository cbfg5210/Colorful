package com.ue.colorful.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import com.ue.colorful.feature.calculate.CalculateFragment
import com.ue.colorful.feature.material.MDPaletteFragment

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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragFlag = intent.getIntExtra(ARG_FRAGMENT_FLAG, 0)
        when (fragFlag) {
            Constants.FRAG_PICK_FROM_PALETTE -> {
                supportActionBar?.setTitle("MDPalette")
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, MDPaletteFragment())
                        .commit()
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
                supportActionBar?.setTitle("Calculate")
                supportFragmentManager.beginTransaction()
                        .add(R.id.vgFragmentContainer, CalculateFragment())
                        .commit()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
