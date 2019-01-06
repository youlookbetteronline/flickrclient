package com.example.gav.flickrclient.feed;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gav.flickrclient.R;
import com.example.gav.flickrclient.model.PhotoItem;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private final List<PhotoItem> photos;
    private final int imageWidth;

    public FeedAdapter(List<PhotoItem> photos, int imageWidth) {
        this.photos = photos;
        this.imageWidth = imageWidth;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.feed_item, viewGroup, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, int i) {
        feedViewHolder.bind(photos.get(i), imageWidth);

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
