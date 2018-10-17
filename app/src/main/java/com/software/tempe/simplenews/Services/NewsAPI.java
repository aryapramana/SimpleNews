package com.software.tempe.simplenews.Services;

import com.software.tempe.simplenews.Common.Common;
import com.software.tempe.simplenews.Model.News;
import com.software.tempe.simplenews.Model.SourceSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsAPI {
    @GET("v2/sources?apiKey=" + Common.API_KEY)
    Call<SourceSite> getSources();

    @GET
    Call<News> getRecentArticle(@Url String url);
}
