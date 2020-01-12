package com.wpam.spenpresentationcontrol.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SPenEventDao {
    @Query("SELECT * FROM spenevent WHERE id LIKE :sPenEventId LIMIT 1")
    SPenEvent get(String sPenEventId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SPenEvent... sPenEvents);

    @Update
    void updateAll(SPenEvent... sPenEvents);

    @Delete
    void deleteAll(SPenEvent... sPenEvents);
}
