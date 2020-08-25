package com.littlebear.learnhanzi.ui.learning;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.littlebear.learnhanzi.LittleBearUtils;
import com.littlebear.learnhanzi.db.LittleBearRepository;
import com.littlebear.learnhanzi.db.ProgressConfig;
import com.littlebear.learnhanzi.db.StudyRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LearningViewModel extends AndroidViewModel {
    private Textbook textbook = null;
    private Lesson currentLesson = null;
    private LiveData<List<StudyRecord>> charactersToBeReviewed;
    private LittleBearRepository repository;

    private boolean bookIsFinshed = false;

    private ProgressConfig config = ProgressConfig.getConfig(getApplication());

    private LittleBearUtils utils;

    private double dailyStudyTime; //每天学习时间，分钟
    private double newTime;        //学习一个新字所需时间（根据实际学习速度估算出来的）,分钟
    private double reviewTime;     //复习一个单字所需时间（根据实际学习速度估算出来的），分钟
    private double remainingTime;  //当天剩余学习时间，分钟

    private int totalNews;        //当天实际需要学习的新字数目
    private int totalReviews;     //当天实际需要复习的单词数目
    private int RationRN;         //要复习的单字和要学习的新字数比例，每复习RationNR个字后，学习一个新字。

    private int numOfReviewed;    //已经复习完的单字数量
    private int numOfStudied;     //已经新学习的单字数量

    private List<StudyRecord> reviewCharacters; //需要复习的汉字的列表
    private List<String> newCharacters = new ArrayList<>(100);  //需要学习的汉字的列表
    private List<StudyRecord> unknownCharacters; //本次学习中没有掌握的汉字列表

    private List<Reading> lessonReadings = new ArrayList<>();

    private JSONObject configJO = null;
    private JSONArray allCharactersInLessonJA; //本课中的所有单字，用于单字拼音测试随机产生拼音


    public LearningViewModel(@NonNull Application application) {
        super(application);
        repository = new LittleBearRepository(application);
        charactersToBeReviewed = repository.getCharactersToBeReviewed();

        utils = LittleBearUtils.geUtils(getApplication());
    }

    public LiveData<List<StudyRecord>> getRecords() {
        return charactersToBeReviewed;
    }

    public void init () {
        reviewCharacters = charactersToBeReviewed.getValue();
        getStudyTimeFromConfig();
        calcNumOfStudies();
    }

    //从配置文件读取需要学习的时间
    private void getStudyTimeFromConfig() {

        dailyStudyTime = config.getDailyStudyTime();
        newTime = config.getNewTime();
        reviewTime = config.getReviewTime();

    }

    //计算需要学习的新字和需要复习的字的数量
    private void calcNumOfStudies() {
        int standardNews  = (int) Math.round(dailyStudyTime / (newTime + reviewTime * 4));
        int leastNews = standardNews - 1;

        int totalNeedToReviews = reviewCharacters.size();
        double totalTimeOfNeedToReview = totalNeedToReviews  * reviewTime;

        double leastTotalNewTime = leastNews * newTime;
        double leftTimeForNews = dailyStudyTime - totalTimeOfNeedToReview;
        double totalNewTime = Math.max(leftTimeForNews, leastTotalNewTime);

        totalNews = (int) Math.round(totalNewTime / newTime);
        totalReviews = (int) Math.round((dailyStudyTime - totalNewTime) / reviewTime);
        RationRN = (int) Math.round((double)totalReviews / (double)totalNews);

    }

    public void setLesson (Textbook book, Lesson lesson) {
        textbook = book;
        currentLesson = lesson;
        if (currentLesson.characterNo == -1) return;

        openLesson();

    }

    private void openLesson() {
        String strLesson = utils.buildStringFromAssetFile(currentLesson.jsonFile);

        try {
            JSONObject lessonJO = new JSONObject(strLesson);
            JSONArray charactersJA = lessonJO.getJSONArray("characters");

            for (int i = currentLesson.characterNo; i < charactersJA.length(); i++) {
                newCharacters.add(charactersJA.getString(i));
            }

            JSONArray readingJA = lessonJO.getJSONArray("readings");
            for (int i = 0; i < readingJA.length(); i++) {
                Reading reading = new Reading();
                reading.readingTitle = readingJA.getJSONObject(i).getString("title");
                reading.readingText =  readingJA.getJSONObject(i).getString("text");
                lessonReadings.add(reading);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setNextLesson() {
        //打开教材json文件
        String lessonsFile = textbook.bookDir + "/"+ utils.lessonsFile;
        String strLessons  = utils.buildStringFromAssetFile(lessonsFile);

        //读取当前课程详细信息
        JSONObject lessons = null;
        try {
            lessons = new JSONObject(strLessons);
            JSONArray lessonJA = lessons.getJSONArray("lessons");

            currentLesson.lessonNo++;
            if (currentLesson.lessonNo == lessonJA.length()) {
                bookIsFinshed = true;
                return;
            }

            JSONObject lessonJO = lessonJA.getJSONObject(currentLesson.lessonNo);
            currentLesson.lessonTitle = lessonJO.getString("lesson_title");
            currentLesson.jsonFile =  textbook.bookDir + "/"+ lessonJO.getString("json");
            currentLesson.characterNo = 0;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //设置完毕，打开课程
        openLesson();

    }


    private String getNewCharacter() {
        if (bookIsFinshed) return "";

//        if (numOfStudied == newCharacters.size()) {
//            setNextLesson();
//        }

        if  (numOfStudied == newCharacters.size()) {
            return "";
        }

       return newCharacters.get(numOfStudied++);
    }

    //获取下一个需要学习的汉字，如果本教材学习完毕，则返回空字符串""。
    //
    public JSONObject getNextCharacter(boolean finished)
    {
        String character = "";
        if (finished) {
            try {
                configJO.put("",currentLesson.lessonNo);
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        if ( numOfReviewed / (numOfStudied + 1) < RationRN) {
            if (numOfReviewed < reviewCharacters.size()) {
                character = reviewCharacters.get(numOfReviewed++).hanzi;
            }
        }
        else {
            character = getNewCharacter();
        }

        if (character.equals("")) {
            return null;
        }

        String charJson = utils.buildStringFromAssetFile("characters/" + character + ".json");
        JSONObject charJO = null;
        try {
            charJO = new JSONObject(charJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return charJO;

    }

    public List<Reading> getReadings () {
        return lessonReadings;
    }

}
