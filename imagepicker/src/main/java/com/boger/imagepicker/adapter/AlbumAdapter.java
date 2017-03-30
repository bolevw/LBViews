package com.boger.imagepicker.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boger.imagepicker.R;
import com.boger.imagepicker.activities.ImageSelectActivity;
import com.boger.imagepicker.model.Album;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubo on 2017/3/28.
 */

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Album> data = new ArrayList<>();
    private ViewGroup parent;
    private Activity activity;

    public AlbumAdapter(ArrayList<Album> data, Activity context) {
        this.data = data;
        activity = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        return new AlbumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Album album = data.get(position);
        AlbumViewHolder vh = (AlbumViewHolder) holder;
        vh.name.setText(album.path);
        Log.d("cover", album.cover);
        Glide.with(parent.getContext()).load(album.cover)
                .placeholder(R.color.colorAccent)
                .override(200, 200)
                .crossFade()
                .centerCrop()
                .into(vh.imageView);

        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSelectActivity.enter(activity, album.path);
//                Intent intent = new Intent(activity, ImageActivity.class);
//                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.albumIv);
            name = (TextView) itemView.findViewById(R.id.albumNameTv);
        }
    }
}
