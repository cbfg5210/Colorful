package com.ue.colorful.feature.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ue.colorful.R;

public class ColorVisionTestFragment extends Fragment {
    private static final String ARG_NUM = "arg_num";

    private String[] testAnswer;
    private static int[] testImg = new int[]{
            R.raw.p01, R.raw.p02, R.raw.p03, R.raw.p04, R.raw.p05,
            R.raw.p06, R.raw.p07, R.raw.p08, R.raw.p09, R.raw.p10,
            R.raw.p11, R.raw.p12, R.raw.p13, R.raw.p14, R.raw.p15, R.raw.p16,
            R.raw.p17, R.raw.p18, R.raw.p19, R.raw.p20, R.raw.p21, R.raw.p22,
            R.raw.p23, R.raw.p24,
    };

    int pageNum;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment testImg.
     */
    public static ColorVisionTestFragment newInstance(int num) {
        ColorVisionTestFragment fragment = new ColorVisionTestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM, num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageNum = getArguments().getInt(ARG_NUM);
        }
        testAnswer = getResources().getStringArray(R.array.colorVisionAns);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_img, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.testImg);
        img.setImageResource(testImg[pageNum]);
        TextView text = view.findViewById(R.id.answer);
        text.setText(testAnswer[pageNum]);
        return view;
    }
}
