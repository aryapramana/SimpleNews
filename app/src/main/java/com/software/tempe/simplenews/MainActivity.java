package com.software.tempe.simplenews;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.software.tempe.simplenews.Adapter.ListSourceAdapter;
import com.software.tempe.simplenews.Common.Common;
import com.software.tempe.simplenews.Model.SourceSite;
import com.software.tempe.simplenews.Services.NewsAPI;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private NewsAPI newsAPI;

    private SwipeRefreshLayout mainLayout;
    private RecyclerView recycler_source;
    private RecyclerView.LayoutManager layoutManager;

    private ListSourceAdapter adapter;

    private android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start database
        Paper.init(this);

        // initialise view
        mainLayout = findViewById(R.id.mainLayout);

        recycler_source = findViewById(R.id.recycler_source);
        recycler_source.setHasFixedSize(true);
        recycler_source.setItemViewCacheSize(20);
        layoutManager = new LinearLayoutManager(this);
        recycler_source.setLayoutManager(layoutManager);

        alertDialog = new SpotsDialog.Builder()
                .setContext(MainActivity.this)
                .build();

        // start API
        newsAPI = Common.getNewsSources();

        mainLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSources(true);
            }
        });

        loadSources(false);

    }

    private void loadSources(boolean update) {
        if (!update)    {
            // read database
            String readCache = Paper.book()
                    .read("cache");
            if (readCache != null && !readCache.isEmpty() && !readCache.equals("null"))  {
                // use Gson to convert json to obj
                SourceSite site =  new Gson().fromJson(readCache, SourceSite.class);
                adapter = new ListSourceAdapter(site, getBaseContext());
                adapter.notifyDataSetChanged();

                recycler_source.setAdapter(adapter);
            } else  {
                // mainLayout.setRefreshing(true);
                alertDialog.show();

                newsAPI.getSources().enqueue(new Callback<SourceSite>() {
                    @Override
                    public void onResponse(@NonNull Call<SourceSite> call, @NonNull Response<SourceSite> response) {
                        adapter = new ListSourceAdapter(response.body(), getBaseContext());
                        adapter.notifyDataSetChanged();

                        recycler_source.setAdapter(adapter);

                        // write to database
                        Paper.book()
                                .write("cache", new Gson().toJson(response.body()));

                        // mainLayout.setRefreshing(false);
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<SourceSite> call, Throwable t) {

                    }
                });
            }
        } else {
            mainLayout.setRefreshing(true);

            newsAPI.getSources().enqueue(new Callback<SourceSite>() {
                @Override
                public void onResponse(@NonNull Call<SourceSite> call, @NonNull Response<SourceSite> response) {
                    adapter = new ListSourceAdapter(response.body(), getBaseContext());
                    adapter.notifyDataSetChanged();

                    recycler_source.setAdapter(adapter);

                    // write to database
                    Paper.book()
                            .write("cache", new Gson().toJson(response.body()));

                    mainLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<SourceSite> call, Throwable t) {

                }
            });
        }
    }
}
