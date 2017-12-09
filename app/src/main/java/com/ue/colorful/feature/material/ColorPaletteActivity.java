package com.ue.colorful.feature.material;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ue.colorful.R;
import com.ue.colorful.model.PaletteColor;
import com.ue.colorful.model.PaletteColorSection;

import java.util.ArrayList;
import java.util.List;

public class ColorPaletteActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private static final String FRAGMENT_KEY = "FRAGMENT_KEY";
    private static final String POSITION_KEY = "POSITION_KEY";
    private static final String DRAWER_TITLE_KEY = "DRAWER_TITLE_KEY";
    private static final String TITLE_KEY = "TITLE_KEY";

    private PaletteFragment mFragment = null;
    private CharSequence mDrawerTitle = null;
    private CharSequence mTitle = null;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<PaletteColorSection> mColorList;
    private int mPosition = 0;

    private final ListView.OnItemClickListener drawerClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(final AdapterView parent, final View view,
                                final int position, final long id) {
            selectItem(position);
        }
    };

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_palette);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mColorList = getPaletteColorSectionsList();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.navigation_drawer);
        setupNavigationDrawer(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState != null) {
            mFragment = (PaletteFragment) getFragmentManager()
                    .getFragment(savedInstanceState, FRAGMENT_KEY);
            mPosition = savedInstanceState.getInt(POSITION_KEY);
            mDrawerTitle = savedInstanceState.getCharSequence(DRAWER_TITLE_KEY);
            mTitle = savedInstanceState.getCharSequence(TITLE_KEY);
        }
        if (mDrawerTitle == null) {
            mDrawerTitle = getTitle();
        }
        if (mTitle == null) {
            mTitle = getTitle();
        }

        selectItem(mPosition, mFragment, false);
    }

    private void setupNavigationDrawer(Toolbar toolbar) {
        mDrawerList.setAdapter(new DrawerAdapter(this, mColorList));
        mDrawerList.setOnItemClickListener(drawerClickListener);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    /* Swaps fragments in the main content view */
    private void selectItem(final int position, final PaletteFragment fragment,
                            boolean fromClick) {
        final PaletteColorSection paletteColorSection = mColorList.get(position);
        final String sectionName = paletteColorSection.getColorSectionName();
        final int sectionValue = paletteColorSection.getColorSectionValue();
        final int darkColorSectionValue = paletteColorSection.getDarkColorSectionsValue();
        if (mPosition == position && fromClick) {
            mFragment.scrollToTop();
        } else if (fromClick) {
            mPosition = position;
            mFragment.replaceColorCardList(paletteColorSection);
        } else {
            mPosition = position;
            if (fragment == null) {
                final Bundle bundle = new Bundle();
                bundle.putParcelable(PaletteFragment.ARG_COLOR_SECTION, paletteColorSection);
                mFragment = new PaletteFragment();
                mFragment.setArguments(bundle);
            } else {
                mFragment = fragment;
            }
            getFragmentManager().beginTransaction().replace(R.id.container, mFragment,
                    FRAGMENT_TAG).commit();
        }
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(mPosition, true);
        setTitle(sectionName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(sectionValue));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(darkColorSectionValue);
        }

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void selectItem(final int position) {
        selectItem(position, null, true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getFragmentManager().putFragment(outState, FRAGMENT_KEY, mFragment);
        outState.putInt(POSITION_KEY, mPosition);
        outState.putCharSequence(DRAWER_TITLE_KEY, mDrawerTitle);
        outState.putCharSequence(TITLE_KEY, mTitle);
        super.onSaveInstanceState(outState);
    }

    private List<PaletteColorSection> getPaletteColorSectionsList() {
        Resources res = getResources();

        final String[] colorSectionsNames = res.getStringArray(R.array.color_sections_names);
        final int[] colorSectionsValues = res.getIntArray(R.array.color_sections_colors);
        final int[] darkColorSectionsValues = res.getIntArray(R.array.dark_color_sections_colors);

        TypedArray baseColorNamesArray = res.obtainTypedArray(R.array.all_color_names);
        TypedArray colorValuesArray = res.obtainTypedArray(R.array.all_color_list);

        mColorList = new ArrayList<>();
        for (int i = 0, len = colorSectionsNames.length; i < len; i++) {
            List<PaletteColor> paletteColorList = new ArrayList<>();
            String[] baseColorNames = res.getStringArray(baseColorNamesArray.getResourceId(i, -1));
            int[] colorValues = res.getIntArray(colorValuesArray.getResourceId(i, -1));

            for (int j = 0, jLen = baseColorNames.length; j < jLen; j++) {
                paletteColorList.add(new PaletteColor(colorSectionsNames[i], colorValues[j], baseColorNames[j]));
            }
            mColorList.add(new PaletteColorSection(colorSectionsNames[i], colorSectionsValues[i], darkColorSectionsValues[i], paletteColorList));
        }
        return mColorList;
    }
}
