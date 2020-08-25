package com.littlebear.learnhanzi.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_record_table")
public class StudyRecord {

    @PrimaryKey
    @NonNull
    public String hanzi; //所学习的汉字，多音字需要带拼音，主键
    public int  total_times_studied;        //总共已学习的次数
    public String last_studied_time;            //上次学习时间
    public int study_stage;                    //当前所处的学习阶段
}

