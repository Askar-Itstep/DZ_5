package com.example.dz_5;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.dz_5.dao.AlbumDao;
import com.example.dz_5.entity.Album;

@Database(entities = {Album.class}, version = 1)
public abstract class AlbumDB extends RoomDatabase {
    public abstract AlbumDao getAlbumDao();
}
