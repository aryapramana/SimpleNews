package com.software.tempe.simplenews;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.SearchView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.software.tempe.simplenews.Adapter.ListNewsAdapter;
import com.software.tempe.simplenews.Common.Common;
import com.software.tempe.simplenews.Model.Article;
import com.software.tempe.simplenews.Model.News;
import com.software.tempe.simplenews.Services.NewsAPI;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListActivity extends AppCompatActivity {
    private RecyclerView recycler_news_list;
    private ListNewsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private SwipeRefreshLayout news_refresh;

    private NewsAPI newsAPI;

    private KenBurnsView imgHeadline;

    private DiagonalLayout headlineLayout;

    private android.app.AlertDialog alertDialog;

    private TextView txtHeadlineAuthor;
    private TextView txtHeadlineTitle;

    private String sources = "";
    private String webHotURL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        newsAPI = Common.getNewsSources();

        alertDialog = new SpotsDialog.Builder()
                .setContext(NewsListActivity.this)
                .build();

        news_refresh = findViewById(R.id.news_refresh);

        imgHeadline = findViewById(R.id.imgHeadline);

        headlineLayout = findViewById(R.id.headlineLayout);

        txtHeadlineTitle = findViewById(R.id.txtHeadlineTitle);
        txtHeadlineAuthor = findViewById(R.id.txtHeadlineAuthor);

        recycler_news_list = findViewById(R.id.recycler_news_list);
        recycler_news_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_news_list.setLayoutManager(layoutManager);


        // pass value
        if (getIntent() != null) {
            sources = getIntent().getStringExtra("sources");
            // totalResult = getIntent().getStringExtra("totalResult");

            if (!sources.isEmpty()) {
                loadNews(sources, false);
            }
        }

        Objects.requireNonNull(getSupportActionBar()).setSubtitle("source: " + sources);

        headlineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NewsDetailIntent = new Intent(getBaseContext(), NewsDetailActivity.class);
                NewsDetailIntent.putExtra("web_url", webHotURL);
                startActivity(NewsDetailIntent);
            }
        });

        news_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(sources, true);
            }
        });
    }

    private void loadNews(String sources, boolean update) {
        if (!update) {
            alertDialog.show();
            newsAPI.getRecentArticle(Common.getAPIUrl(sources, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            alertDialog.dismiss();

                            assert response.body() != null;
                            Picasso.get()
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(imgHeadline);

                            txtHeadlineTitle.setText(response.body().getArticles().get(0).getTitle());
                            txtHeadlineAuthor.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> currentList = response.body().getArticles();

                            currentList.remove(0);
                            adapter = new ListNewsAdapter(currentList, getBaseContext());
                            adapter.notifyDataSetChanged();
                            recycler_news_list.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        } else {
            alertDialog.show();
            newsAPI.getRecentArticle(Common.getAPIUrl(sources, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            alertDialog.dismiss();

                            Picasso.get()
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(imgHeadline);

                            txtHeadlineTitle.setText(response.body().getArticles().get(0).getTitle());
                            txtHeadlineAuthor.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> currentList = response.body().getArticles();

                            currentList.remove(0);
                            adapter = new ListNewsAdapter(currentList, getBaseContext());
                            adapter.notifyDataSetChanged();
                            recycler_news_list.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });

            news_refresh.setRefreshing(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();
        MenuItem searchMenu = menu.findItem(R.id.search_action);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search news...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    loadSearch(sources, query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // loadSearch(sources, newText);
                return false;
            }
        });

        searchMenu.getIcon().setVisible(false, false);

        return true;
    }

    private void loadSearch(String sources, String q) {
        alertDialog.show();
        newsAPI.getRecentArticle(Common.getAPISearch(sources, q, Common.API_KEY))
                .enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        alertDialog.dismiss();

/*                        Picasso.get()
                                .load(response.body().getArticles().get(0).getUrlToImage())
                                .into(imgHeadline);

                        txtHeadlineTitle.setText(response.body().getArticles().get(0).getTitle());
                        txtHeadlineAuthor.setText(response.body().getArticles().get(0).getAuthor());

                        webHotURL = response.body().getArticles().get(0).getUrl();*/

                        assert response.body() != null;
                        List<Article> currentList = response.body().getArticles();

                        // currentList.remove(0);
                        adapter = new ListNewsAdapter(currentList, getBaseContext());
                        adapter.notifyDataSetChanged();
                        recycler_news_list.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {

                    }
                });

        news_refresh.setRefreshing(false);
    }
}
