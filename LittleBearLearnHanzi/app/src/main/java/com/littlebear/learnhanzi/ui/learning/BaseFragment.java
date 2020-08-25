package com.littlebear.learnhanzi.ui.learning;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Locale;

class BaseFragment extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    protected Toast mToast;
    protected TextToSpeech mTts;

    protected Activity mActivity;

    protected OnFragmentUpdateListener mFragmentUpdateListener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);

        mTts = new TextToSpeech(mActivity.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mTts.setLanguage(Locale.CHINESE);
                    mTts.setSpeechRate(0.3f);
                }
            }
        });

        mFragmentUpdateListener = (OnFragmentUpdateListener)mActivity;

    }

    protected void showTip(String str) {
        if (!TextUtils.isEmpty(str)) {
            mToast.setText(str);
            mToast.show();
        }
    }

}
