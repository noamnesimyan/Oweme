package com.example.oweme;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {Depth.class})
public abstract class MyDataBase extends RoomDatabase {

    abstract public MyDao getMyDao();

    private static final String DB_NAME = "MyDataBase.db";
    private static volatile MyDataBase instance;

    static synchronized MyDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private MyDataBase() { }

    private static MyDataBase create(final Context context) {
        return Room.databaseBuilder(context, MyDataBase.class, DB_NAME).build();
    }
}
