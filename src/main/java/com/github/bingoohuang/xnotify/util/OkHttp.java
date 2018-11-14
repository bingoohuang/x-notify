package com.github.bingoohuang.xnotify.util;

import lombok.SneakyThrows;
import lombok.val;
import okhttp3.*;

import java.net.Proxy;
import java.util.Map;

public class OkHttp {
    public static final OkHttpClient CLIENT = new OkHttpClient();

    @SneakyThrows
    public static String postJSON(String url, String json, Proxy proxy) {
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        val request = new Request.Builder().url(url).post(body).build();
        return getClient(proxy).newCall(request).execute().body().string();
    }

    @SneakyThrows
    public static String postForm(String url, Map<String, String> map, Proxy proxy) {
        val bodyBuilder = new FormBody.Builder();
        for (val e : map.entrySet()) {
            bodyBuilder.add(e.getKey(), e.getValue());
        }

        val request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        return getClient(proxy).newCall(request).execute().body().string();
    }


    @SneakyThrows
    public static String encodedGet(String url, Map<String, String> encodedQueryParameters, Proxy proxy) {
        val urlBuilder = HttpUrl.get(url).newBuilder();
        encodedQueryParameters.forEach(urlBuilder::addEncodedQueryParameter);

        val request = new Request.Builder().url(urlBuilder.build()).get().build();
        return getClient(proxy).newCall(request).execute().body().string();
    }

    /**
     * 获得OkHttpClient实例。
     * proxy对象示例:
     * <code>
     * Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.105", 8081);
     * </code>
     *
     * @param proxy 代理对象。
     * @return 定制化后的OkHttpClient对象。
     */
    @SneakyThrows
    public static OkHttpClient getClient(Proxy proxy) {
        // https://square.github.io/okhttp/3.x/okhttp/okhttp3/OkHttpClient.html
        // You can customize a shared OkHttpClient instance with newBuilder().
        // This builds a CLIENT that shares the same connection pool, thread pools, and configuration.
        // Use the builder methods to configure the derived CLIENT for a specific purpose.
        return proxy != null ? CLIENT.newBuilder().proxy(proxy).build() : CLIENT;
    }
}
