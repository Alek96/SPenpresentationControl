package com.wpam.spenpresentationcontrol.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SocketAddress.class, SPenEvent.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SocketAddressDao socketAddressDao();

    public abstract SPenEventDao sPenEventDao();
}
