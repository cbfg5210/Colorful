package com.ue.colorful.feature.material

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.ue.colorful.R
import com.ue.colorful.model.PaletteColor
import com.ue.colorful.model.PaletteColorSection
import kotlinx.android.synthetic.main.activity_color_palette.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class ColorPaletteActivity : AppCompatActivity() {

    private var mFragment: PaletteFragment? = null
    private var mDrawerTitle: CharSequence? = null
    private var mTitle: CharSequence? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mColorList: MutableList<PaletteColorSection>? = null
    private var mPosition = 0

    private val drawerClickListener = object : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View,
                                 position: Int, id: Long) {
            selectItem(position)
        }
    }

    private val paletteColorSectionsList: MutableList<PaletteColorSection>
        get() {
            val res = resources
            val colorSectionsNames = res.getStringArray(R.array.color_sections_names)
            val colorSectionsValues = res.getIntArray(R.array.color_sections_colors)
            val darkColorSectionsValues = res.getIntArray(R.array.dark_color_sections_colors)

            val baseColorNamesArray = res.obtainTypedArray(R.array.all_color_names)
            val colorValuesArray = res.obtainTypedArray(R.array.all_color_list)

            var mColorList = ArrayList<PaletteColorSection>()
            var i = 0
            val len = colorSectionsNames.size
            while (i < len) {
                val paletteColorList = ArrayList<PaletteColor>()
                val baseColorNames = res.getStringArray(baseColorNamesArray.getResourceId(i, -1))
                val colorValues = res.getIntArray(colorValuesArray.getResourceId(i, -1))

                var j = 0
                val jLen = baseColorNames.size
                while (j < jLen) {
                    paletteColorList.add(PaletteColor(colorSectionsNames[i], colorValues[j], baseColorNames[j]))
                    j++
                }
                mColorList!!.add(PaletteColorSection(colorSectionsNames[i], colorSectionsValues[i], darkColorSectionsValues[i], paletteColorList))
                i++
            }
            return mColorList
        }

    override fun setTitle(title: CharSequence) {
        mTitle = title
        supportActionBar!!.setTitle(mTitle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_palette)

        setSupportActionBar(toolbar)

        mColorList = paletteColorSectionsList

        setupNavigationDrawer(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        if (savedInstanceState != null) {
            mFragment = fragmentManager
                    .getFragment(savedInstanceState, FRAGMENT_KEY) as PaletteFragment
            mPosition = savedInstanceState.getInt(POSITION_KEY)
            mDrawerTitle = savedInstanceState.getCharSequence(DRAWER_TITLE_KEY)
            mTitle = savedInstanceState.getCharSequence(TITLE_KEY)
        }
        if (mDrawerTitle == null) {
            mDrawerTitle = title
        }
        if (mTitle == null) {
            mTitle = title
        }

        selectItem(mPosition, mFragment, false)
    }

    private fun setupNavigationDrawer(toolbar: Toolbar) {
        navDrawerList!!.adapter = DrawerAdapter(this, mColorList)
        navDrawerList!!.setOnItemClickListener(drawerClickListener)
        mDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout!!.addDrawerListener(mDrawerToggle!!)
    }

    /* Swaps fragments in the main content view */
    private fun selectItem(position: Int, fragment: PaletteFragment? = null,
                           fromClick: Boolean = true) {
        val paletteColorSection = mColorList!![position]
        val sectionName = paletteColorSection.colorSectionName
        val sectionValue = paletteColorSection.colorSectionValue
        val darkColorSectionValue = paletteColorSection.darkColorSectionsValue
        if (mPosition == position && fromClick) {
            mFragment?.scrollToTop()
        } else if (fromClick) {
            mPosition = position
            mFragment!!.replaceColorCardList(paletteColorSection)
        } else {
            mPosition = position
            if (fragment == null) {
                val bundle = Bundle()
                bundle.putParcelable(PaletteFragment.ARG_COLOR_SECTION, paletteColorSection)
                mFragment = PaletteFragment()
                mFragment!!.arguments = bundle
            } else {
                mFragment = fragment
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.container, mFragment,
                    FRAGMENT_TAG)
                    .commit()
        }
        // Highlight the selected item, update the title, and close the drawer
        navDrawerList!!.setItemChecked(mPosition, true)
        setTitle(sectionName)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(sectionValue))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = darkColorSectionValue
        }

        drawerLayout!!.closeDrawer(navDrawerList)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle!!.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (mDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        fragmentManager.putFragment(outState, FRAGMENT_KEY, mFragment)
        outState!!.putInt(POSITION_KEY, mPosition)
        outState.putCharSequence(DRAWER_TITLE_KEY, mDrawerTitle)
        outState.putCharSequence(TITLE_KEY, mTitle)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private val FRAGMENT_TAG = "FRAGMENT_TAG"
        private val FRAGMENT_KEY = "FRAGMENT_KEY"
        private val POSITION_KEY = "POSITION_KEY"
        private val DRAWER_TITLE_KEY = "DRAWER_TITLE_KEY"
        private val TITLE_KEY = "TITLE_KEY"
    }
}
