package com.example.dz_5;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.room.Room;

@SuppressLint("Registered")
public class AlbumApp extends Application {
    private static final String DB_NAME = "albumDB";
    private static AlbumApp instance;
    private AlbumDB db;
    private static final String TAG = "===AlbumApp===";


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = Room.databaseBuilder(this, AlbumDB.class, DB_NAME)
                .allowMainThreadQueries().build();
    }

    public static AlbumApp getInstance(){
        return instance;
    }

    public AlbumDB getDb(){
        return db;
    }

}
