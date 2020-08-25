package com.littlebear.learnhanzi.db;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.littlebear.learnhanzi.LittleBearUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class ProgressConfig {
    private static final String configFile = "config.json";
    private static volatile ProgressConfig INSTANCE;
    private static LittleBearUtils utils;


    private int textbookNo;             //正在学习的教材编号
    private int lessonNo;               //正在学习的课程编号
    private int characterNo;            //正在学习的单字编号
    private int readingNo;             //正在学习的阅读编号
    private boolean textbookFinished;   //当前教材是否学习完毕

    private double dailyStudyTime;      //每天学习时间
    private double newTime;             //用于学习新字的时间
    private double reviewTime;          //用于复习的时间

    private JSONObject configJO = null;

    private ProgressConfig() {

    }

    public static ProgressConfig getConfig(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProgressConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProgressConfig();
                    utils = LittleBearUtils.geUtils(context);
                    INSTANCE.readFromJson(context);
                }
            }
        }
        return INSTANCE;
    }


    private void readFromJson(final Context context) {
        String strConfig = utils.buildStringFromAssetFile(configFile);
        try {
            configJO = new JSONObject(strConfig.toString());

            textbookNo = configJO.getInt("textbookNo");
            lessonNo = configJO.getInt("lessonNo");
            characterNo = configJO.getInt("characterNo");
            textbookFinished = configJO.getBoolean("textbookFinished");

            dailyStudyTime = configJO.getDouble("dailyStudyTime");
            newTime = configJO.getDouble("newTime");
            reviewTime = configJO.getDouble("reviewTime");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SaveConfig() {

        try {
            configJO.put("textbookNo",textbookNo);
            configJO.put("lessonNo",lessonNo);
            configJO.put("characterNo",characterNo);
            configJO.put("textbookFinished",textbookFinished);

            configJO.put("dailyStudyTime",dailyStudyTime);
            configJO.put("newTime",newTime);
            configJO.put("reviewTime",reviewTime);
            configJO.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public int getTextbookNo() {
        return textbookNo;
    }

    public void setTextbookNo(int textbookNo) {
        this.textbookNo = textbookNo;
    }

    public int getLessonNo() {
        return lessonNo;
    }

    public void setLessonNo(int lessonNo) {
        this.lessonNo = lessonNo;
    }

    public int getCharacterNo() {
        return characterNo;
    }

    public void setCharacterNo(int characterNo) {
        this.characterNo = characterNo;
    }

    public boolean isTextbookFinished() {
        return textbookFinished;
    }

    public void setTextbookFinished(boolean textbookFinished) {
        this.textbookFinished = textbookFinished;
    }

    public double getDailyStudyTime() {
        return dailyStudyTime;
    }

    public void setDailyStudyTime(double dailyStudyTime) {
        this.dailyStudyTime = dailyStudyTime;
    }

    public double getNewTime() {
        return newTime;
    }

    public void setNewTime(double newTime) {
        this.newTime = newTime;
    }

    public double getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(double reviewTime) {
        this.reviewTime = reviewTime;
    }

    public int getReadingNo() {
        return readingNo;
    }

    public void setReadingNo(int readingNo) {
        this.readingNo = readingNo;
    }
}
