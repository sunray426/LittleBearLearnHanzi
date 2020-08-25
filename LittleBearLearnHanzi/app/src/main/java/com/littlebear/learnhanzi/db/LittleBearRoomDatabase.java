package com.littlebear.learnhanzi.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {StudyRecord.class}, version = 1, exportSchema = false)
public abstract class LittleBearRoomDatabase extends RoomDatabase {

    public abstract StudyRecordDao recordDao();

    private static volatile LittleBearRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static LittleBearRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LittleBearRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LittleBearRoomDatabase.class, "little_bear_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
