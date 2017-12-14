package com.ue.colorful.feature.test

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.ue.colorful.R
import kotlinx.android.synthetic.main.activity_color_vision_test.view.*
import kotlinx.android.synthetic.main.fragment_test_img.view.*

/**
 * Created by hawk on 2017/12/14.
 */
class ColorVisionTestFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_color_vision_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val testAnswer = resources.getStringArray(R.array.colorVisionAns)
        val size = testAnswer.size
        val states = SparseArray<Boolean>()

        val testImgResTa = resources.obtainTypedArray(R.array.colorVisionImgs)
        val testImgRes = Array(size, { i -> testImgResTa.getResourceId(i, 0) })
        testImgResTa.recycle()

        //绑定自定义适配器
        view.vpTestPager.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
            override fun getCount(): Int {
                return size
            }

            override fun getItem(position: Int): Fragment {
                val fragment = CVTPagerFragment.newInstance(position, testAnswer[position], testImgRes[position], states[position, false])
                fragment.setAnalyseListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        states.put(position, true)
                    }
                })
                return fragment
            }
        }

        view.tvTags.text = "${(view.vpTestPager.currentItem + 1)}/$size"

        view.vpTestPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                view.tvTags.text = "${(view.vpTestPager.currentItem + 1)}/$size"
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    class CVTPagerFragment : Fragment() {
        internal var pageNum: Int = 0
        internal var testAns: String = ""
        internal var testImg: Int = 0
        internal var isVisible: Boolean = false
        internal var onAnalyseListener: View.OnClickListener? = null

        fun setAnalyseListener(onAnalyseListener: View.OnClickListener) {
            this.onAnalyseListener = onAnalyseListener
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            pageNum = arguments.getInt(ARG_NUM)
            testAns = arguments.getString(ARG_TEST_ANS)
            testImg = arguments.getInt(ARG_TEST_IMG)
            isVisible = arguments.getBoolean(ARG_IS_VISIBLE)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_test_img, container, false)

            Picasso.with(activity)
                    .load(testImg)
                    .into(view.testImg)

            if (isVisible) {
                view.tvTips.visibility = View.GONE
                view.vgAnalysePanel.isSelected = true
                (view.vsAnswer.inflate() as TextView).text = testAns
            } else {
                view.tvTips.setOnClickListener { _ ->
                    view.tvTips.visibility = View.GONE
                    view.vgAnalysePanel.isSelected = true
                    (view.vsAnswer.inflate() as TextView).text = testAns
                    onAnalyseListener?.onClick(null)
                }
            }

            return view
        }

        companion object {
            private val ARG_NUM = "arg_num"
            private val ARG_TEST_ANS = "arg_test_ans"
            private val ARG_TEST_IMG = "arg_test_img"
            private val ARG_IS_VISIBLE = "arg_is_visible"

            fun newInstance(num: Int, testAns: String, testImg: Int, isVisible: Boolean): CVTPagerFragment {
                val fragment = CVTPagerFragment()
                val args = Bundle()
                args.putInt(ARG_NUM, num)
                args.putString(ARG_TEST_ANS, testAns)
                args.putInt(ARG_TEST_IMG, testImg)
                args.putBoolean(ARG_IS_VISIBLE, isVisible)
                fragment.arguments = args
                return fragment
            }
        }
    }

}