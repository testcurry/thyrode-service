package vip.testops.engine.http;

import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Map;

public interface EasyRequest {
    String PLAIN = "text/plain; cahrset=utf-8";
    String FORM = "application/x-www-form-urlencoded";
    String JSON = "application/json; cahrset=utf-8";
    String SOAP11 = "application/xml; cahrset=utf-8";
    String SOAP12 = "application/xml+soap; cahrset=utf-8";

    EasyRequest setUrl(String url);

    String getUrl();

    EasyRequest addQueryParam(String paramKey, String value);

    String getQueryParam(String paramKey);

    EasyRequest setQueryParam(Map<String, String> paramMap);

    EasyRequest addHeader(String headerName, String headerValue);

    EasyRequest setHeader(Map<String,String>headerMap);

    EasyRequest getHeader(String headerName);

    Map<String,String> getHearders();

    EasyRequest addCookie(String cookieName, String cookieValue);

    EasyRequest setCookie(Map<String, String> cookieMap);

    EasyRequest setBody(String mimeType, String content);

    EasyRequest setBody(ResponseBody body);

    EasyRequest setMethod(String method);

    String getMethod();

    EasyResponse execute() throws IOException;


}
