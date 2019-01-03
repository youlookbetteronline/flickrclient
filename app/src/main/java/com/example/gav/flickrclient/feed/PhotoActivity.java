package com.example.gav.flickrclient.feed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.gav.flickrclient.R;

public class PhotoActivity extends AppCompatActivity {

    private String url;
    private ImageView ivFullPhoto;
    private ImageView ivBack;
    private ImageView ivShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        initViews();
        loadPhoto();
        initListeners();
    }

    private void initViews() {
        ivFullPhoto = findViewById(R.id.ivFullPhoto);
        ivBack      = findViewById(R.id.ivBack);
        ivShare     = findViewById(R.id.ivShare);
    }

    private void loadPhoto() {
        Glide.with(ivFullPhoto)
                .load(url)
                .into(ivFullPhoto);
    }

    private void initListeners() {
        ivBack.setOnClickListener(v -> {
            PhotoActivity.this.finish();
        });

        ivShare.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_text));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_link)));
        });
    }
}
