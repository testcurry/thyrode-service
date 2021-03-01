package vip.testops.engine.http.Impl;

import okhttp3.Headers;
import okhttp3.Response;
import vip.testops.engine.http.EasyResponse;

public class OkHttpResponse implements EasyResponse {
    public OkHttpResponse(String url, Response response) {
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public <T> T getBody(Class<T> bodyType) {
        return null;
    }

    @Override
    public String getHeader(String headerName) {
        return null;
    }

    @Override
    public Headers getHeaders() {
        return null;
    }

    @Override
    public String getCookie(String cookieName) {
        return null;
    }

    @Override
    public int getCode() {
        return 0;
    }
}
