package com.example.dz_5.DialogFragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.dz_5.AlbumApp;
import com.example.dz_5.DBCallback;
import com.example.dz_5.R;
import com.example.dz_5.entity.Album;
import com.example.dz_5.entity.DBEntity;
import com.example.dz_5.repository.SelectAlbumByDateRepository;
import com.example.dz_5.repository.SelectAlbumBySingerRepository;
import com.example.dz_5.repository.SelectAlbumByTitleRepository;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DialogSeachAlbumFromDB extends DialogFragment implements DBCallback<DBEntity>, View.OnClickListener{
    public final static String DIALOG_TITLE = "dialog_title";
    public final static String DIALOG_SINGER = "dialog_singer";
    public final static String DIALOG_DATE = "dialog_date";
    private EditText edAlbumTitle;
    private static String TAG = "===DialogSeachFilmFromDB===";
    private static List<Album> findAlbums =  new ArrayList<>();;
    private Findable findable;
    private TextView textView;
    private TableLayout tableLayout;
    private List<Album> albums;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.dialog_select_film_fragment, container, false);
        Button btnApplay = view.findViewById(R.id.btnSeachApply);
        Button btnCancel = view.findViewById(R.id.btnSeachCancel);
        btnApplay.setOnClickListener(this::onClick);
        btnCancel.setOnClickListener(this::onClick);
        edAlbumTitle = view.findViewById(R.id.edAlbumTitle);
        Bundle bundle = getArguments();
        textView = view.findViewById(R.id.tv_title_album);
        if (bundle.containsKey(DIALOG_DATE))
            textView.setText("Найти по дате");
        else  if(bundle.containsKey(DIALOG_SINGER))
            textView.setText("Найти по исполнителю");
        tableLayout = view.findViewById(R.id.dialog_select_film);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        findable = (Findable) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSeachApply:{
                Bundle arguments = this.getArguments();
                albums = (List<Album>) arguments.getSerializable("dbAlbums");
                if(arguments.containsKey(DIALOG_TITLE)){
                    String title = edAlbumTitle.getText().toString();
                    Log.e(TAG, "!!!!!!!!!!!!!!!!!!!!!");
                    SelectAlbumByTitleRepository selectAlbumByTitleRepository = new SelectAlbumByTitleRepository(AlbumApp.getInstance().getDb().getAlbumDao(), this);
                    selectAlbumByTitleRepository.execute(title);
                }
                else if(arguments.containsKey(DIALOG_SINGER)){
                    String singer =  edAlbumTitle.getText().toString();
                    SelectAlbumBySingerRepository selectAlbumBySingerRepository = new SelectAlbumBySingerRepository(AlbumApp.getInstance().getDb().getAlbumDao(), this);
                    selectAlbumBySingerRepository.execute(singer);
                }
                else if(arguments.containsKey(DIALOG_DATE)){
                    String dateStr =  edAlbumTitle.getText().toString();
                    Date date = null;
                    try{
                        Date.valueOf(dateStr);
                    }catch (Exception e){
                        Snackbar.make(tableLayout, "Вероятно неправильный ввод даты", Snackbar.LENGTH_LONG).show();
                    }
                    SelectAlbumByDateRepository selectAlbumByDateRepository = new SelectAlbumByDateRepository(AlbumApp.getInstance().getDb().getAlbumDao(), this);
                    selectAlbumByDateRepository.execute(date);
                }
                this.dismiss();
            }
            break;

            case R.id.btnSeachCancel:this.dismiss();
                break;
        }
    }

    @Override
    public void onSelectCollection(List<Album> collection) {
        if(collection.size()==0){
            Log.d(TAG, "Такого фильма нет или неправильный ввод!");
            findable.findAlbum(albums);
        }else {
            findable.findAlbum(collection);
        }
    }

    @Override
    public void onSelectSingleItem(DBEntity item) {
        List<Album> albums = new ArrayList<Album>();
        albums.add((Album)item);
        findable.findAlbum(albums);
    }

    @Override
    public void onSave() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onDelete() {

    }
}
