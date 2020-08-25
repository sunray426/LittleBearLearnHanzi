package com.littlebear.learnhanzi.ui.learning;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.littlebear.learnhanzi.R;

import java.util.ArrayList;
import java.util.List;

public class LearingWordAdapter extends RecyclerView.Adapter<LearingWordAdapter.MyViewHoder> {
    List<Lesson> allWord = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MyViewHoder extends RecyclerView.ViewHolder {
        TextView textViewWord, textViewpinyin, textViewWordType, textViewSentence;
        ImageView imagePlayWord, imagePlaySentence, imageFavorite;
        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.textViewWord);
            textViewpinyin = itemView.findViewById(R.id.textVievPinyin);
            textViewWordType = itemView.findViewById(R.id.textViewWordType);
            textViewSentence = itemView.findViewById(R.id.textViewSentence);

            imagePlayWord = itemView.findViewById(R.id.imagePlayWord);
            imagePlaySentence = itemView.findViewById(R.id.imagePlayWord);
            imageFavorite = itemView.findViewById(R.id.imagePlayWord);

        }
    }
}
