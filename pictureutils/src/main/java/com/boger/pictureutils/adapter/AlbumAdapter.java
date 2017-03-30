package com.boger.pictureutils.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boger.pictureutils.R;
import com.boger.pictureutils.activities.ImageActivity;
import com.boger.pictureutils.model.Album;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubo on 2017/3/28.
 */

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Album> data = new ArrayList<>();
    private ViewGroup parent;
    private Activity activity;

    public AlbumAdapter(List<Album> data, Activity context) {
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
        vh.name.setText(album.name + "(" + album.count + ")");
        Log.d("cover", album.cover);
        Picasso.with(parent.getContext()).load(new File(album.cover)).into(vh.imageView);
        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ImageActivity.class);
                intent.putStringArrayListExtra("data", album.child);
                activity.startActivity(intent);
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
