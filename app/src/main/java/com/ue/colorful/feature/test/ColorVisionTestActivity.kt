package com.ue.colorful.feature.test

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import kotlinx.android.synthetic.main.activity_color_vision_test.*
import kotlinx.android.synthetic.main.fragment_test_img.view.*

class ColorVisionTestActivity : AppCompatActivity() {
    private lateinit var testAnswer: Array<String>
    private lateinit var testImg: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_vision_test)

        testAnswer = resources.getStringArray(R.array.colorVisionAns)
        testImg = intArrayOf(
                R.raw.p01, R.raw.p02, R.raw.p03,
                R.raw.p04, R.raw.p05, R.raw.p06,
                R.raw.p07, R.raw.p08, R.raw.p09,
                R.raw.p10, R.raw.p11, R.raw.p12,
                R.raw.p13, R.raw.p14, R.raw.p15,
                R.raw.p16, R.raw.p17, R.raw.p18,
                R.raw.p19, R.raw.p20, R.raw.p21,
                R.raw.p22, R.raw.p23, R.raw.p24)

        val size = testAnswer.size

        //绑定自定义适配器
        vpTestPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return size
            }

            override fun getItem(position: Int): Fragment {
                return ColorVisionTestFragment.newInstance(position, testAnswer[position], testImg[position])
            }
        }

        tvTags.text = "${(vpTestPager.currentItem + 1)}/$size"

        vpTestPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                tvTags.text = "${(vpTestPager.currentItem + 1)}/$size"
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    class ColorVisionTestFragment : Fragment() {
        internal var pageNum: Int = 0
        internal var testAns: String = ""
        internal var testImg: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (arguments != null) {
                pageNum = arguments.getInt(ARG_NUM)
                testAns = arguments.getString(ARG_TEST_ANS)
                testImg = arguments.getInt(ARG_TEST_IMG)
            }
        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater!!.inflate(R.layout.fragment_test_img, container, false)
            view.testImg.setImageResource(testImg)
            view.ravAnswer.text = testAns
            return view
        }

        companion object {
            private val ARG_NUM = "arg_num"
            private val ARG_TEST_ANS = "arg_test_ans"
            private val ARG_TEST_IMG = "arg_test_img"

            fun newInstance(num: Int, testAns: String, testImg: Int): ColorVisionTestFragment {
                val fragment = ColorVisionTestFragment()
                val args = Bundle()
                args.putInt(ARG_NUM, num)
                args.putString(ARG_TEST_ANS, testAns)
                args.putInt(ARG_TEST_IMG, testImg)
                fragment.arguments = args
                return fragment
            }
        }
    }
}