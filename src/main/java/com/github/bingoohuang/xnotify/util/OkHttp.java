package com.github.bingoohuang.xnotify.util;

import lombok.SneakyThrows;
import lombok.val;
import okhttp3.*;

import java.net.Proxy;
import java.util.Map;

public class OkHttp {
    static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    @SneakyThrows
    public static String postJSON(String url, String json, Proxy proxy) {
        val body = RequestBody.create(JSON_MEDIA_TYPE, json);
        val request = new Request.Builder().url(url).post(body).build();
        return execute(request, proxy);
    }

    @SneakyThrows
    public static String postForm(String url, Map<String, String> map, Proxy proxy) {
        val bodyBuilder = new FormBody.Builder();
        for (val e : map.entrySet()) {
            bodyBuilder.add(e.getKey(), e.getValue());
        }

        val request = new Request.Builder().url(url).post(bodyBuilder.build()).build();

        return execute(request, proxy);
    }


    @SneakyThrows
    public static String encodedGet(String url, Map<String, String> encodedQueryParameters, Proxy proxy) {
        val urlBuilder = HttpUrl.parse(url).newBuilder();
        for (val e : encodedQueryParameters.entrySet()) {
            urlBuilder.addEncodedQueryParameter(e.getKey(), e.getValue());
        }

        val request = new Request.Builder().url(urlBuilder.build()).get().build();

        return execute(request, proxy);
    }

    @SneakyThrows
    private static String execute(Request request, Proxy proxy) {
        val clientBuilder = new OkHttpClient.Builder();
        if (proxy != null) clientBuilder.proxy(proxy);

        val client = clientBuilder.build();
        val response = client.newCall(request).execute();
        return response.body().string();
    }
}
