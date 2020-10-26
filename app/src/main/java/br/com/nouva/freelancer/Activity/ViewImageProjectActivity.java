package br.com.nouva.freelancer.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import br.com.nouva.freelancer.R;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class ViewImageProjectActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progress_view_image;
    private CardView cv_exit_image_project;
    private ImageView iv_view_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_project);

        openIDS();

        cv_exit_image_project.setOnClickListener(this);
    }

    private void openIDS() {
        cv_exit_image_project = findViewById(R.id.cv_exit_image_project);
        progress_view_image = findViewById(R.id.progress_view_image);
        iv_view_image = findViewById(R.id.iv_view_image);
        String url = getIntentReceive();
        if (url != null) generateViewImage(url);

    }

    private void generateViewImage(String url) {

        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(url)
                .transition(withCrossFade(factory))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        finalizeUiRun();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        progress_view_image.setVisibility(View.GONE);
                        iv_view_image.setVisibility(View.VISIBLE);
                        cv_exit_image_project.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(iv_view_image);
    }

    private String getIntentReceive() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (!url.isEmpty()) return url;
        return null;
    }

    private void finalizeUiRun() {
        ViewImageProjectActivity.super.finish();
    }

    @Override
    public void onBackPressed() {
        finalizeUiRun();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_exit_image_project) {
            finalizeUiRun();
        }
    }
}
