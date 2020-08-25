package com.littlebear.learnhanzi.ui.learning;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.littlebear.learnhanzi.IseDemo;
import com.littlebear.learnhanzi.LittleBearUtils;
import com.littlebear.learnhanzi.R;


public class BeginLearningFragment extends Fragment {

    private BeginLearningViewModel viewModel;
    public static final String EXTRA_MESSAGE = "com.littlebear.learnhanzi.MESSAGE";

    private Textbook textBook;
    private Lesson currentLesson;
    private String strLesson;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new  ViewModelProvider(this).get(BeginLearningViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_begin_learning, container, false);
        final TextView textStudying = rootView.findViewById(R.id.textStudying);
        viewModel.getTextBook().observe(getViewLifecycleOwner(), new Observer<Textbook>() {
            @Override
            public void onChanged(@Nullable final Textbook book) {
                textStudying.setText("《" +book.textbookTitle + "》");
                textBook = book;
            }
        });

        viewModel.getLesson().observe(getViewLifecycleOwner(), new Observer<Lesson>() {
            @Override
            public void onChanged(@Nullable final Lesson lesson) {
                currentLesson = lesson;
                LittleBearUtils utils = LittleBearUtils.geUtils(getContext());
                strLesson = utils.buildStringFromAssetFile(currentLesson.jsonFile);

                textStudying.setText(textStudying.getText() +  " " + currentLesson.lessonTitle);
            }
        });

        Button btnBeginLearning = rootView.findViewById(R.id.btn_begin_learning);
        btnBeginLearning.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), LearningActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("textbook", textBook);
                bundle.putSerializable("lesson", currentLesson);
                intent.putExtras(bundle);
                intent.putExtra(EXTRA_MESSAGE,strLesson);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.translate_right_in,R.anim.translate_out);
            }
        });

        Button btnChangeTextbook =  rootView.findViewById(R.id.btn_change_textbook);
        btnChangeTextbook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), IseDemo.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.translate_right_in,R.anim.translate_out);

            }
        });

        return rootView;
    }

}
