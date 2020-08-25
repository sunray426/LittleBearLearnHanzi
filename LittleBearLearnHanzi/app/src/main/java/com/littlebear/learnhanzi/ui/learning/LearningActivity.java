package com.littlebear.learnhanzi.ui.learning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.littlebear.learnhanzi.R;
import com.littlebear.learnhanzi.db.StudyRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LearningActivity extends AppCompatActivity implements OnFragmentUpdateListener {

    private final int MSG_FRAGMENT_FINISHED = 123;

    private JSONObject mCurrentHanziEntryJO = null;     //整个汉字条目
    private JSONObject mHanziJO = null;                 //汉字对象，包括字、拼音和发音
    private JSONArray mWordListJO = null;               //汉字对应的词组列表
    private JSONArray mSentenceListJA = null;           //汉字对应的句子列表
    private String mCharacter;                        //汉字对应的字符

    private int mWordIndex = 0;  //当前词组的index
    private int mSentenceIndex = 0;  //当前句子的index

    private List<Reading> mReadingList = null;
    private int mReadingIndex = 0; //当前阅读的index

    private LearningViewModel mViewModel;

    Textbook textbook = null;
    Lesson currentLesson = null;

    private HanziPage mNextPage;  //当前显示的页面

    @SuppressLint("HandlerLeak")
    private Handler mMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_FRAGMENT_FINISHED) {
                //findViewById(R.id.next).setEnabled(true);
            }
            super.handleMessage(msg);
        }
    };

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        textbook = (Textbook) bundle.getSerializable("textbook");
        currentLesson = (Lesson) bundle.getSerializable("lesson");

        //String message = intent.getStringExtra(BeginLearningFragment.EXTRA_MESSAGE);
/*
        mViewModel = new ViewModelProvider(this).get(LearningViewModel.class);

        mViewModel.getRecords().observe(this, new Observer<List<StudyRecord>>() {
            @Override
            public void onChanged(@Nullable final List<StudyRecord> records) {
                // Update the cached copy of the words in the adapter.
                mViewModel.init();
                mViewModel.setLesson(textbook,currentLesson);
                //设置生字表
                //setLesson(message);
                //设置当前汉字对应所有数据项
                mCurrentHanziEntryJO = mViewModel.getNextCharacter(false);
                setCurrentHanziEntry();

                //加载第一个页面
               // setFirstFragment(savedInstanceState);
                try {
                    mNextPage = HanziPage.PRONUNCIATION;
                    rotatePages();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
*/

//        Button nextBtn = findViewById(R.id.next);
//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    rotatePages();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    private void loadFragment(FragmentManager fm, BaseFragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        //ft.replace(R.id.fragment_content, fragment, "fragmentTag");
        ft.commit();
    }

    private void rotatePages() throws JSONException {
        FragmentManager fm = getSupportFragmentManager();
        switch (mNextPage) {
            case PRONUNCIATION:
                loadFragment(fm, LearningFragment_pronoumciation.newInstance(mHanziJO.toString(), ""));;
        //        findViewById(R.id.next).setEnabled(false);
                mNextPage = HanziPage.WRITING;
                break;

            case WRITING:
                loadFragment(fm, LearningFragment_writing.newInstance(mCharacter, ""));
         //       findViewById(R.id.next).setEnabled(true);
                mNextPage = HanziPage.WORD;
                mWordIndex = 0;
                break;

            case WORD:
                /*切换到词组页面*/
                if (mWordListJO != null && mWordListJO.length() > 0) {
                    if (mWordIndex == 0) {
                       loadFragment(fm, LearningFragment_character.newInstance(mWordListJO.getJSONObject(mWordIndex).toString(), ""));
                        mNextPage = HanziPage.WORD;
                        mWordIndex++;
                        break;
                    } else  {
                        if (mWordIndex < mWordListJO.length()) {
                            LearningFragment_character currFragment = (LearningFragment_character) fm.findFragmentByTag("fragmentTag");
                            currFragment.updatePage(mWordListJO.getJSONObject(mWordIndex).toString());
                            mNextPage = HanziPage.WORD;
                            mWordIndex++;
                            break;
                        }
                    }
                }

                //无需break，顺序执行到Sentence页面
                mNextPage = HanziPage.SENTENCE;
                mSentenceIndex = 0;

            case SENTENCE:
                if (mSentenceListJA != null && mSentenceListJA.length() > 0) {
                    if (mSentenceIndex == 0) {
                        loadFragment(fm, LearningFragment_sentence.newInstance(mSentenceListJA.getJSONObject(mSentenceIndex).toString(), ""));
                        mNextPage = HanziPage.SENTENCE;
                        mSentenceIndex++;
                        break;
                    }
                    else {
                        if (mSentenceIndex < mSentenceListJA.length()) {
                            LearningFragment_sentence currFragment = (LearningFragment_sentence) fm.findFragmentByTag("fragmentTag");
                            currFragment.updatePage(mSentenceListJA.getJSONObject(mSentenceIndex).toString());
                            mNextPage = HanziPage.SENTENCE;
                            mSentenceIndex++;
                            break;
                        }
                    }
                }

                mCurrentHanziEntryJO = mViewModel.getNextCharacter(false);

                if (mCurrentHanziEntryJO != null) {
                    setCurrentHanziEntry();
                    mNextPage = HanziPage.PRONUNCIATION;
                } else {
                    mReadingIndex = 0;
                    mNextPage = HanziPage.READING;
                    mReadingList = mViewModel.getReadings();
                }

                //切换到pronunciation或者reading页面
                rotatePages();
                break;

            case READING:
                if (mReadingList != null && mReadingList.size() > 0) {
                    if (mReadingIndex == 0) {
                        loadFragment(fm, LearningFragment_reading.newInstance(mReadingList.get(0).readingTitle, mReadingList.get(0).readingText));
                        mReadingIndex++;
                        mNextPage = HanziPage.READING;
                        break;
                    } else {
                        if (mReadingIndex < mReadingList.size()) {
                            LearningFragment_reading currFragment = (LearningFragment_reading) fm.findFragmentByTag("fragmentTag");
                            currFragment.updatePage(mReadingList.get(mReadingIndex));
                            mReadingIndex++;
                            mNextPage = HanziPage.READING;
                            break;
                        }
                    }
                }

            default:
                System.out.println("Error in rotatePages!");

        }

    }
//
//    private void setFirstFragment(Bundle savedInstanceState) {
//        if (savedInstanceState == null) {
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.fragment_content, LearnWordsFragment_pronoumciation.newInstance(mHanziJO.toString(), ""), "fragmentTag");
//            findViewById(R.id.next).setEnabled(false);
//            mNextPage = HanziPage.PRONUNCIATION;
//            ft.commit();
//        }
//
//    }

//    private void setLesson(String message) {
//        //获得汉字表
//        try {
//            JSONObject json = new JSONObject(message);
//            mHanziTable = json.getJSONArray("characters");
//            mReadingList = json.getJSONArray("readings");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void setCurrentHanziEntry() {
        // 初始化对象
        try {
            //mCurrentHanziEntry =
            mHanziJO = mCurrentHanziEntryJO.getJSONObject("hanzi");
            mCharacter = mHanziJO.getString("character");

            mWordListJO = mCurrentHanziEntryJO.getJSONArray("words");
            mSentenceListJA = mCurrentHanziEntryJO.getJSONArray("sentences");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //初始化索引
        mWordIndex = 0;
        mSentenceIndex = 0;

    }

    @Override
    public void onFragmentUpdate(HanziPage page, int arg1) {
        if (arg1 == -1) { //-1表示已经完成所有汉字的书写。
            Message msg = mMsgHandler.obtainMessage();        //nextBtn.refreshDrawableState();
            msg.what = MSG_FRAGMENT_FINISHED;
            msg.arg1 = arg1;
            mMsgHandler.sendMessage(msg);
            return;
        }

        switch (page) {
            case WRITING:
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                LearningFragment_writing currFragment = (LearningFragment_writing) fm.findFragmentByTag("fragmentTag");
                currFragment.updateCompleted(arg1);
                ft.commit();
                break;
            default:
                break;
        }
    }


}
