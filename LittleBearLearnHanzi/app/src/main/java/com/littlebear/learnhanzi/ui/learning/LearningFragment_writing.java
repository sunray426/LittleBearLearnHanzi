package com.littlebear.learnhanzi.ui.learning;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.littlebear.learnhanzi.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearningFragment_writing#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearningFragment_writing extends BaseFragment {
    private static final String ARG_CHARACTER = "character";
    private static final String ARG_PARAM2 = "param2";

    private int mTotalTimes = 5; //总共要书写的汉字数量
    private int mCompletedTimes = 0; //已经完成书写的汉字数量

    // TODO: Rename and change types of parameters
    private String mCharacter;
    private String mParam2;


    public LearningFragment_writing() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param character Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearnWordFragment_3.
     */
    // TODO: Rename and change types and number of parameters
    public static LearningFragment_writing newInstance(String character, String param2) {
        LearningFragment_writing fragment = new LearningFragment_writing();
        Bundle args = new Bundle();
        args.putString(ARG_CHARACTER, character);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LearningFragment_writing draw() {
        //WebView fanggeWeb =getView().findViewById(R.id.fanggeWeb);
        //fanggeWeb.loadUrl("javascript:main_draw('一', 5)");
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCharacter = getArguments().getString(ARG_CHARACTER);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learning_writing, container, false);

        final WebView fanggeWeb = (WebView) view.findViewById(R.id.fanggeWeb);
        WebSettings webSettings = fanggeWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        fanggeWeb.setBackgroundColor(0xe2e2e2);//加上这行语句，汉字的webview就不会出现白色背景。
        fanggeWeb.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
        fanggeWeb.loadUrl("file:///android_asset/web/SingleHanzi-Writer.html?hanzi=" + mCharacter + "&total=" + mTotalTimes + "&completed=" + mCompletedTimes);

//
//        Button nextBtn = view.findViewById(R.id.next2);
//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mCallback.onFragmentFinished(3);
//
//            }
//        });
        return view;
    }

    //该函数由主Activity调用，主要是为了用于当屏幕旋转时重绘界面
    public void updateCompleted(int times) {
        mCompletedTimes = times;
    }
}
