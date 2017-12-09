package com.ue.colorful.feature.material;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ue.colorful.R;
import com.ue.colorful.model.PaletteColor;
import com.ue.colorful.model.PaletteColorSection;

public class PaletteFragment extends Fragment implements ColorCardRecyclerAdapter.ColorCardRecyclerAdapterCallback {

    public static final String ARG_COLOR_SECTION = "COLOR_SECTION";

    private static final String SECTION_KEY = "SECTION_KEY";

    private RecyclerView mListView;
    private ColorCardRecyclerAdapter mRecyclerAdapter;
    private PaletteColorSection mPaletteColorSection = null;

    protected ClipboardManager mClipboardManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_color_palette, container, false);
        mListView = (RecyclerView) v;
        return mListView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.palette_color_menu, menu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mPaletteColorSection = savedInstanceState.getParcelable(SECTION_KEY);
        }
        if (mPaletteColorSection == null) {
            mPaletteColorSection = getArguments().getParcelable(ARG_COLOR_SECTION);
        }

        mRecyclerAdapter = new ColorCardRecyclerAdapter(getActivity(), mPaletteColorSection.getPaletteColorList(), this);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SECTION_KEY, mPaletteColorSection);
        super.onSaveInstanceState(outState);
    }

    public void replaceColorCardList(PaletteColorSection paletteColorSection) {
        mPaletteColorSection = paletteColorSection;
        mRecyclerAdapter = new ColorCardRecyclerAdapter(getActivity(),
                mPaletteColorSection.getPaletteColorList(), this);
        mListView.setAdapter(mRecyclerAdapter);
    }

    public void scrollToTop() {
        mListView.smoothScrollToPosition(0);
    }

    @Override
    public void onCopyColorClicked(PaletteColor paletteColor) {
        if (mClipboardManager == null) {
            mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        }
        final ClipData clip
                = ClipData.newPlainText(getString(R.string.color_clipboard, paletteColor.getColorSectionName(),
                paletteColor.getBaseName()), paletteColor.getHexString());
        mClipboardManager.setPrimaryClip(clip);

        Toast.makeText(getActivity(), getString(R.string.color_copied, paletteColor.getHexString()),
                Toast.LENGTH_SHORT).show();
    }
}
