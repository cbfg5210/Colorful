package com.ue.fingercoloring.controller.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.ue.fingercoloring.R
import kotlinx.android.synthetic.main.activity_finger_coloring.*

/**
 * Created by Swifty.Wang on 2015/7/31.
 */
class FingerColoringActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finger_coloring)

        vgTabs.setupWithViewPager(initViewPager())
    }

    private fun initViewPager(): ViewPager {
        val tabs = arrayOf(getString(R.string.themelist), getString(R.string.userlogin))

        viewpager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return if (position == 0) ThemesFragment.newInstance() else WorksFragment.newInstance()
            }

            override fun getPageTitle(position: Int): CharSequence {
                return tabs[position]
            }

            override fun getCount(): Int {
                return tabs.size
            }
        }
        return viewpager
    }
}
