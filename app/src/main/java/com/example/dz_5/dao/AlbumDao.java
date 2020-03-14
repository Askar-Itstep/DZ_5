package com.example.dz_5.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.dz_5.entity.Album;
import com.example.dz_5.entity.DateConvert;

import java.sql.Date;
import java.util.List;

@Dao
public interface AlbumDao {
    @Query("select * from album")
    List<Album> getAllAlbum();

    @Query("select * from album where id = :id")
    Album getAlbumById(Long id);  //получить фильм по номеру  (исполь-ся в SelectFilmRepository)

    @Query("select * from album where album.title = :title")
    List<Album> getAlbumByTitle(String title);

    @Query("select * from album where album.leadsinger = :singer")
    List<Album> getAlbumBySinger(String singer);

    @Query("select * from album where album.date = :date")
    @TypeConverters(DateConvert.class)
    List<Album> getAlbumByDate(Date date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Album album);                   //сохран. фильм в БД (исполь-ся в InsertAlbumRepository)

    @Update
    void update (Album album);   // cоздать UpdateFilmRepository???

    @Delete
    void delete(Album album);     // DeleteAlbumRepository???
}
