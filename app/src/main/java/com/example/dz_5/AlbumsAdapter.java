package com.example.dz_5;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dz_5.entity.Album;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumsViewHolder> {
    private static final String TAG = "===AlbumsAdapter===";
    private List<Album> albums;
    private	Context	context;
    private static int k = 0;

    public AlbumsAdapter(Context context, List<Album> albums) {
        this.context = context; this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albums, parent, false);
        return new AlbumsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull AlbumsViewHolder holder, int position) {
        holder.tvSinger.setText(getAlbum(position).getLeadsinger());
        holder.tvDate.setText(getAlbum(position).getDate().toString());
        holder.tvTitle.setText(getAlbum(position).getTitle());
//        holder.tvId.setText((getAlbum(position).getId().toString()));
        if(getAlbum(position).getPathImage() == null)
            Log.d(TAG, "img IS NULL!");
        holder.ivImg.setImageURI(Uri.parse(getAlbum(position).getPathImage()));
//---------------установ. задн. фона------------------------------
        AssetManager manager = this.context.getAssets();
        String[] stringList = null;
        try {
            stringList = manager.list("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> streamList = new ArrayList<>();
        Arrays.stream(stringList).filter(s->!s.contains("album") && s.contains(".png") || s.contains(".jpg")).forEach(s->streamList.add(s));
        InputStream[] inputStreams = new InputStream[streamList.size()];
        for(int i = 0; i < streamList.size(); i++){
            try {
                inputStreams[i] = manager.open(streamList.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Random random = new Random();
//        int j = random.nextInt(streamList.length);
        Log.d(TAG, "streamList.size: "+streamList.size());
//--------установ. задн. стенки-------------
        holder.cardView.setBackground(new BitmapDrawable(BitmapFactory.decodeStream(inputStreams[k++%streamList.size()])));
    }
    public Album getAlbum(int position) {
        return albums.get(position);
    }
    @Override
    public int getItemCount() {
        if(albums == null) return 0;
        else return albums.size();
    }

    public class AlbumsViewHolder extends RecyclerView.ViewHolder {
//        TextView tvId;
        TextView tvTitle;
        ImageView ivImg;
        TextView tvDate;
        TextView tvSinger;
        CardView cardView;

        public AlbumsViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvId = itemView.findViewById(R.id.tvId);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSinger = itemView.findViewById(R.id.tvSinger);
            ivImg = itemView.findViewById(R.id.ivImg);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
