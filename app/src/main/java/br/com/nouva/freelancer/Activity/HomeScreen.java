package br.com.nouva.freelancer.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.nouva.freelancer.Adapter.AdapterAllUsers;
import br.com.nouva.freelancer.Adapter.AdapterNewUsers;
import br.com.nouva.freelancer.Model.BodySearch;
import br.com.nouva.freelancer.Model.ModelMoreProjects;
import br.com.nouva.freelancer.R;
import br.com.nouva.freelancer.Service.ServiceHome;
import br.com.nouva.freelancer.Utils.ApiGetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    private Context context = this;

    RecyclerView recyclerView, recyclerViewall;
    AdapterNewUsers adapter;
    AdapterAllUsers adapterAllUsers;

    private CardView card_home_design_project, card_home_product_project, card_home_dev_project, card_home_art_project;

    private ProgressBar progress_home;
    private ScrollView scrollView;

    private LinearLayout button_more_new_project, button_more_all_project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        openIDS();
        connectToService();
        connectToServiceAll();

        //set clicks
        button_more_new_project.setOnClickListener(this);
        button_more_all_project.setOnClickListener(this);
        card_home_design_project.setOnClickListener(this);
        card_home_product_project.setOnClickListener(this);
        card_home_dev_project.setOnClickListener(this);
        card_home_art_project.setOnClickListener(this);


    }

    private void openIDS() {
        progress_home = findViewById(R.id.progress_home);
        scrollView = findViewById(R.id.scrool_home);

        button_more_new_project = findViewById(R.id.button_more_new_project);
        button_more_all_project = findViewById(R.id.button_more_all_project);

        card_home_design_project = findViewById(R.id.card_home_design_project);
        card_home_product_project = findViewById(R.id.card_home_product_project);

        card_home_dev_project = findViewById(R.id.card_home_dev_project);
        card_home_art_project = findViewById(R.id.card_home_art_project);
    }

    private void connectToServiceAll() {
        /*Create handle for the RetrofitInstance interface*/
        ServiceHome service = ApiGetService.getRetrofitInstance().create(ServiceHome.class);
        BodySearch bodySearch = new BodySearch("All", "onload");

        service.moreProjects(bodySearch).enqueue(new Callback<List<ModelMoreProjects>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelMoreProjects>> call, @NonNull Response<List<ModelMoreProjects>> response) {
                generateDataListAll(response.body());
                Log.d("respApi", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelMoreProjects>> call, @NonNull Throwable t) {
                Toast.makeText(HomeScreen.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.e("respApi", t.getMessage());
                progress_home.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);


            }
        });
    }

    private void connectToService() {
        progress_home.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        /*Create handle for the RetrofitInstance interface*/
        ServiceHome service = ApiGetService.getRetrofitInstance().create(ServiceHome.class);
        BodySearch bodySearch = new BodySearch("New", "onload");

        service.moreProjects(bodySearch).enqueue(new Callback<List<ModelMoreProjects>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelMoreProjects>> call, @NonNull Response<List<ModelMoreProjects>> response) {
                generateDataList(response.body());
                Log.e("respApi", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelMoreProjects>> call, @NonNull Throwable t) {
                Toast.makeText(HomeScreen.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.e("respApi", t.getMessage());

            }
        });
    }

    private void generateDataList(List<ModelMoreProjects> body) {
        recyclerView = findViewById(R.id.recycler_home_new);
        adapter = new AdapterNewUsers(context, body);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void generateDataListAll(List<ModelMoreProjects> body) {
        recyclerViewall = findViewById(R.id.recycler_home_all);
        adapterAllUsers = new AdapterAllUsers(context, body);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);

        recyclerViewall.setLayoutManager(layoutManager);
        recyclerViewall.setAdapter(adapterAllUsers);

        progress_home.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    private void createIntentExtra(String type, String sub) {
        Intent intent = new Intent(HomeScreen.this, MoreProjectsActivity.class);
        intent.putExtra("from", type);
        intent.putExtra("sub", sub);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_more_new_project:
                createIntentExtra("New", "onload");
                break;
            case R.id.button_more_all_project:
                createIntentExtra("All", "onload");
                break;
            case R.id.card_home_design_project:
                createIntentExtra("Design", "");
                break;
            case R.id.card_home_dev_project:
                createIntentExtra("Development", "");
                break;
            case R.id.card_home_art_project:
                createIntentExtra("Art", "");
                break;
            case R.id.card_home_product_project:
                createIntentExtra("Product", "");
                break;
        }
    }
}
