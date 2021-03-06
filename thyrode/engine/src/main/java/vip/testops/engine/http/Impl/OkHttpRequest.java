package vip.testops.engine.http.Impl;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.testops.engine.http.EasyRequest;
import vip.testops.engine.http.EasyResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OkHttpRequest implements EasyRequest {

    private Logger logger = LoggerFactory.getLogger(OkHttpRequest.class);
    private final OkHttpClient newClient = new OkHttpClient.Builder()
            .followRedirects(false)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private Map<String, String> queryParams;
    private RequestBody requestBody;

    public OkHttpRequest() {
        headers = new HashMap<>();
        cookies = new HashMap<>();
        queryParams = new HashMap<>();
        method = "GET";
        requestBody = null;
    }

    @Override
    public EasyRequest setUrl(String url) {
        this.url=url;
        return this;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public EasyRequest addQueryParam(String paramKey, String value) {
        this.queryParams.put(paramKey,value);
        return this;
    }

    @Override
    public String getQueryParam(String paramKey) {
        return this.queryParams.get(paramKey);
    }

    @Override
    public EasyRequest setQueryParam(Map<String, String> paramMap) {
        this.queryParams=paramMap;
        return this;
    }

    @Override
    public EasyRequest addHeader(String headerName, String headerValue) {
        this.headers.put(headerName,headerValue);
        return this;
    }

    @Override
    public EasyRequest setHeader(Map<String, String> headerMap) {
        this.headers=headerMap;
        return this;
    }

    @Override
    public EasyRequest getHeader(String headerName) {
        this.headers.get(headerName);
        return this;
    }

    @Override
    public Map<String, String> getHearders() {
        return headers;
    }

    @Override
    public EasyRequest addCookie(String cookieName, String cookieValue) {
        this.cookies.put(cookieName,cookieValue);
        return this;
    }

    @Override
    public EasyRequest setCookie(Map<String, String> cookieMap) {
        this.cookies=cookieMap;
        return this;
    }

    @Override
    public EasyRequest setBody(String mimeType, String content) {
        MediaType mediaType = MediaType.parse(mimeType);
        this.requestBody=RequestBody.create(content,mediaType);
        logger.info("request body: {}",content);
        return this;
    }

    @Override
    public EasyRequest setBody(ResponseBody body) {
        this.requestBody=requestBody;
        logger.info("request body: {}",body);
        return this;
    }

    @Override
    public EasyRequest setMethod(String method) {
        this.method=method.toUpperCase();
        return this;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public EasyResponse execute() throws IOException {
        //拼裝url
        Request.Builder newBuilder = new Request.Builder();
        String url = expandUrl(this.url);
        logger.info("target url -> {}",url);
        newBuilder.url(url);

        //设置header
        if (headers != null) {
            headers.forEach(newBuilder::addHeader);
        }

        //设置cookie
        if (cookies!=null){
            StringBuilder cookieString = new StringBuilder();
            cookies.forEach((k,v)-> cookieString.append(k).append("=").append("v").append(";"));
            newBuilder.addHeader("Cookie",cookieString.toString());
        }

        //设置请求方法和body
        newBuilder.method(this.method,this.requestBody);
        Request request=newBuilder.build();

        Response response = newClient.newCall(request).execute();

        EasyResponse easyResponse=new OkHttpResponse(url,response);

        return null;
    }

    private String expandUrl(String url){
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        if (this.queryParams != null & this.queryParams.size() != 0) {
            logger.info("query param: {}",this.queryParams);
            queryParams.forEach(urlBuilder::setQueryParameter);
        }
        return urlBuilder.build().toString();
    }

}
