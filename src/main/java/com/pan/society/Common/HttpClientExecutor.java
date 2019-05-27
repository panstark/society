package com.pan.society.Common;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * HTTP客户端执行类，用于发送HTTP的GET或POST请求。
 *
 * @author wangruiv
 * @date 2017-08-14 11:05:31
 */
public class HttpClientExecutor {
    private final Logger LOGGER = LoggerFactory.getLogger(HttpClientExecutor.class);

    /*
    * Available content Types
    * */
    public static final List<ContentType> CONTENT_TYPES = Arrays.asList(ContentType.TEXT_PLAIN, ContentType.TEXT_HTML,
            ContentType.TEXT_XML, ContentType.APPLICATION_XML, ContentType.APPLICATION_SVG_XML,
            ContentType.APPLICATION_XHTML_XML, ContentType.APPLICATION_ATOM_XML, ContentType.APPLICATION_JSON);

    // Convert mill seconds to second unit
    protected static final int MS_TO_S_UNIT = 1000;

    // https prefix
    protected static final String HTTPS = "https";

    protected static HttpsTrustManager httpsTrustManager = new HttpsTrustManager();

    protected String url;

    protected String method;

    protected int maxConnectionSeconds = 0;

    protected Map<String, String> requestHeaders = new HashMap<>();

    protected Map<String, String> requestParams = new HashMap<>();

    public HttpClientExecutor(String url) {
        this(url, "GET");
    }

    public HttpClientExecutor(String url, String method) {
        this.url = url;
        this.method = method;
    }

    @SuppressWarnings("unchecked")
    public <T extends HttpClientExecutor> T maxConnectionSeconds(int maxConnectionSeconds) {
        this.maxConnectionSeconds = maxConnectionSeconds;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends HttpClientExecutor> T putRequestHeader(String key, String value) {
        this.requestHeaders.put(key, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends HttpClientExecutor> T putRequestParam(String key, String value) {
        this.requestParams.put(key, value);
        return (T) this;
    }

    public <T> T execute(ResponseHandler<T> responseHandler) {
        try {
            final CloseableHttpResponse response = sendRequest();
            T data = responseHandler.handleResponse(response);
            response.close();
            return data;
        } catch (IOException e) {
            LOGGER.error("Send request to url[" + url + "] failed", e);
        }

        return null;
    }

    /*
    * Execute and handle exception by yourself
    * */
    public <T> T executeWithException(ResponseHandler<T> responseHandler) throws IOException {
        final CloseableHttpResponse response = sendRequest();
        T data = responseHandler.handleResponse(response);
        response.close();
        return data;
    }

    protected CloseableHttpResponse sendRequest() throws IOException {
        HttpUriRequest request = retrieveHttpRequest();

        CloseableHttpClient client = retrieveHttpClient();
        return client.execute(request);
    }

    private HttpUriRequest retrieveHttpRequest() {
        final RequestBuilder builder = createRequestBuilder();
        addRequestHeaders(builder);
        addRequestParams(builder);
        return builder.setUri(url).build();
    }

    protected RequestBuilder createRequestBuilder() {
        return RequestBuilder.create(method.toUpperCase());
    }

    protected void addRequestHeaders(RequestBuilder builder) {
        final Set<String> keySet = requestHeaders.keySet();
        for (String key : keySet) {
            builder.addHeader(key, requestHeaders.get(key));
        }
    }

    protected void addRequestParams(RequestBuilder builder) {
        final Set<String> keySet = requestParams.keySet();
        for (String key : keySet) {
            builder.addParameter(key, requestParams.get(key));
        }
    }

    protected CloseableHttpClient retrieveHttpClient() {
        final RequestConfig requestConfig = requestConfig();
        if (isHttps()) {
            // Support SSL
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(createSSLContext());
            return HttpClients.custom().setDefaultRequestConfig(requestConfig).setSSLSocketFactory(
                    sslConnectionSocketFactory).build();
        } else {
            return HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        }
    }

    private RequestConfig requestConfig() {
        final int maxConnMillSeconds = this.maxConnectionSeconds * MS_TO_S_UNIT;
        return RequestConfig.custom().setSocketTimeout(maxConnMillSeconds).setConnectTimeout(maxConnMillSeconds)
                .build();
    }

    protected boolean isHttps() {
        return url.toLowerCase().startsWith(HTTPS);
    }

    private SSLContext createSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new HttpsTrustManager[] {httpsTrustManager}, null);
            return sslContext;
        } catch (Exception e) {
            throw new IllegalStateException("Create SSLContext error", e);
        }
    }


    /**
     * Default X509TrustManager implement
     */
    private static class HttpsTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            //ignore
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            //ignore
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }


    public static void main(String[] args) {
        HttpClientExecutor executor = new HttpClientExecutor("http://test.com", "POST");
        executor.maxConnectionSeconds(30);
        executor.putRequestHeader("Content-Type", "text/xml;charset=UTF-8");
        executor.putRequestParam("id", "test-id");
        executor.putRequestParam("type", "test-type");

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String result = executor.execute(responseHandler);

        System.out.println(result);
    }
}
