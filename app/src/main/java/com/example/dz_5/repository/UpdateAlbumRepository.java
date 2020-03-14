package com.example.dz_5.repository;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.dz_5.DBCallback;
import com.example.dz_5.dao.AlbumDao;
import com.example.dz_5.entity.Album;

import java.util.List;

public class UpdateAlbumRepository extends AsyncTask<Object, Void, Void> {
    private AlbumDao dao;
    private DBCallback callback;
    private static String TAG = "===UpdateAlbumRepository===";

    public UpdateAlbumRepository(AlbumDao dao, DBCallback callback) {
        this.dao = dao;
        this.callback = callback;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Void doInBackground(Object... objects) {
        if(objects[0] instanceof List){
            List<Album> filmes = (List<Album>) objects[0];
            filmes.forEach(f->dao.update(f));
        }
        else dao.update((Album) objects[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        callback.onUpdate();
    }
}
