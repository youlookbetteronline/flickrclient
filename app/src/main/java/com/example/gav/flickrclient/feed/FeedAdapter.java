package com.example.gav.flickrclient.feed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gav.flickrclient.R;
import com.example.gav.flickrclient.model.PhotoItem;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private final List<PhotoItem> photos;
    private final int imageWidth;
    private final FeedActivity.OnFeedClickListener listener;

    public FeedAdapter(List<PhotoItem> photos, int imageWidth, FeedActivity.OnFeedClickListener listener) {
        this.photos = photos;
        this.imageWidth = imageWidth;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.feed_item, viewGroup, false);

        final FeedViewHolder feedViewHolder = new FeedViewHolder(view);
        feedViewHolder.itemView.setOnClickListener(v -> {
            int position = feedViewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onFeedClick(photos.get(position));
            }
        });

        return feedViewHolder;
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
