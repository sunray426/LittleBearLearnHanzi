package com.littlebear.learnhanzi.ui.learning;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.littlebear.learnhanzi.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearningFragment_sentence#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearningFragment_sentence extends BaseFragment {
    private static final String ARG_SENTENCE_JSON = "sentenceJson";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mSentenceJson;
    private String mParam2;

    private int mSentenceIndex = 0;
    TextView mTextView = null;


    public LearningFragment_sentence() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearnWordsFragment_4.
     */
    // TODO: Rename and change types and number of parameters
    public static LearningFragment_sentence newInstance(String param1, String param2) {
        LearningFragment_sentence fragment = new LearningFragment_sentence();
        Bundle args = new Bundle();
        args.putString(ARG_SENTENCE_JSON, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSentenceJson = getArguments().getString(ARG_SENTENCE_JSON);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learning_sentence, container, false);

        mTextView = (TextView) view.findViewById(R.id.sentence);
        mTextView.setText(parseSentence());
        mTextView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Called when action mode is first created. The menu supplied
                // will be used to generate action buttons for the action mode
                int min = 0;
                int max = mTextView.getText().length();
                if (mTextView.isFocused()) {
                    final int selStart = mTextView.getSelectionStart();
                    final int selEnd = mTextView.getSelectionEnd();

                    min = Math.max(0, Math.min(selStart, selEnd));
                    max = Math.max(0, Math.max(selStart, selEnd));
                }
                // Perform your definition lookup with the selected text
                final CharSequence selectedText = mTextView.getText().subSequence(min, max);
                System.out.println(selectedText);

                int[] location = new int[2] ;
                mTextView.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                Toast toast = Toast.makeText(getActivity(),selectedText.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if (selectedText.length() > 3 )  {
                    mTts.setSpeechRate(0.75f);
                } else {
                    mTts.setSpeechRate(0.3f);
                }
                mTts.speak(selectedText, QUEUE_FLUSH,null,null);

                return false;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Called when an action mode is about to be exited and
                // destroyed
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

        });


        ImageButton readBtn = view.findViewById(R.id.readit);
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTts.setSpeechRate(0.75f);
                mTts.speak(mTextView.getText(),QUEUE_FLUSH, null,null);
                mTts.setSpeechRate(0.3f);
            }
        });




        return view;
    }

     void updatePage(String sentenceJson) {
         mSentenceJson = sentenceJson;
         mTextView.setText(parseSentence());
    }

    private String parseSentence() {
        try {
            JSONObject sentence = new JSONObject(mSentenceJson);
            return sentence.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
