package com.littlebear.learnhanzi.ui.learning;

//import android.content.Context;
import android.content.Context;
import android.webkit.JavascriptInterface;


public class WebAppInterface extends Object {

    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context context) {
        mContext = context;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void updateNumOfWrited(String msg) {
        LearningActivity activity  =(LearningActivity) mContext;
        activity.onFragmentUpdate(HanziPage.WRITING, Integer.valueOf(msg));
    }
}
