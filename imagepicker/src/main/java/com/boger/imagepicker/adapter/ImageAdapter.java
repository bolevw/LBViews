package com.boger.imagepicker.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boger.imagepicker.R;
import com.boger.imagepicker.model.Image;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by liubo on 2017/3/28.
 */

public class ImageAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Image> data;
    private ViewGroup parent;

    public ImageAdapter(List<Image> data) {
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
        Glide.with(parent.getContext()).load(data.get(position).path)
                .placeholder(R.color.colorAccent)
                .override(200, 200)
                .crossFade()
                .centerCrop()
                .into(vh.iamge);

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
