package com.example.gav.flickrclient.feed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gav.flickrclient.R;
import com.example.gav.flickrclient.model.PhotoItem;

class FeedViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivPhoto;
    public FeedViewHolder(@NonNull View itemView) {
        super(itemView);
        ivPhoto = itemView.findViewById(R.id.ivPhoto);
    }

    public void bind(PhotoItem photoItem, int imageWidth) {
        String url = String.format(
                "https://farm%s.staticflickr.com/%s/%s_%s.jpg",
                photoItem.getFarm(),
                photoItem.getServer(),
                photoItem.getId(),
                photoItem.getSecret()
        );
        ivPhoto.getLayoutParams().width = imageWidth;
        ivPhoto.getLayoutParams().height = imageWidth;
        ivPhoto.requestLayout();

        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(ivPhoto)
                .load(url)
                .apply(options)
                .into(ivPhoto);
    }
}
