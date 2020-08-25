package com.littlebear.learnhanzi.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LittleBearRepository {
    private StudyRecordDao mStudyRecordDao;
    LiveData<List<StudyRecord>> records;
    List<String> list;

    public LittleBearRepository(Application application) {
        LittleBearRoomDatabase db = LittleBearRoomDatabase.getDatabase(application);
        mStudyRecordDao = db.recordDao();
        records = mStudyRecordDao.queryAllCharactersToBeReviewed();

    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(StudyRecord record) {
        LittleBearRoomDatabase.databaseWriteExecutor.execute(() -> {
            mStudyRecordDao.insert(record);
        });
    }

    public LiveData<List<StudyRecord>> getCharactersToBeReviewed() {
        return records;
    }

}
