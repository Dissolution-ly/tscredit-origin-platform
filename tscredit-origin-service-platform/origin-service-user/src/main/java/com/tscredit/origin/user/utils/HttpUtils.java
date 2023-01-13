package com.tscredit.origin.user.utils;


import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

/**
 * @author liuweiwei
 * @since 2017-05-27 14:50
 */
public class HttpUtils {
    /**
     * 日志记录
     */

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 获取日志
     *
     * @return
     */
    public Logger getLog() {
        return log;
    }

    /**
     * 设置超时时间
     */
    private RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(25000).setConnectTimeout(25000)
            .setSocketTimeout(25000).build();


    /**
     * 创建新连接
     */
    public CloseableHttpClient createHttpClient() {
        return HttpClients.custom().setDefaultRequestConfig(requestConfig).setMaxConnTotal(200).
                setMaxConnPerRoute(400).setRetryHandler(retryHandler).build();
    }

    /**
     * 关闭当前连接
     */
    public void closeClient(CloseableHttpClient httpClient) {
        org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpClient);
    }

    /**
     * 关闭当前连接
     */
    public void closeHttpRequest(HttpRequestBase httpRequest) {
        if (httpRequest != null) {
            httpRequest.releaseConnection();
        }
    }


    /**
     * HttpGet 访问
     *
     * @param url
     * @return
     */
    public String get(String url) {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        try {
            httpClient = createHttpClient();
            httpGet = new HttpGet(url);
            log.info("=====请求参数=======" + url);
            httpGet.addHeader("Connection", "close");
            return httpClient.execute(httpGet, responseHandler);
        } catch (Exception e) {
            log.error("访问链接失败", e);
        } finally {
            closeClient(httpClient);
            closeHttpRequest(httpGet);
            getLog().debug(MessageFormat.format("请求：[{0}]", url));
        }
        return "";
    }
    public String getWithHead(String url, Map<String, Object> headMap) {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        try {
            httpClient = createHttpClient();
            httpGet = new HttpGet(url);
            log.info("=====请求参数=======" + url);
            httpGet.addHeader("Connection", "close");
            httpGet.addHeader("accept", MapUtils.getString(headMap,"accept"));
            httpGet.addHeader(MapUtils.getString(headMap,"keyName"), MapUtils.getString(headMap,"key"));
            return httpClient.execute(httpGet, responseHandler);
        } catch (Exception e) {
            log.error("访问链接失败", e);
        } finally {
            closeClient(httpClient);
            closeHttpRequest(httpGet);
            getLog().debug(MessageFormat.format("请求：[{0}]", url));
        }
        return "";
    }

    /**
     * HttpPost 访问
     *
     * @param url
     * @param parameter
     * @return
     */
    public String post(String url, HttpEntity parameter) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            httpClient = createHttpClient();
            httpPost.setEntity(parameter);
            httpPost.addHeader("Connection", "close");
            return httpClient.execute(httpPost, responseHandler);
        } catch (Exception e) {
            log.error("访问链接失败", e);
        } finally {
            closeClient(httpClient);
            closeHttpRequest(httpPost);
            getLog().debug(MessageFormat.format("请求：[{0}->{1}]", url, parameter));
        }
        return "";
    }


    /**
     * 重试策略
     */
    protected HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException exception, int executionCount,
                                    HttpContext context) {
            System.out.println("Exception:" + exception.getMessage() + "重试 第"
                    + executionCount + "次");

            if (executionCount >= 2) {
                return false;
            }
            return true;
        }
    };
    /**
     * 返回结果格式化
     */
    protected ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        public String handleResponse(final HttpResponse response)
                throws ClientProtocolException, IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException(
                        "Unexpected response status: " + status);
            }
        }
    };

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

}
