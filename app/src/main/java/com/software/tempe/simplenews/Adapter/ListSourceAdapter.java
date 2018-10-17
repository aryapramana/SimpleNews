package com.software.tempe.simplenews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.tempe.simplenews.Common.Common;
import com.software.tempe.simplenews.Interface.ItemClickListener;
import com.software.tempe.simplenews.Model.IconFavicon;
import com.software.tempe.simplenews.Model.SourceSite;
import com.software.tempe.simplenews.NewsListActivity;
import com.software.tempe.simplenews.R;
import com.software.tempe.simplenews.Services.IconAPI;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ItemClickListener clickListener;

    ImageView imgSource;
    TextView txtSource;
    TextView txtSourceDesc;

    public ListSourceViewHolder(View view)  {
        super(view);

        imgSource = view.findViewById(R.id.imgSource);
        txtSource = view.findViewById(R.id.txtSource);
        txtSourceDesc = view.findViewById(R.id.txtSourceDesc);

        view.setOnClickListener(this);

    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public void onClick(View v) {
        clickListener.onClick(v, getAdapterPosition(), false);
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder>{

    private IconAPI iconAPI;

    private SourceSite site;
    private Context context;

    public ListSourceAdapter(SourceSite site, Context context) {
        this.site = site;
        this.context = context;

        iconAPI = Common.getIconSources();
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.layout_source, viewGroup, false);

        return new ListSourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder listSourceViewHolder, final int i) {
        StringBuilder icon = new StringBuilder("https://besticon-demo.herokuapp.com/allicons.json?url=");
        icon.append(site.getSources().get(i).getUrl());

        // BUG: icon doesn't show properly when scrolling
        iconAPI.getIcon(icon.toString())
                .enqueue(new Callback<IconFavicon>() {
                    @Override
                    public void onResponse(Call<IconFavicon> call, final Response<IconFavicon> response) {
                        assert response.body() != null;
                        try {
                            Picasso.get()
                                    .load(response.body().getIcons().get(0).getUrl())
                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                    .fit()
                                    .placeholder(R.mipmap.newspaper)
                                    .into(listSourceViewHolder.imgSource, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Picasso.get()
                                                    .load(response.body().getIcons().get(0).getUrl())
                                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                                    .error(R.mipmap.newspaper)
                                                    .into(listSourceViewHolder.imgSource, new com.squareup.picasso.Callback() {
                                                        @Override
                                                        public void onSuccess() {

                                                        }

                                                        @Override
                                                        public void onError(Exception e) {
                                                            Log.v("Check your connection: ", e.getMessage());
                                                        }
                                                    });
                                        }
                                    });
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<IconFavicon> call, Throwable t) {

                    }
                });

        listSourceViewHolder.txtSource.setText(site.getSources().get(i).getName());
        listSourceViewHolder.txtSourceDesc.setText(site.getSources().get(i).getDescription());

        listSourceViewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isClick) {
                Intent newsList = new Intent(context, NewsListActivity.class);
                newsList.putExtra("sources", site.getSources().get(i).getId());
                context.startActivity(newsList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return site.getSources().size();
    }
}
