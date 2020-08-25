package com.littlebear.learnhanzi.ui.learning;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.littlebear.learnhanzi.LittleBearUtils;
import com.littlebear.learnhanzi.db.ProgressConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BeginLearningViewModel extends AndroidViewModel {

    private MutableLiveData<Textbook> mCurrentTextbook;
    private MutableLiveData<Lesson> mCurrentLesson;

    private ProgressConfig config = ProgressConfig.getConfig(getApplication());


    public BeginLearningViewModel(Application application) {
        super(application);

        mCurrentTextbook = new MutableLiveData<>();
        mCurrentLesson = new MutableLiveData<>();

        loadStudyProgress();
    }

    //加载学习进度信息
    private void loadStudyProgress() {
        Textbook book = new Textbook();
        Lesson lesson = new Lesson();

        //打开配置json文件
        LittleBearUtils utils = LittleBearUtils.geUtils(getApplication());

        //读取配置文件中的在学教材信息
        boolean bookIsFinished = false;
        book.textbookNo = config.getTextbookNo();
        bookIsFinished = config.isTextbookFinished();

        if( book.textbookNo  == -1 || bookIsFinished ) return;

        //读取配置文件中课程进度信息
        lesson.lessonNo = config.getLessonNo();
        lesson.characterNo = config.getCharacterNo();
        lesson.readingNo = config.getReadingNo();

        //打开教材列表json文件
        String strBooks = utils.buildStringFromAssetFile(utils.booksFile);
        JSONObject books = null;

        //读取在学教材详细信息
        try {
            books = new JSONObject(strBooks);
            JSONArray bookList = books.getJSONArray("books");
            JSONObject bookJson = bookList.getJSONObject(book.textbookNo);
            book.textbookTitle = bookJson.getString("book_title");
            book.bookDir = bookJson.getString("dir");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //保存教材进度信息
        mCurrentTextbook.setValue(book);

        //打开教材json文件
        String lessonsFile = book.bookDir + "/"+ utils.lessonsFile;
        String strLessons = utils.buildStringFromAssetFile(lessonsFile);

        //读取当前课程详细信息
        JSONObject lessons = null;
        try {
            lessons = new JSONObject(strLessons);
            JSONArray lessonList = lessons.getJSONArray("lessons");
            JSONObject lessonJson = lessonList.getJSONObject(lesson.lessonNo);
            lesson.lessonTitle = lessonJson.getString("lesson_title");
            lesson.jsonFile =  book.bookDir + "/" + lessonJson.getString("json");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //保存课程进度信息
        mCurrentLesson.setValue(lesson);
    }

    public static void loadStudyTime() {

    }

    public LiveData<Textbook> getTextBook() {
        return mCurrentTextbook;
    }

    public LiveData<Lesson> getLesson() {
        return mCurrentLesson;
    }

}