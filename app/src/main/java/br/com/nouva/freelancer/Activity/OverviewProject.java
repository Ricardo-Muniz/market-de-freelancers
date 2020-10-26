package br.com.nouva.freelancer.Activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.gson.Gson;

import java.util.List;

import br.com.nouva.freelancer.Adapter.AdapterShotsUrl;
import br.com.nouva.freelancer.Model.ModelOverview;
import br.com.nouva.freelancer.Model.ModelUrls;
import br.com.nouva.freelancer.Model.Overview;
import br.com.nouva.freelancer.Model.Urls;
import br.com.nouva.freelancer.R;
import br.com.nouva.freelancer.Service.ServiceHome;
import br.com.nouva.freelancer.Utils.ApiGetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class OverviewProject extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OverviewProject";
    private static String URL_SELECTED;
    private static boolean CONTENT_ON;
    private Context context = this;
    private ScrollView scroll_overview_project;
    private ProgressBar progress_overview_project;
    private AdapterShotsUrl adapterShotsUrl;
    private RecyclerView recyclerView;
    private TextView tv_category_overview, tv_title_description_overview, tv_name_user_overview, tv_description_overview, tv_more_details;
    private ImageView iv_wallpaper_overview, iv_icon_more_text_project;
    private LinearLayout layout_icon_more_text_card, layout_card_more_text;
    private CardView cv_more_information_project, cv_exit_overview_project;
    private int angle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_project);
        openIDS();

        layout_icon_more_text_card.setOnClickListener(this);
        cv_exit_overview_project.setOnClickListener(this);
        iv_wallpaper_overview.setOnClickListener(this);
        tv_more_details.setOnClickListener(this);
    }

    private void openIDS() {
        CONTENT_ON = false;
        scroll_overview_project = findViewById(R.id.scroll_overview_project);
        progress_overview_project = findViewById(R.id.progress_overview_project);
        recyclerView = findViewById(R.id.recycler_overview_shots);

        tv_category_overview = findViewById(R.id.tv_category_overview);
        tv_title_description_overview = findViewById(R.id.tv_title_description_overview);
        tv_name_user_overview = findViewById(R.id.tv_name_user_overview);
        iv_wallpaper_overview = findViewById(R.id.iv_wallpaper_overview);

        layout_icon_more_text_card = findViewById(R.id.layout_icon_more_text_card);
        iv_icon_more_text_project = findViewById(R.id.iv_icon_more_text_project);
        cv_more_information_project = findViewById(R.id.cv_more_information_project);
        layout_card_more_text = findViewById(R.id.layout_card_more_text);
        tv_description_overview = findViewById(R.id.tv_description_overview);

        cv_exit_overview_project = findViewById(R.id.cv_exit_overview_project);

        tv_more_details = findViewById(R.id.tv_more_details);

        String key = getIntentACT();
        initializeService(key);
    }

    private String getIntentACT() {
        Intent intent = getIntent();
        String extra = intent.getStringExtra("from");
        if (!extra.isEmpty()) return extra;
        return null;
    }

    private void initializeService(final String key) {
        ServiceHome service = ApiGetService.getRetrofitInstance().create(ServiceHome.class);
        Overview overview = new Overview(key);

        service.seeOverview(overview).enqueue(new Callback<List<ModelOverview>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelOverview>> call, @NonNull Response<List<ModelOverview>> response) {
                if (response.code() == 200) {
                    getUrlsAsstes(key, response);
                    Log.d("respApi", "Response body total: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelOverview>> call, @NonNull Throwable t) {
                Toast.makeText(OverviewProject.this, "Problem to connect to server...", Toast.LENGTH_SHORT).show();
                Log.e("respApi", t.getMessage());
            }
        });
    }

    private void getUrlsAsstes(String key, final Response<List<ModelOverview>> json) {
        ServiceHome service = ApiGetService.getRetrofitInstance().create(ServiceHome.class);
        Urls urls = new Urls(key);

        service.getUlrs(urls).enqueue(new Callback<List<ModelUrls>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelUrls>> call, @NonNull Response<List<ModelUrls>> response) {
                if (response.code() == 200) {
                    createOverviewProject(json.body(), response.body());

                    Gson gson = new Gson();
                    String over = gson.toJson(response.body());
                    Log.d("respApi", "Response body urls: " + response.code());
                    Log.d("respApi", over);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelUrls>> call, @NonNull Throwable t) {

            }
        });


    }

    private void createOverviewProject(List<ModelOverview> body, List<ModelUrls> urls) {
        //String joined = TextUtils.join(", ", body);
        // String joined = TextUtils.join(", ", body);
        //Toast.makeText(context, joined, Toast.LENGTH_LONG).show();

        adapterShotsUrl = new AdapterShotsUrl(context, urls);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterShotsUrl);

        createInfosExtra(body);

    }

    private void createInfosExtra(List<ModelOverview> body) {
        for (int i = 0; i < body.size(); i++) {
            ModelOverview overview = new ModelOverview(body.get(i).getContent(), body.get(i).getCreated(),
                    body.get(i).getId_project(), body.get(i).getId_user(), body.get(i).getName(), body.get(i).getProfile(),
                    body.get(i).getTitle(), body.get(i).getType(), body.get(i).getWallpaper());

            String name = afterTextChanged(overview.getName());
            tv_name_user_overview.setText(name);
            tv_category_overview.setText(overview.getType() + " —");
            tv_title_description_overview.setText(overview.getTitle());
            tv_description_overview.setText(overview.getContent());

            Glide.with(context)
                    .load(overview.getWallpaper())
                    .apply(new RequestOptions()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(800, 800))
                    .into(iv_wallpaper_overview);

            progress_overview_project.setVisibility(View.GONE);
            scroll_overview_project.setVisibility(View.VISIBLE);
        }
    }

    public void changeImage(String url) {
        if (!url.equals(URL_SELECTED)) {
            URL_SELECTED = url;
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
                            .override(800, 800))
                    .into(iv_wallpaper_overview);
        }
    }

    public String afterTextChanged(String source) {
        return String.valueOf(source).toLowerCase().replace(" ", "");
    }

    private void translateTextCard() {
        int visibility;
        if (!CONTENT_ON) {
            visibility = View.VISIBLE;
            tv_more_details.setTextColor(ContextCompat.getColor(context, R.color.colorSecondary));
            iv_icon_more_text_project.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_arrow_dowm_list_category_secondary));
            CONTENT_ON = true;
        } else {
            visibility = View.GONE;
            tv_more_details.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            iv_icon_more_text_project.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_arrow_dowm_list_category));
            CONTENT_ON = false;
        }
        TransitionManager.beginDelayedTransition(layout_card_more_text, new AutoTransition().setDuration(500));
        layout_card_more_text.setVisibility(visibility);
        flipAnimation();
    }

    private void flipAnimation() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_icon_more_text_project, "rotation", angle, angle + 180);
        anim.setDuration(500);
        anim.start();
        angle += 180;
        angle = angle % 360;
        Log.d(TAG, "Angle: " + angle);
    }

    private void finalizeUiRun() {
        OverviewProject.super.finish();
    }

    @Override
    public void onBackPressed() {
        finalizeUiRun();
    }

    private void openViewImage() {
      Intent intent = new Intent(OverviewProject.this, ViewImageProjectActivity.class);
      intent.putExtra("url", URL_SELECTED);
      startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_icon_more_text_card:
                translateTextCard();
                break;
            case R.id.cv_exit_overview_project:
                finalizeUiRun();
                break;
            case R.id.iv_wallpaper_overview:
                openViewImage();
                break;
            case R.id.tv_more_details:
                translateTextCard();
                break;
        }
    }

}
