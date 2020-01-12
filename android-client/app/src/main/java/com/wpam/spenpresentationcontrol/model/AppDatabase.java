package com.wpam.spenpresentationcontrol.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SocketAddress.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SocketAddressDao socketAddressDao();
}
