package com.github.bingoohuang.xnotify;

import lombok.SneakyThrows;
import lombok.val;
import okhttp3.*;

import java.util.Map;

public class OkHttp {
    static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    static final OkHttpClient client = new OkHttpClient();

    @SneakyThrows
    public static String postJSON(String url, String json) {
        val body = RequestBody.create(JSON_MEDIA_TYPE, json);
        val request = new Request.Builder().url(url).post(body).build();
        val response = client.newCall(request).execute();
        return response.body().string();
    }

    @SneakyThrows
    public static String postForm(String url, Map<String, String> map) {
        val bodyBuilder = new FormBody.Builder();
        for (val e : map.entrySet()) {
            bodyBuilder.add(e.getKey(), e.getValue());
        }

        val request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        val response = client.newCall(request).execute();
        return response.body().string();
    }


    @SneakyThrows
    public static String get(String url, Map<String, String> map) {
        val urlBuilder = HttpUrl.parse(url).newBuilder();

        for (val e : map.entrySet()) {
            urlBuilder.addQueryParameter(e.getKey(), e.getValue());
        }

        val request = new Request.Builder().url(urlBuilder.build()).get().build();
        val response = client.newCall(request).execute();
        return response.body().string();
    }
}
