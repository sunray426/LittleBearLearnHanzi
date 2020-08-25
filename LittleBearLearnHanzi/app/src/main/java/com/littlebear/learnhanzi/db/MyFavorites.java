package com.littlebear.learnhanzi.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_favorites_table")
public class MyFavorites {
    @PrimaryKey
    public int id;
    public String hanzi;
    public String word; //如果word不空，则表示收藏的是词组，否则收藏的是生字。
    public String add_time; //收藏时间
}
