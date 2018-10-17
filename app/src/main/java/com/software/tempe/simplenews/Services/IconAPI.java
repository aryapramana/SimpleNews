package com.software.tempe.simplenews.Services;

import com.software.tempe.simplenews.Model.IconFavicon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IconAPI {
    @GET
    Call<IconFavicon> getIcon(@Url String url);
}
