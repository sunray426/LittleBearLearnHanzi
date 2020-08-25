package com.littlebear.learnhanzi.ui.learning;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.littlebear.learnhanzi.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearningFragment_character#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearningFragment_character extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_WORD_JSON = "wordJson";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mWordJson;
    private String mParam2;

    //private JSONObject mWord;

    private int mWordIndex = 0;
    private TextView mTxtWord = null;


    public LearningFragment_character() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param wordJson Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearnWordsFragment_2.
     */
    // TODO: Rename and change types and number of parameters
    public static LearningFragment_character newInstance(String wordJson, String param2) {
        LearningFragment_character fragment = new LearningFragment_character();
        Bundle args = new Bundle();
        args.putString(ARG_WORD_JSON, wordJson);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWordJson = getArguments().getString(ARG_WORD_JSON);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learning_word, container, false);

        //mTxtWord =  "";//view.findViewById(R.id.word);
        mTxtWord.setText(parseWord());

        Button unknownBtn = view.findViewById(R.id.unknownWord);
        unknownBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTts.speak(mTxtWord.getText(), TextToSpeech.QUEUE_FLUSH, null, null);
        }
        });
        return view;
    }

    void updatePage(String wordJson) {
        mWordJson = wordJson;
        mTxtWord.setText(parseWord());
    }

    private String parseWord() {
        try {
            JSONObject word = new JSONObject(mWordJson);
            return word.getString("characters");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



}
