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
import android.widget.TextView;
import android.widget.Toast;

import com.littlebear.learnhanzi.R;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearningFragment_reading#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearningFragment_reading extends BaseFragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_READING_TITLE= "title";
    private static final String ARG_READING_TEXT = "text";

    private Reading mReading = new Reading();

    private TextView mTitle =null;
    private TextView mText = null;

    public LearningFragment_reading() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param readingTitle Parameter 1.
     * @param readingText Parameter 2.
     * @return A new instance of fragment LearnWordsFragment_Reading.
     */
    // TODO: Rename and change types and number of parameters
    public static LearningFragment_reading newInstance(String readingTitle, String readingText) {
        LearningFragment_reading fragment = new LearningFragment_reading();
        Bundle args = new Bundle();
        args.putString(ARG_READING_TITLE, readingTitle);
        args.putString(ARG_READING_TEXT, readingText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReading.readingTitle = getArguments().getString(ARG_READING_TITLE);
            mReading.readingText = getArguments().getString(ARG_READING_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learning_reading, container, false);

        mTitle = view.findViewById(R.id.readingTitle);
        mTitle.setText(mReading.readingTitle);
        mText = view.findViewById(R.id.readingText);
        mText.setText(mReading.readingText);

        mText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Called when action mode is first created. The menu supplied
                // will be used to generate action buttons for the action mode
                int min = 0;
                int max = mText.getText().length();
                if (mText.isFocused()) {
                    final int selStart = mText.getSelectionStart();
                    final int selEnd = mText.getSelectionEnd();

                    min = Math.max(0, Math.min(selStart, selEnd));
                    max = Math.max(0, Math.max(selStart, selEnd));
                }
                // Perform your definition lookup with the selected text
                final CharSequence selectedText = mText.getText().subSequence(min, max);
                System.out.println(selectedText);

                int[] location = new int[2] ;
                mText.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                Toast toast = Toast.makeText(getActivity(),selectedText.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
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


        return view;
    }


    void updatePage(Reading reading) {
        mTitle.setText(reading.readingTitle);
        mText.setText(reading.readingText);
    }

}
