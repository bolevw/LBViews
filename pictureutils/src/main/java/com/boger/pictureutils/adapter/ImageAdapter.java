package com.boger.pictureutils.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boger.pictureutils.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by liubo on 2017/3/28.
 */

public class ImageAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<String> data;
    private ViewGroup parent;

    public ImageAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        return new ImageViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_iamge, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageViewholder vh = (ImageViewholder) holder;
        Picasso.with(parent.getContext()).load(new File(data.get(position))).into(vh.iamge);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ImageViewholder extends ViewHolder {
        ImageView iamge;

        public ImageViewholder(View itemView) {
            super(itemView);
            iamge = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
