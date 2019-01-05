package com.example.gav.flickrclient.feed;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gav.flickrclient.App;
import com.example.gav.flickrclient.R;
import com.example.gav.flickrclient.api.FlickrApi;
import com.example.gav.flickrclient.model.PhotoItem;
import com.example.gav.flickrclient.model.Result;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.gav.flickrclient.api.FlickrApi.API_KEY;

public class FeedActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;
    private TextView tvText;
    private CoordinatorLayout cl;
    private RecyclerView rvPhotos;
    private Toolbar appToolbar;

    private Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable displayResult;
    private retrofit2.Call<Result> callConnection;

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initViews();
        getPhotosViaRetrofit();
    }

    private void initViews() {
      //tvText = findViewById(R.id.tvText);
        cl = findViewById(R.id.cl);
        rvPhotos = findViewById(R.id.rvPhotos);
        appToolbar = findViewById(R.id.appToolbar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvPhotos.setLayoutManager(gridLayoutManager);

        setSupportActionBar(appToolbar);
    }

    private void getPhotosViaRetrofit() {
        FlickrApi flickrApi = App.getApp(this).getFlickrApi();
        disposable = flickrApi.listRepos("flickr.photos.getRecent", API_KEY, "json", 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    List<PhotoItem> photos = result.getPhotos().getPhoto();
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int imageWidth = size.x / 2;

                    FeedAdapter adapter = new FeedAdapter(photos, imageWidth, photoItem -> {
                        String url = String.format(
                                "https://farm%s.staticflickr.com/%s/%s_%s.jpg",
                                photoItem.getFarm(),
                                photoItem.getServer(),
                                photoItem.getId(),
                                photoItem.getSecret()
                        );
                        Intent intent = new Intent(FeedActivity.this, PhotoActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    });
                    rvPhotos.setAdapter(adapter);
                }, error -> Snackbar
                        .make(cl, getString(R.string.repeat_text), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", view -> {
                            if (disposable != null) {
                                disposable.dispose();
                            }
                            getPhotosViaRetrofit();
                        }).show());
        //makeCall();
    }

    private void makeCall() {
        callConnection.clone().enqueue(
                new retrofit2.Callback<Result>() {
                    @Override
                    public void onResponse(retrofit2.Call<Result> call, retrofit2.Response<Result> response) {

                    }

                    @Override
                    public void onFailure(retrofit2.Call<Result> call, Throwable t) {

                    }
                }
        );
    }

    private void getPhotosViaOkHttp() {
        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url("https://www.flickr.com/services/rest/?"+
                "method=flickr.photos.getRecent&"+
                "api_key="+API_KEY +"&"+
                "format=json&"+
                "nojsoncallback=1")
            .get()
            .build();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody body = response.body();
                    Gson gson = new Gson();
                    Result result = gson.fromJson(new String(body.bytes()), Result.class);

                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < result.getPhotos().getPhoto().size() && i < 3; i++) {
                        builder.append(result.getPhotos().getPhoto().get(i).getTitle());
                    }

                    final String titles = builder.toString();
                    displayResult = new Runnable() {
                        @Override
                        public void run() {
//                            tvText.setText(titles);
                        }
                    };
                    handler.post(displayResult);
                }
            });
        }

    }

    private void getPhotosViaHttpURLConnection() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    URL url = new URL("https://www.flickr.com/services/rest/?"+
                            "method=flickr.photos.getRecent&"+
                            "api_key="+API_KEY +"&"+
                            "format=json&"+
                            "nojsoncallback=1");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    final String json = getStringFormatInputStream(inputStream);

                    Gson gson = new Gson();
                    Result result = gson.fromJson(json, Result.class);

                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < result.getPhotos().getPhoto().size() && i < 3; i++) {
                        builder.append(result.getPhotos().getPhoto().get(i).getTitle());
                    }

                    final String titles = builder.toString();

                    JSONObject jsonObject = new JSONObject(json);
                    final String total = jsonObject.getJSONObject("photos").getInt("total") + "";

                    displayResult = new Runnable() {
                        @Override
                        public void run() {
//                            tvText.setText(titles);
                        }
                    };
                    handler.post(displayResult);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException ignored) {}
                    }

                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (displayResult != null) {
            handler.removeCallbacks(displayResult);
        }

        if (disposable != null) {
            disposable.dispose();
        }
    }

    public static String getStringFormatInputStream(InputStream stream) throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        StringWriter writer = new StringWriter();
        while(-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }

    public interface OnFeedClickListener {
        void onFeedClick(PhotoItem photoItem);
    }
}
