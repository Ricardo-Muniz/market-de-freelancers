package br.com.nouva.freelancer.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import br.com.nouva.freelancer.Adapter.AdapterMore;
import br.com.nouva.freelancer.Model.BodySearch;
import br.com.nouva.freelancer.Model.ModelMoreProjects;
import br.com.nouva.freelancer.R;
import br.com.nouva.freelancer.Service.ServiceHome;
import br.com.nouva.freelancer.Utils.ApiGetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreProjectsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    AdapterMore adapterMore;
    private TextView tv_title_more_items;
    private Context context = this;

    private ProgressBar progress_more_project;
    private LinearLayout layout_more_projects, button_exit_more_project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_more);
        openIds();

        button_exit_more_project.setOnClickListener(this);
    }

    private String getBundleParams() {
        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        if (from != null && !from.isEmpty()) {
            return from;
        }
        return from;
    }

    private String getBundleParamsSub() {
        Intent intent = getIntent();
        String from = intent.getStringExtra("sub");
        if (from != null && !from.isEmpty()) {
            return from;
        }
        return from;
    }


    private void openIds() {
        recyclerView = findViewById(R.id.recycler_more);
        tv_title_more_items = findViewById(R.id.tv_title_more_items);
        progress_more_project = findViewById(R.id.progress_more_project);
        layout_more_projects = findViewById(R.id.layout_more_projects);
        button_exit_more_project = findViewById(R.id.button_exit_more_project);
        initializeRoot();
    }

    private void initializeRoot() {
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        loadMoreData();
        /*recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreData();
            }
        });*/
    }

    private void loadMoreData() {
        String extra = getBundleParams();
        String sub = getBundleParamsSub();
        changeTitleACT(extra);
        serviceConnect(extra, sub);
    }

    private void changeTitleACT(String current) {
        switch (current) {
            case "New":
                tv_title_more_items.setText("New project");
                break;
            case "All":
                tv_title_more_items.setText("All project");
                break;
            case "Design":
                tv_title_more_items.setText("Projects design");
                break;
            case "Development":
                tv_title_more_items.setText("Projects development");
                break;
            case "Art":
                tv_title_more_items.setText("Projects art and illustration");
                break;
            case "Product":
                tv_title_more_items.setText("Projects product");
                break;
        }
    }

    private void serviceConnect(String type, String sub) {
        String subject = null;
        if (!sub.isEmpty()) subject = sub;

        ServiceHome service = ApiGetService.getRetrofitInstance().create(ServiceHome.class);
        BodySearch bodySearch = new BodySearch(type, subject);

        service.moreProjects(bodySearch).enqueue(new Callback<List<ModelMoreProjects>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelMoreProjects>> call, @NonNull Response<List<ModelMoreProjects>> response) {
                generateDataListAll(response.body(), true);
                Log.e("respApi", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelMoreProjects>> call, @NonNull Throwable t) {
                Log.e("respApi", t.getMessage());
                generateDataListAll(null, false);
            }
        });
    }

    private void generateDataListAll(List<ModelMoreProjects> body, Boolean state) {
        if (state) {
            progress_more_project.setVisibility(View.GONE);
            layout_more_projects.setVisibility(View.VISIBLE);
            adapterMore = new AdapterMore(context, body);
            recyclerView.setAdapter(adapterMore);
            adapterMore.notifyDataSetChanged();
        } else {
            progress_more_project.setVisibility(View.GONE);
            layout_more_projects.setVisibility(View.VISIBLE);

            MoreProjectsActivity.super.finish();
            Snackbar.make(findViewById(android.R.id.content), "",Snackbar.LENGTH_LONG).setText("No load data, try again later.").show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_exit_more_project) {
            MoreProjectsActivity.super.finish();
        }
    }
}
