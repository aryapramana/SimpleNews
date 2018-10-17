package com.software.tempe.simplenews.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.software.tempe.simplenews.Common.ISO8601DateParser;
import com.software.tempe.simplenews.Interface.ItemClickListener;
import com.software.tempe.simplenews.Model.Article;
import com.software.tempe.simplenews.NewsDetailActivity;
import com.software.tempe.simplenews.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemClickListener clickListener;

    ImageView imgNewsList;
    TextView txtNewsTitle;
    RelativeTimeTextView txtNewsTime;

    public ListNewsViewHolder(@NonNull View itemView) {
        super(itemView);

        imgNewsList = itemView.findViewById(R.id.imgNewsList);
        txtNewsTitle = itemView.findViewById(R.id.txtNewsTitle);
        txtNewsTime = itemView.findViewById(R.id.txtNewsTime);

        itemView.setOnClickListener(this);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v, getAdapterPosition(), false);
    }
}

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder>{
    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.layout_news_list, viewGroup, false);
        return new ListNewsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListNewsViewHolder listNewsViewHolder, final int i) {
        Picasso.get()
                .load(articleList.get(i).getUrlToImage())
                .into(listNewsViewHolder.imgNewsList);

        if (articleList.get(i).getTitle().length() > 75)    {
            listNewsViewHolder.txtNewsTitle.setText(articleList.get(i).getTitle().substring(0, 75) + ".....");
        } else {
            listNewsViewHolder.txtNewsTitle.setText(articleList.get(i).getTitle());
        }

        Date date = null;

        try {
            date = ISO8601DateParser.parse(articleList.get(i).getPublishedAt());
        } catch (ParseException e)  {
            e.printStackTrace();
        }

        assert date != null;
        listNewsViewHolder.txtNewsTime.setReferenceTime(date.getTime());

        listNewsViewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isClick) {
                Intent NewsDetailIntent = new Intent(context, NewsDetailActivity.class);
                NewsDetailIntent.putExtra("web_url", articleList.get(i).getUrl());
                context.startActivity(NewsDetailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
