package com.hq.ecmp.util;

import com.alibaba.fastjson.JSON;
import com.hq.ecmp.constant.UrlConstant;
import com.hq.ecmp.interceptor.LoggingInterceptor;
import com.hq.ecmp.interceptor.PrintingEventListener;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * okhttp请求类
 *
 * @author gogym
 * @version 2018年8月2日
 * @see OkHttpUtil
 * @since
 */
@Slf4j
public class OkHttpUtil {

    public final static String GET = "GET";

    public final static String POST = "POST";

    public final static String PUT = "PUT";

    public final static String DELETE = "DELETE";

    public final static String PATCH = "PATCH";

    private final static String UTF8 = "UTF-8";

    private final static String GBK = "GBK";

    private final static String DEFAULT_CHARSET = UTF8;

    private final static String DEFAULT_METHOD = GET;

    private final static String DEFAULT_MEDIA_TYPE = "application/json";

    private final static boolean DEFAULT_LOG = false;


    private final static OkHttpClient client = new OkHttpClient.Builder().
            eventListenerFactory(PrintingEventListener.FACTORY).
            addInterceptor(new LoggingInterceptor()).
            connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES)).
            readTimeout(3, TimeUnit.SECONDS).
            connectTimeout(3, TimeUnit.SECONDS).
            build();


    /**
     * GET请求
     *
     * @param url URL地址
     * @return
     */
    public static String get(String url)
            throws Exception {
        return execute(OkHttp.builder().url(url).build());
    }

    /**
     * GET请求
     *
     * @param url URL地址
     * @return
     */
    public static String get(String url, String charset)
            throws Exception {
        return execute(OkHttp.builder().url(url).responseCharset(charset).build());
    }

    /**
     * 带查询参数的GET查询
     *
     * @param url      URL地址
     * @param queryMap 查询参数
     * @return
     */
    public static String get(String url, Map<String, String> queryMap)
            throws Exception {
        return execute(OkHttp.builder().url(url).queryMap(queryMap).build());
    }

    /**
     * 带查询参数的GET查询
     *
     * @param url      URL地址
     * @param queryMap 查询参数
     * @return
     */
    public static String getMicro(String url, Map<String, String> queryMap, String token)
            throws Exception {
        HashMap headers = null;
        if (! StringUtils.isEmpty(token)) {
            headers = new HashMap();
            headers.put("Authorization", token);
        }
        return execute(OkHttp.builder().url(UrlConstant.getCloudUrl() + url).headerMap(headers).queryMap(queryMap).build());
    }

    /**
     * 带查询参数的GET查询
     *
     * @param url      URL地址
     * @param queryMap 查询参数
     * @return
     */
    public static String get(String url, Map<String, String> queryMap, String charset)
            throws Exception {
        return execute(OkHttp.builder().url(url).queryMap(queryMap).responseCharset(charset).build());
    }

    /**
     * POST application/json
     *
     * @param url
     * @param obj
     * @return
     */
    public static String postJson(String url, Object obj)
            throws Exception {
        return execute(OkHttp.builder().url(url).method(POST).data(JSON.toJSONString(obj)).mediaType(
                "application/json").build());
    }

    /**
     * POST application/json
     *
     * @param url
     * @param obj
     * @param token
     * @return
     */
    public static String postJson(String url, Object obj, String token)
            throws Exception {
        OkHttp.OkHttpBuilder builder = OkHttp.builder();
        Map m = new HashMap();
        m.put("Authorization", "Bearer " + token);
        builder.headerMap(m);
        String param = null;
        if (obj instanceof String) {
            param = (String) obj;
        } else {
            param = JSON.toJSONString(obj);
        }
        return execute(builder.url(UrlConstant.getCloudUrl() + url).method(POST).data(param).mediaType(
                "application/json").build());
    }

    /**
     * POST application/x-www-form-urlencoded
     *
     * @param url
     * @param formMap
     * @return
     */
    public static String postForm(String url, Map<String, Object> formMap,String token)
            throws Exception {

        String data = "";
        Map m = new HashMap();
        m.put("Authorization", "Bearer " + token);
        if (MapUtils.isNotEmpty(formMap)) {
            data = formMap.entrySet().stream().map(
                    entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(
                    Collectors.joining("&"));
        }
        return execute(OkHttp.builder().url(UrlConstant.getCloudUrl()+url).method(POST).data(data).mediaType(
                "application/x-www-form-urlencoded").headerMap(m).build());
    }
    /**
     * POST application/x-www-form-urlencoded
     *
     * @param url
     * @param formMap
     * @return
     */
    public static String postForm(String url, Map<String, Object> formMap)
            throws Exception {

        String data = "";
        if (MapUtils.isNotEmpty(formMap)) {
            data = formMap.entrySet().stream().map(
                    entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(
                    Collectors.joining("&"));
        }
        return execute(OkHttp.builder().url(url).method(POST).data(data).mediaType(
                "application/x-www-form-urlencoded").build());
    }
    /**
     * 微服务调用接口
     *
     * @param url
     * @param formMap
     * @return
     */
    public static String postMicro(String url, Map<String, Object> formMap, String token)
            throws Exception {

        String data = "";
        if (MapUtils.isNotEmpty(formMap)) {
            data = formMap.entrySet().stream().map(
                    entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(
                    Collectors.joining("&"));
        }
        HashMap headers = null;
        if (! StringUtils.isEmpty(token)) {
            headers = new HashMap();
            headers.put("Authorization", token);
        }
        return execute(OkHttp.builder().url(UrlConstant.getCloudUrl() + url).method(POST).data(data).headerMap(headers).mediaType(
                "application/x-www-form-urlencoded").build());
    }

    /**
     * 微服务调用接口
     *
     * @param url
     * @param formMap
     * @return
     */
    private static final MediaType FROM_DATA = MediaType.parse("multipart/form-data");

    public static String postFileMicro(String url, MultipartFile[] files, Map<String, Object> formMap, String token)
            throws Exception {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(FROM_DATA);
        for(int i= 0;i<files.length; i++){
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),multipartFileToFile(files[i]));
            builder.addFormDataPart("files",files[i].getOriginalFilename(),fileBody);
        }
        for (Map.Entry<String, Object> stringObjectEntry : formMap.entrySet()) {
            builder.addFormDataPart(stringObjectEntry.getKey(),stringObjectEntry.getValue()==null?"":stringObjectEntry.getValue().toString());
        }

        Request request = new Request.Builder()
                .post(builder.build())
                .header("Authorization",token)
                .url(UrlConstant.getCloudUrl()+url)
                .build();

        return client.newCall(request).execute().body().string();
    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }
    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }
    /**
     * 为服务调用接口
     *
     * @param url
     * @param formMap
     * @return
     */
    public static String postMicro(String url, Map<String, Object> formMap)
            throws Exception {
        return postMicro(url, formMap, null);
    }

    private static String post(String url, String data, String mediaType, String charset)
            throws Exception {
        return execute(OkHttp.builder().url(url).method(POST).data(data).mediaType(mediaType).responseCharset(
                charset).build());
    }

    /**
     * 通用执行方法
     */
    private static String execute(OkHttp okHttp)
            throws Exception {
        if (StringUtils.isEmpty(okHttp.requestCharset)) {
            okHttp.requestCharset = DEFAULT_CHARSET;
        }
        if (StringUtils.isEmpty(okHttp.responseCharset)) {
            okHttp.responseCharset = DEFAULT_CHARSET;
        }
        if (StringUtils.isEmpty(okHttp.method)) {
            okHttp.method = DEFAULT_METHOD;
        }
        if (StringUtils.isEmpty(okHttp.mediaType)) {
            okHttp.mediaType = DEFAULT_MEDIA_TYPE;
        }
        if (okHttp.requestLog) {// 记录请求日志
            log.info(okHttp.toString());
        }

        // 获取请求URL
        String url = okHttp.url;
        // 创建请求
        Request.Builder builder = new Request.Builder();

        if (MapUtils.isNotEmpty(okHttp.queryMap)) {
            String queryParams = okHttp.queryMap.entrySet().stream().map(
                    entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(
                    Collectors.joining("&"));
            url = String.format("%s%s%s", url, url.contains("?") ? "&" : "?", queryParams);
        }
        builder.url(url);

        // 设置请求头
        if (MapUtils.isNotEmpty(okHttp.headerMap)) {
            okHttp.headerMap.forEach(builder::addHeader);
        }

        // 设置请求类型
        String method = okHttp.method.toUpperCase();
        String mediaType = String.format("%s;charset=%s", okHttp.mediaType, okHttp.requestCharset);

        if (method.equals(GET)) {
            builder.get();
        } else if (ArrayUtils.contains(new String[]{POST, PUT, DELETE, PATCH}, method)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType), okHttp.data);

            builder.method(method, requestBody);
        } else {
            throw new Exception("未设置请求method");
        }
        // 返回值
        String result = "";
        Response response = client.newCall(builder.build()).execute();
//            byte[] bytes = response.body().bytes();
//            result = new String(bytes, okHttp.responseCharset);
//            response.body().source().readByteString();
        result = response.body().string();
        // 记录返回日志
        log.info(result);

        return result;
    }

    /**
     * 一个内部类
     *
     * @author gogym
     * @version 2018年7月30日
     * @see OkHttp
     * @since
     */

    @Builder
    @ToString(exclude = {"requestCharset", "responseCharset", "requestLog", "responseLog"})
    static class OkHttp {
        private String url;

        private String method = DEFAULT_METHOD;

        private String data;

        private String mediaType = DEFAULT_MEDIA_TYPE;

        private Map<String, String> queryMap;

        private Map<String, String> headerMap;

        private String requestCharset = DEFAULT_CHARSET;

        private boolean requestLog = DEFAULT_LOG;

        private String responseCharset = DEFAULT_CHARSET;

        private boolean responseLog = DEFAULT_LOG;
    }
}

