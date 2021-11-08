package cn.weihu.kol.util;

import cn.weihu.kol.push.PushRetryBo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpFun {

    public static final Logger log = LoggerFactory.getLogger(HttpFun.class);

    public static String postAsyncSaveLog(PushRetryBo pushRetryBo) {
        String result;
        try {
            log.info("uuid[" + pushRetryBo.getUuid() + "]post  url:" + pushRetryBo.getUrl() + " params:" + pushRetryBo.getParams());
            result = HttpFun.post(pushRetryBo.getUuid(), pushRetryBo.getUrl(), pushRetryBo.getParams());
        } catch(Exception e) {
            log.error("uuid[" + pushRetryBo.getUuid() + "]请求失败:{}", e.getMessage());
            result = "senderror:" + e.getMessage();
        }
        return result;
    }

    /**
     * 发送HttpPost请求
     *
     * @param url    服务地址
     * @param params json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     * @return 成功:返回json字符串<br/>
     */
    public static String post(String uuid, String url, String params) {
        long startTime = System.currentTimeMillis();
        try {
            // 创建连接
            URL               realUrl = new URL(url);
            HttpURLConnection conn    = (HttpURLConnection) realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // 设置请求方式
            conn.setRequestMethod("POST");
            // 设置接收数据的格式
            conn.setRequestProperty("Accept", "application/json");
            // 设置发送数据的格式
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.connect();
            // utf-8编码
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            InputStream    is     = conn.getInputStream();
            StringBuilder  sb     = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String         line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String result  = sb.toString();
            long   endTime = System.currentTimeMillis();
            log.info("uuid[" + uuid + "]回执结果:" + result + ",处理时间:{}", (endTime - startTime));
            return result;
        } catch(Exception e) {
            long endTime = System.currentTimeMillis();
            log.info("uuid[" + uuid + "]处理时间:" + (endTime - startTime) + ",请求异常:" + e.getMessage());
            return e.getMessage();
        }
    }

    public static String httpPost(String uuid, String url, String params) {
        long   startTime = System.currentTimeMillis();
        String result;
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost            httpPost   = new HttpPost(url);
            //设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)    // 设置连接超时时间，单位毫秒
                    .setConnectionRequestTimeout(1000)  // 设置从connect Manager获取Connection 超时时间，单位毫秒
                    .setSocketTimeout(5000)     // 请求获取数据的超时时间，单位毫秒
                    .build();
            httpPost.setConfig(requestConfig);
            // 设置请求参数
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setHeader("Accept", "application/json");
            StringEntity stringEntity = new StringEntity(params, "utf-8");
            httpPost.setEntity(stringEntity);
            HttpResponse response   = httpClient.execute(httpPost);
            int          statusCode = response.getStatusLine().getStatusCode();
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            if(statusCode != HttpStatus.SC_OK) {
                result = "error:" + result;
            }
            long endTime = System.currentTimeMillis();
            log.info("uuid[" + uuid + "]回执结果:" + result + ",处理时间:{}", (endTime - startTime));
        } catch(Exception e) {
            long endTime = System.currentTimeMillis();
            log.info("uuid[" + uuid + "]处理时间:" + (endTime - startTime) + ",请求异常:" + e.getMessage());
            result = "error:" + e.getMessage();
        }
        return result;
    }

    public static String upload(String url, String filename) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        String res = "error";
        try {
            HttpPost      httppost      = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2000).setSocketTimeout(2000).build();
            httppost.setConfig(requestConfig);
            FileBody   bin       = new FileBody(new File(filename));
            StringBody comment   = new StringBody("This is comment", ContentType.TEXT_PLAIN);
            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", bin).addPart("comment", comment).build();
            httppost.setEntity(reqEntity);
            httppost.setHeader("filename", filename);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null) {
                    res = EntityUtils.toString(response.getEntity());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch(ClientProtocolException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        log.debug("推送录音文件,url:{},filename:{},res:{}", url, filename, res);
        return res;
    }

    public static String httpGet(String uuid, String url) {
        long   startTime = System.currentTimeMillis();
        String result;
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet             httpGet    = new HttpGet(url);
            //设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)    // 设置连接超时时间，单位毫秒
                    .setConnectionRequestTimeout(1000)  // 设置从connect Manager获取Connection 超时时间，单位毫秒
                    .setSocketTimeout(5000)     // 请求获取数据的超时时间，单位毫秒
                    .build();
            httpGet.setConfig(requestConfig);
            HttpResponse response   = httpClient.execute(httpGet);
            int          statusCode = response.getStatusLine().getStatusCode();
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
            if(statusCode != HttpStatus.SC_OK) {
                result = "error:" + result;
            }
            long endTime = System.currentTimeMillis();
            log.info("uuid[" + uuid + "]回执结果:" + result + ",处理时间:{}", (endTime - startTime));
        } catch(Exception e) {
            long endTime = System.currentTimeMillis();
            log.info("uuid[" + uuid + "]处理时间:" + (endTime - startTime) + ",请求异常:" + e.getMessage());
            result = "error:" + e.getMessage();
        }
        return result;
    }
    public static String postfrom(String uuid, String url, String params) {
        long starttime = System.currentTimeMillis();
        try {
//            log.info("uuid[" + uuid + "]post size:"+linkedBlockingQueue.size()+" url:" + url + " params:" + params);
            URL raelUrl = new URL(url);// 创建连接
            HttpURLConnection conn = (HttpURLConnection) raelUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            conn.setRequestProperty("Accept", "*/*"); // 设置接收数据的格式
//            conn.setRequestProperty("Content-Type", "multipart/form-data"); // 设置发送数据的格式
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.connect();
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            InputStream is = conn.getInputStream();
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String result = sb.toString();
            long endtime = System.currentTimeMillis();
            log.info("uuid[" + uuid + "]回执结果:" + result+",处理时间:{}",(endtime-starttime));
            return result;
        } catch (IOException e) {
            long endtime = System.currentTimeMillis();
            log.info("uuid[" + uuid + "]处理时间:"+(endtime-starttime)+",请求异常:"+e.getMessage());
            return "senderror:"+e.getMessage();
        }
    }
}
