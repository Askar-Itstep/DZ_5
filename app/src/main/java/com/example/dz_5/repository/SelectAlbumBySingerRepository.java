package com.example.dz_5.repository;

import android.os.AsyncTask;

import com.example.dz_5.DBCallback;
import com.example.dz_5.dao.AlbumDao;
import com.example.dz_5.entity.Album;

import java.util.List;

public class SelectAlbumBySingerRepository extends AsyncTask<String, Void, Object> {
    private AlbumDao dao;
    private DBCallback callback;

    public SelectAlbumBySingerRepository(AlbumDao dao, DBCallback callback) {
        this.dao = dao;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(String... singers) {
        if (singers != null && singers.length > 0){
            return dao.getAlbumBySinger(singers[0]);
        } else {
            return dao.getAllAlbum();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if (o instanceof List){
            callback.onSelectCollection((List<Album>) o);
        } else {
            callback.onSelectSingleItem((Album)o);
        }
    }
}
