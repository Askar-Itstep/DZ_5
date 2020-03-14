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

import com.example.dz_5.R;
import com.example.dz_5.entity.Album;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DialogSeachAlbum extends DialogFragment implements View.OnClickListener{
    public final static String DIALOG_TITLE = "dialog_title";
    public final static String DIALOG_SINGER = "dialog_singer";
    public final static String DIALOG_DATE = "dialog_date";
    private EditText edAlbumTitle;
    private static String TAG = "===DialogSeachFilm===";
//    private String title;
    private static List<Album> dbAlbums;
    private Findable findable;
    private TextView textView;
    private TableLayout tableLayout;

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
        dbAlbums = (List<Album>) bundle.getSerializable("dbAlbum");
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
                if(arguments.containsKey(DIALOG_TITLE)){
                    String title = edAlbumTitle.getText().toString();
                    List<Album> albums = (List<Album>) arguments.getSerializable("dbAlbums");
                    List<Album> findAlbums =  new ArrayList<>();
                    albums.stream().filter(f->f.getTitle().regionMatches(true, 0, title, 0,5)).forEach((f->findAlbums.add(f)));// .equals(title)).forEach(f->findAlbums.add(f));
                    if(findAlbums.size()==0){
                        Log.d(TAG, "Такого фильма нет или неправильный ввод!");
                        findable.findAlbum(albums);
                    }else {
                        findable.findAlbum(findAlbums);
                    }
                }
                else if(arguments.containsKey(DIALOG_SINGER)){
                    String singer =  edAlbumTitle.getText().toString();
                    List<Album> albums = (List<Album>) arguments.getSerializable("dbAlbums");
                    List<Album> findAlbums =  new ArrayList<>();
                    albums.stream().filter(f->f.getLeadsinger().regionMatches(true, 0, singer, 0,5)).forEach((f->findAlbums.add(f)));
                    if(findAlbums.size()==0){
                        Log.d(TAG, "Такого фильма нет или неправильный ввод!");
                        findable.findAlbum(albums);
                    }else {
                        findable.findAlbum(findAlbums);
                    }
                }
                else if(arguments.containsKey(DIALOG_DATE)){
                    String dateStr =  edAlbumTitle.getText().toString();
                    Date date = null;
                    try{
                        Date.valueOf(dateStr);
                    }catch (Exception e){
                        Snackbar.make(tableLayout, "Вероятно неправильный ввод даты", Snackbar.LENGTH_LONG).show();
                    }
                    List<Album> albums = (List<Album>) arguments.getSerializable("dbAlbums");
                    List<Album> findAlbums =  new ArrayList<>();
                    albums.stream().filter(f->f.getDate().equals(date)).forEach((f->findAlbums.add(f)));
                    if(findAlbums.size()==0){
                        Log.d(TAG, "Такого фильма нет или неправильный ввод!");
                        findable.findAlbum(albums);
                    }else {
                        findable.findAlbum(findAlbums);
                    }
                }
                this.dismiss();
            }
            break;

            case R.id.btnSeachCancel:this.dismiss();
            break;
        }
    }
}
