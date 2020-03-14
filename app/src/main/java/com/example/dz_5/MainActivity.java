package com.example.dz_5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dz_5.DialogFragments.DialogSeachAlbum;
import com.example.dz_5.DialogFragments.DialogSeachAlbumFromDB;
import com.example.dz_5.DialogFragments.Findable;
import com.example.dz_5.entity.Album;
import com.example.dz_5.entity.DBEntity;
import com.example.dz_5.repository.InsertAlbumRepository;
import com.example.dz_5.repository.SelectAlbumRepository;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity implements DBCallback<DBEntity> , Findable {//
    private List<Album> albums = new ArrayList<>();
    private List<Album> dbAlbums = new ArrayList<>();

    private static final String TAG = "===MainActivity===";
    private RecyclerView recyclerView;
    private NavigationView naviView;
    private DrawerLayout drawerMain;
    private Album curDbAlbum;       //а дальше что, как запис. весь альбом, как его проигрывать...???????????
    private AlbumsAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.drawerMain	= (DrawerLayout)	this.findViewById(R.id.drawerMain);
        this.naviView	= (NavigationView)	this.findViewById(R.id.naviView);

        // ----- Toolbar -------------------------------------------------------
        Toolbar toolBar	= (Toolbar)	this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolBar);
        toolBar.setTitle(R.string.app_name);
        toolBar.setTitleTextColor(Color.WHITE);
        CollapsingToolbarLayout collapsingToolbarLayout	=
                (CollapsingToolbarLayout) this.findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setExpandedTitleColor(Color.rgb(0x00, 0x33, 0x66));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        SetAlbums setAlbums = new SetAlbums().invoke();
        albums = setAlbums.getAlbums();
//---------------------------------------------
        InsertAlbumRepository insertAlbumRepository = new InsertAlbumRepository(AlbumApp.getInstance().getDb().getAlbumDao(), this);
        insertAlbumRepository.execute(albums);
        SelectAlbumRepository selectAlbumRepository = new SelectAlbumRepository( AlbumApp.getInstance().getDb().getAlbumDao(), MainActivity.this);
        selectAlbumRepository.execute();

        Log.e(TAG, "База данных загружена!");
        //-----------------------------заполнен. адапт.-------------------------
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.albums);
        this.recyclerView.setLayoutManager(layoutManager);
        adapter = new AlbumsAdapter(MainActivity.this, dbAlbums); //загружено из ДБ !!!!!!!
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        toolBar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolBar.setNavigationOnClickListener(new NavigationView.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.drawerMain.openDrawer(MainActivity.this.naviView);
            }
        });

        this.naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_for_title : {
//                        DialogSeachAlbum dialog = new DialogSeachAlbum();
                        DialogSeachAlbumFromDB dialog = new DialogSeachAlbumFromDB();
                        Bundle bundle = new Bundle();
                        bundle.putString(DialogSeachAlbum.DIALOG_TITLE, "title");
                        bundle.putSerializable("dbAlbums", (Serializable) dbAlbums);
                        dialog.setArguments(bundle);
                        dialog.show(getFragmentManager(), "dialog");
                    }
                        break;

                    case R.id.navigation_for_singer:{
                        DialogSeachAlbum dialog = new DialogSeachAlbum();
                        Bundle bundle = new Bundle();
                        bundle.putString(DialogSeachAlbum.DIALOG_SINGER, "singer");
                        bundle.putSerializable("dbAlbums", (Serializable) dbAlbums);
                        dialog.setArguments(bundle);
                        dialog.show(getFragmentManager(), "dialog");
                    }
                        break;

                    case R.id.navigation_for_date:{
                        DialogSeachAlbum dialog = new DialogSeachAlbum();
                        Bundle bundle = new Bundle();
                        bundle.putString(DialogSeachAlbum.DIALOG_DATE, "date");
                        bundle.putSerializable("dbAlbums", (Serializable) dbAlbums);
                        dialog.setArguments(bundle);
                        dialog.show(getFragmentManager(), "dialog");
                    }
                        break;
                }

                item.setChecked(true);
                MainActivity.this.drawerMain.closeDrawers();
                return true;
            }
        });
    }
    //***************динам. установ. пре-списка фильмов для адаптера (еще до insert DB)

    private class SetAlbums {
        private List<Album> albums;
        public List<Album> getAlbums() {
            return albums;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public SetAlbums invoke() {
            albums = new ArrayList<>();
            albums.add(new Album(1L, "The fat of the land", "The Progigy", new Date(97, 8, 22)));
            albums.add(new Album(2L, "Surrender", "The Chemical Brothers", new Date(99, 5, 22)));
            albums.add(new Album(3L, "Яблокитай", "Nautilus Pompilius", new Date(97, 6, 17)));
            albums.add(new Album(4L, "Рожденный в СССР", "DDT", new Date(97, 4, 14)));
            albums.add(new Album(5L, "Звезда по имени Солнце", "Кино", new Date(89, 9, 12)));
            albums.add(new Album(6L, "Gorillaz", "Gorillaz", new Date(2000, 3, 15)));
//копир. файлов из папки Assets в в директорию PICTURE--------------------------------
    //1)--------вытаскив. потоки из папки Assets ---------------------------------
            if (isExternalStorageWritable()) {
                AssetManager assetManager = MainActivity.this.getAssets();
                List<String> imageNames = new ArrayList<>();
                try {
                    imageNames = new ArrayList<>(Arrays.asList(assetManager.list("")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<InputStream> inputStreams = new ArrayList<>();
                imageNames.stream().filter(i -> i.contains(".")).forEach(i -> {
                    try {
                        inputStreams.add(assetManager.open(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                final List<byte[]> buffers = new ArrayList<>();   //для кажд. фйла свой буффер
                for (InputStream is : inputStreams) {
                    try {
                        buffers.add(new byte[is.available()]);  //заготовки для буфера
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < inputStreams.size(); i++) {
                    try {
                        inputStreams.get(i).read(buffers.get(i));
                        inputStreams.get(i).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

  //2)----------------------перебрoс. в директорию PICTURE ------------------------------------------------------
                List<File> listPicture = new ArrayList<>();
                File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
          //a)-----------созд. файлов в конечн. папке --------------------------------
                    imageNames.stream().filter(i -> i.contains(".png") || i.contains(".jpg"))
                            .forEach( iName -> listPicture.add(new File(picDir, iName)));

        //б)---------------------запись изобр. в файлы-------------------------------------------
                List<FileOutputStream> outputStreams = new ArrayList<FileOutputStream>();
                for (int i = 0; i < listPicture.size(); i++) {
                    try {                                           //извлечение потоков
                        outputStreams.add(new FileOutputStream(listPicture.get(i), false));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < listPicture.size(); i++) {
                    try {
                        outputStreams.get(i).write(buffers.get(i));     //запись
                        outputStreams.get(i).close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        //------------проверка конечн. папки -------------------------------------------------------------
                try {
                    InputStream is = new FileInputStream(listPicture.get(0));
                    Bitmap bmp = BitmapFactory.decodeStream(is); // ;

                    if (bmp != null)
                        Log.e(TAG, "bmp2 IS GOOD!");
                    else Log.e(TAG, "bmp2 IS NULL!"); //??????????????
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

      //==================================================================================================
            if (setAvatarAlbum()) {     //постеры из папки Pictures (пока ручн. перенос)
                Log.d(TAG, "Постеры альбомов перенесены!");
            }
            return this;
        }
        //запасн. метод (не исп)
        private void copy(String fileName, String dir) {
            try {
                AssetManager am = getAssets();
                File destinationFile = new File(dir, fileName);
                InputStream in = am.open(fileName); // открываем файл
                FileOutputStream fos = new FileOutputStream(destinationFile);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        private boolean setAvatarAlbum() {
            int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            File esPicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if(this.isExternalStorageReadable()){
                if (!esPicDir.exists())
                    esPicDir.mkdir();

                File[] files = esPicDir.listFiles();
                if(files == null){
                    Toast.makeText(MainActivity.this, "Каталог носителя пуст!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "В Каталоге нет изображений!");
                    return false;
                }

//                String defaultUri = Arrays.stream(files).filter(fl -> fl.getName().contains("default")).findFirst().get().toURI().getPath();
                String defaultUri = "";
                Optional defaultUriOption = Arrays.stream(files).filter(fl -> fl.getName().regionMatches(true, fl.getName().indexOf(".")+1,"default", 0, 5)).findFirst();
                if(defaultUriOption.isPresent()) {
                    defaultUri = defaultUriOption.get().toString();
                }
                Log.e(TAG, "defaultUri: "+defaultUri);
                for (Album album: albums) {                                                                             //совпадение частей имен до 5 знак
                    Arrays.stream(files).filter(f -> f.isFile()&& f.getName().regionMatches(true, f.getName().indexOf(".")+1, album.getTitle(), 0, 5))
                            .forEach(f -> album.setPathImage(f.toURI().getPath()));
                    if(album.getPathImage()==null ){
                        album.setPathImage(defaultUri); //в худш. случ. - ""
                    }
                }
                albums.forEach(al->Log.e(TAG, "path: " +al.getPathImage()));
                return true;
            }
            return false;
        }

        private boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            return (state.equals(Environment.MEDIA_MOUNTED)||
                    state.equals(Environment.MEDIA_MOUNTED_READ_ONLY));
        }
        private boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            return (state.equals(Environment.MEDIA_MOUNTED) ||
                    state.equals(Environment.MEDIA_MOUNTED_READ_ONLY));
        }
    }//--------конец класса SetAlbum----------------------------

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSelectCollection(List<Album> collection) {
        dbAlbums.clear();
        albums.clear();
        if(collection.get(0) instanceof Album) {
            for (DBEntity album : collection) {
                Log.i("MainActivity", "Selected album collection of title: " + ((Album) album).getTitle());
                dbAlbums.add((Album) album);
            }
            albums = dbAlbums;
        }
    }

    @Override
    public void onSelectSingleItem(DBEntity item) {
        if(item instanceof Album){
            Log.i("MainActivity", "Selected film title: " + ((Album)item).getTitle());
            curDbAlbum = (Album) item;
        }
    }

    @Override
    public void onSave() {
//        Toast.makeText(this, "SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        Snackbar.make(drawerMain, "SAVED SUCCESSFULLY", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onUpdate() {
        Log.d(TAG, "Update!");
        Snackbar.make(drawerMain, "Update is good!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDelete() {
        Log.d(TAG, "Delete!");
        Snackbar.make(drawerMain, "Объект удален!", Snackbar.LENGTH_LONG).show();
    }

   //======================методы для возврата из фрагмента ===============================
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void findAlbum(List<Album> albums) {
        if(albums != null) {
            Snackbar.make(drawerMain, "Пока все неплохо!", Snackbar.LENGTH_LONG).show();
        }
        else {
            Log.e(TAG, "Выборка IS NULL!");
            Snackbar.make(drawerMain, "Объект IS NULL!", Snackbar.LENGTH_LONG).show();
        }
//        dbAlbums.forEach(a->Log.e(TAG, "dbAlbum: "+ a.getTitle()));
        adapter = new AlbumsAdapter(MainActivity.this, albums); //загружено временн. список (в дальн. уйдет)
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
