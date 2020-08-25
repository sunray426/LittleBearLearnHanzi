package com.littlebear.learnhanzi.ui.learning;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.iflytek.ise.result.Result;
import com.iflytek.ise.result.xml.XmlResultParser;
import com.iflytek.speech.util.FucUtil;
import com.littlebear.learnhanzi.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.makeText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearningFragment_pronoumciation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearningFragment_pronoumciation extends BaseFragment {

    private static final String ARG_HANZI_JSON = "hanziJson";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mHanziJson;
    private String mParam2;

    private String mCharacter;

    private final static String PREFER_NAME = "ise_settings";

    private SpeechEvaluator mIse;
    private String mLastResult;

    private WebView characterWebView;

    private int tryTimes = 0; //读字的错误次数


    private ImageButton mKnowWordBtn;

    public LearningFragment_pronoumciation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearnWordsFragment_.
     */
    // TODO: Rename and change types and number of parameters
    public static LearningFragment_pronoumciation newInstance(String param1, String param2) {
        LearningFragment_pronoumciation fragment = new LearningFragment_pronoumciation();
        Bundle args = new Bundle();
        args.putString(ARG_HANZI_JSON, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mHanziJson = getArguments().getString(ARG_HANZI_JSON);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try {
            JSONObject json = new JSONObject(mHanziJson);
            mCharacter = json.getString("character");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIse = SpeechEvaluator.createEvaluator(mActivity, null);



    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learning_pronunciation, container, false); //创建显示汉字的表格

        characterWebView = view.findViewById(R.id.webview);
        WebSettings webSettings = characterWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        characterWebView.setBackgroundColor(0xe2e2e2);//加上这行语句，汉字的webview就不会出现白色背景。
        //characterWebView.loadUrl("file:///android_asset/web/SingleHanzi1.html?zi=" + "汉" + "&ci=汉字");
        characterWebView.loadUrl("file:///android_asset/web/SingleHanzi.html?hanzi=" + mCharacter);


        mKnowWordBtn =  view.findViewById(R.id.knownWord);
        mKnowWordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    start_evaluate(mCharacter);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //mKnowWordBtn.setEnabled(false);
                    stop_evaluate();
                }

                return false;
            }
        });

        Button unknownBtn = view.findViewById(R.id.unknownWord);
        unknownBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTts.speak(mCharacter, TextToSpeech.QUEUE_FLUSH, null,null);
                mFragmentUpdateListener.onFragmentUpdate(HanziPage.PRONUNCIATION, -1);
            }
        });


        return view;

    }


    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            Log.d("", "evaluator result :" + isLast);

            if (isLast) {
                mLastResult = result.getResultString();
                System.out.println(mLastResult);

                if (!TextUtils.isEmpty(mLastResult)) {
                    XmlResultParser resultParser = new XmlResultParser();
                    Result resultScore = resultParser.parse(mLastResult);

                    if (null == resultScore) {
                        showTip("没听清，请重新读一遍");
                    } else {
                        if (resultScore.total_score >= 60.0) {
                            mFragmentUpdateListener.onFragmentUpdate(HanziPage.PRONUNCIATION,-1);
                            showTip("恭喜你，认识这个字!" + resultScore.total_score );

                        } else {
                            tryTimes++;
                            if (tryTimes >= 3) {
                                mFragmentUpdateListener.onFragmentUpdate(HanziPage.PRONUNCIATION,-1);
                            }

                            showTip("没听清，请重新读一遍!" + resultScore.total_score );
                        }
                    }
                }
            }
        }


        @Override
        public void onError(SpeechError error) {
            if(error != null) {
                showTip(" error:"+ error.getErrorCode() + "," + error.getErrorDescription());
                //mResultEditText.setText("");
                //mResultEditText.setHint("请点击“开始评测”按钮");
            } else {
                Log.d("", "evaluator over");
            }
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Log.d("TAG", "evaluator begin");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //Log.d("TAG", "evaluator stoped");
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前音量：" + volume);
            //Log.d("TAG", "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };

    public void start_evaluate (String evaText ) {
        if (mIse == null) {
            return;
        }

        mLastResult = null;

        setParams();
        int ret = mIse.startEvaluating(evaText, null, mEvaluatorListener);
        //以下方法为通过直接写入音频的方式进行评测业务
        if (ret != ErrorCode.SUCCESS) {
            showTip("识别失败,错误码：" + ret);
        } else {
            showTip(getString(R.string.text_begin_ise));
            byte[] audioData = FucUtil.readAudioFile(getActivity(),"isetest.wav");
            System.out.println("audioData = " + audioData.length);
            if(audioData != null) {
                //防止写入音频过早导致失败
                try{
                    new Thread().sleep(100);
                }catch (InterruptedException e) {
                    Log.d("TAG","InterruptedException :"+e);
                }
                mIse.writeAudio(audioData,0,audioData.length);
                System.out.println("audioData = " + audioData.length);
                mIse.stopEvaluating();
            }
        }
    }

    public void stop_evaluate () {
        if (mIse == null) {
            return;
        }
        if (mIse.isEvaluating()) {
            mIse.stopEvaluating();

        }
    }


    private void setParams() {
        // 评测语种
        String language;
        // 评测题型
        String category;
        // 结果等级
        String result_level;

        SharedPreferences pref = mActivity.getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        // 设置评测语言
        language = pref.getString(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置需要评测的类型
        category = pref.getString(SpeechConstant.ISE_CATEGORY, "read_syllable");
        // 设置结果等级（中文仅支持complete）
        result_level = pref.getString(SpeechConstant.RESULT_LEVEL, "complete");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        String vad_bos = pref.getString(SpeechConstant.VAD_BOS, "5000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        String vad_eos = pref.getString(SpeechConstant.VAD_EOS, "1800");
        // 语音输入超时时间，即用户最多可以连续说多长时间；
        String speech_timeout = pref.getString(SpeechConstant.KEY_SPEECH_TIMEOUT, "-1");
        String speech_plev = pref.getString("plev", "0");

        mIse.setParameter(SpeechConstant.LANGUAGE, language);
        mIse.setParameter(SpeechConstant.ISE_CATEGORY, category);
        mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        mIse.setParameter(SpeechConstant.VAD_BOS, vad_bos);
        mIse.setParameter(SpeechConstant.VAD_EOS, vad_eos);
        mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
        mIse.setParameter(SpeechConstant.RESULT_LEVEL, result_level);
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT_AUE,"opus");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
      mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
        //通过writeaudio方式直接写入音频时才需要此设置
        //mIse.setParameter(SpeechConstant.AUDIO_SOURCE,"-1");

        mIse.setParameter("plev", speech_plev);
    }

}
