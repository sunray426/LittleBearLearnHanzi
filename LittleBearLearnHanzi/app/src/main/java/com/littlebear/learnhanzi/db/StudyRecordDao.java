package com.littlebear.learnhanzi.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StudyRecordDao {
    final String sql_characters_to_be_reviewed = "select * from study_record_table " +
            "where date(last_studied_time, 'start of day') <=  date('now', 'start of day', '-' || ( (2 << (study_stage - 1)) - 1) || ' day')" +
            "order by study_stage asc, last_studied_time asc";

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StudyRecord record);

    @Query(sql_characters_to_be_reviewed)
    LiveData<List<StudyRecord>> queryAllCharactersToBeReviewed();


    @Query("SELECT * FROM study_record_table")
    List<StudyRecord> queryAllRecords();


    @Query("UPDATE study_record_table SET study_stage = :stage where hanzi = :hanzi")
    void updateRecord(String hanzi, int stage);

    @Delete
    void delete(StudyRecord record);
}

