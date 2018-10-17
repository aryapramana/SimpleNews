package com.software.tempe.simplenews.Common;

import com.software.tempe.simplenews.Client.IconClient;
import com.software.tempe.simplenews.Client.RClient;
import com.software.tempe.simplenews.Model.IconFavicon;
import com.software.tempe.simplenews.Services.IconAPI;
import com.software.tempe.simplenews.Services.NewsAPI;

public class Common {
    private static final String BASE_URL = "https://newsapi.org/";

    public static final String API_KEY = "b4fc974ae34c43a5aca21930da1638bc";

    public static NewsAPI getNewsSources() {
        return RClient.getRetrofitClient(BASE_URL).create(NewsAPI.class);
    }

    public static IconAPI getIconSources() {
        return IconClient.getRetrofitClient().create(IconAPI.class);
    }

    public static String getAPIUrl(String source, String apiKey) {
        StringBuilder api_url = new StringBuilder("https://newsapi.org/v2/everything?sources=");
        return api_url.append(source)
                .append("&apiKey=")
                .append(apiKey)
                .toString();
    }

    public static String getAPISearch(String source, String q, String apiKey) {
        StringBuilder api_url = new StringBuilder("https://newsapi.org/v2/everything?sources=");
        return api_url.append(source)
                .append("&q=").append('"').append(q).append('"')
                .append("&sortBy=publishedAt")
                .append("&apiKey=")
                .append(apiKey)
                .toString();
    }
}
