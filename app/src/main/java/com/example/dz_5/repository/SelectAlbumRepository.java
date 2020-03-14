package com.example.dz_5.repository;

import android.os.AsyncTask;
import com.example.dz_5.DBCallback;
import com.example.dz_5.dao.AlbumDao;
import com.example.dz_5.entity.Album;

import java.util.List;

//производит выборку из табл. Book
//есть ссылка в MainActivity
public class SelectAlbumRepository extends AsyncTask<Long, Void, Object> {
    private AlbumDao dao;
    private DBCallback callback;

    public SelectAlbumRepository(AlbumDao dao, DBCallback callback) {
        this.dao =  dao;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Long... longs) {
        if (longs != null && longs.length > 0){
            return dao.getAlbumById(longs[0]);
        } else {
            return dao.getAllAlbum();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
//        super.onPostExecute(o);
        if (o instanceof List){
            callback.onSelectCollection((List<Album>) o);
        } else {
            callback.onSelectSingleItem((Album)o);
        }
    }
}
