package com.example.dz_5.repository;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dz_5.DBCallback;
import com.example.dz_5.dao.AlbumDao;
import com.example.dz_5.entity.Album;

import java.util.List;

public class InsertAlbumRepository extends AsyncTask<Object, Void, Void> {
    private AlbumDao dao;
    private DBCallback callback;
    private static final String TAG = "===InsetAlbumRepository===";

    @SuppressLint("LongLogTag")
    public InsertAlbumRepository(AlbumDao dao, DBCallback callback) {
        this.dao = dao;
        this.callback = callback;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Void doInBackground(Object... objects) {
        if(objects[0] instanceof List){
            List<Album> albums = (List<Album>) objects[0];
            albums.forEach(a->dao.save(a));
        }
        else dao.save((Album) objects[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
        callback.onSave();
    }
}
