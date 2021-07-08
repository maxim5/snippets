package com.github.maxim5.snippets.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;

public class ClientHttp {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int port = 8080;
    private static final String localhost = "http://localhost:%d".formatted(port);

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void main(String[] args) throws IOException {
        get("/");
        get("/home");

        post("/foo", Arrays.asList(1, 2, 3));
        post("/foo/bar", Collections.singletonMap("x", "y"));
    }

    private static void get(String path) throws IOException {
        String url = "%s%s".formatted(localhost, path);
        log.at(Level.INFO).log("GET %s", url);
        try (Response response = getRaw(url)) {
            log.at(Level.INFO).log("%d", response.code());
            log.at(Level.FINE).log("Headers:\n%s\n", response.headers());
            log.at(Level.FINE).log("Body:\n%s\n", String.valueOf(response.body()));
        }
    }

    private static void post(String path, Object content) throws IOException {
        String url = "%s%s".formatted(localhost, path);
        String json = objectMapper.writeValueAsString(content);
        log.at(Level.INFO).log("POST %s %s", url, json);
        try (Response response = postRaw(url, json)) {
            log.at(Level.INFO).log("%d", response.code());
            log.at(Level.FINE).log("Headers:\n%s\n", response.headers());
            log.at(Level.FINE).log("Body:\n%s\n", String.valueOf(response.body()));
        }
    }

    private static Response getRaw(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return client.newCall(request).execute();
    }

    private static Response postRaw(String url, String json) throws IOException {
        RequestBody body = json != null ? RequestBody.create(json, JSON) : null;
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        return client.newCall(request).execute();
    }
}
