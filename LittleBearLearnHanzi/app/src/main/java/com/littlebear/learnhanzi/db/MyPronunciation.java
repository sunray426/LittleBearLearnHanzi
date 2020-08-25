package com.littlebear.learnhanzi.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_pronunciation_table")
public class MyPronunciation {
    @PrimaryKey
    @NonNull
    public String hanzi; //所学习的汉字，多音字需要带拼音，主键
    public byte[] audio; //录音音频
    public String record_time;     //录制时间
}
