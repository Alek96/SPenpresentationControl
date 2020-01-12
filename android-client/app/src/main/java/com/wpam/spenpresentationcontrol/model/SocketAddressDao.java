package com.wpam.spenpresentationcontrol.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SocketAddressDao {
    @Query("SELECT * FROM socketaddress LIMIT 1")
    SocketAddress getFirst();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SocketAddress... socketAddresses);

    @Update
    void updateAll(SocketAddress... socketAddresses);

    @Delete
    void deleteAll(SocketAddress... socketAddresses);
}
