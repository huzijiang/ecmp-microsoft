package com.hq.ecmp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author xueyong
 */
@Slf4j
public class HttpClientUtil {

    public static String doGet(String url, Map<String, String> param, Map<String, String> header) {
        String resultString = null;
        CloseableHttpResponse response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet = (HttpGet) setHeader(httpGet, header);
            response = httpClient.execute(httpGet);
            resultString = dealResponse(response);
        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return resultString;
    }

    //form表单
    public static String doPost(String url, Map<String, Object> param, Map<String, String> header) {
        CloseableHttpResponse response;
        String resultString = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost = (HttpPost) setHeader(httpPost, header);
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, (String) param.get(key)));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            response = httpClient.execute(httpPost);
            resultString = dealResponse(response);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return resultString;
    }

    /**
     * json style
     */
    public static String doPostJson(String url, String json, Map<String, String> header) {
        CloseableHttpResponse response;
        String resultString = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost = (HttpPost) setHeader(httpPost, header);
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json;charset=utf-8");
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            resultString = dealResponse(response);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return resultString;
    }

    //多文件
    public static String httpPostFormMultipartFiles(String url, Map<String, String> params, List<File> files, Map<String, String> headers) {
        String encode = "utf-8";
        String resultString = null;
        CloseableHttpResponse httpResponse;
        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            //设置header
            httpPost = (HttpPost) setHeader(httpPost, headers);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
            mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            mEntityBuilder.setCharset(Charset.forName(encode));
            // 普通参数
            //解决中文乱码
            ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));
            if (params != null && params.size() > 0) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    mEntityBuilder.addTextBody(key, params.get(key), contentType);
                }
            }
            //二进制参数
            //多文件处理
            if (files != null && files.size() > 0) {
                for (File file : files) {
                    mEntityBuilder.addBinaryBody("file", file);
                }
            }
            httpPost.setEntity(mEntityBuilder.build());
            httpResponse = closeableHttpClient.execute(httpPost);
            resultString = dealResponse(httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultString;
    }

    //单文件
    public static String httpPostFormMultipartFile(String url, Map<String, String> params, File file, Map<String, String> headers) {
        String encode = "utf-8";
        String resultString = null;
        CloseableHttpResponse httpResponse;
        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            //设置header
            HttpPost httpPost = new HttpPost(url);
            httpPost = (HttpPost) setHeader(httpPost, headers);
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
            mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            mEntityBuilder.setCharset(Charset.forName(encode));
            // 普通参数
            //解决中文乱码
            ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));
            if (params != null && params.size() > 0) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    mEntityBuilder.addTextBody(key, params.get(key), contentType);
                }
            }
            //二进制参数
            if (file != null) {
                mEntityBuilder.addBinaryBody("file", file);
            }
            httpPost.setEntity(mEntityBuilder.build());
            httpResponse = closeableHttpClient.execute(httpPost);
            resultString = dealResponse(httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultString;
    }


    //zimg单文件上传
    public static String postFileToImage(String url, File file) {
        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String resultString = null;
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "no-cache");
        headers.put("Content-Type", ext.toLowerCase());
        headers.put("COOKIE", "gongche");
        CloseableHttpResponse httpResponse;
        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            //设置header
            httpPost = (HttpPost) setHeader(httpPost, headers);
            byte[] bytes = StringFileUtil.file2byte(file);
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
            httpPost.setEntity(byteArrayEntity);
            httpResponse = closeableHttpClient.execute(httpPost);
            resultString = dealResponse(httpResponse);
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        return resultString;
    }

    public static String postMultipartFileToImage(String url, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String resultString = null;
        Map<String, String> headers = new HashMap<>();
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "no-cache");
        headers.put("Content-Type", ext.toLowerCase());
        headers.put("COOKIE", "gongche");
        CloseableHttpResponse httpResponse;
        try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            //设置header
            httpPost = (HttpPost) setHeader(httpPost, headers);
            byte[] bytes = StringFileUtil.toByteArray(file.getInputStream());
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
            httpPost.setEntity(byteArrayEntity);
            httpResponse = closeableHttpClient.execute(httpPost);
            resultString = dealResponse(httpResponse);
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        return resultString;
    }

    private static HttpRequestBase setHeader(HttpRequestBase httpRequestBase, Map<String, String> header) {
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpRequestBase.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpRequestBase;
    }

    private static String dealResponse(CloseableHttpResponse response) {
        String resultString = null;
        String successStatus = "20";
        try {
            if (response != null && String.valueOf(response.getStatusLine().getStatusCode()).contains(successStatus)) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info(resultString);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return resultString;
    }
}