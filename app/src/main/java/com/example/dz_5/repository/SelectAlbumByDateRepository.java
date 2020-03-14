package com.example.dz_5.repository;

import android.os.AsyncTask;

import com.example.dz_5.DBCallback;
import com.example.dz_5.dao.AlbumDao;
import com.example.dz_5.entity.Album;

import java.sql.Date;
import java.util.List;

public class SelectAlbumByDateRepository extends AsyncTask<Date, Void, Object> {
    private AlbumDao dao;
    private DBCallback callback;

    public SelectAlbumByDateRepository(AlbumDao dao, DBCallback callback) {
        this.dao = dao;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Date... dates) {
        if (dates != null && dates.length > 0){
            return dao.getAlbumByDate(dates[0]);
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
