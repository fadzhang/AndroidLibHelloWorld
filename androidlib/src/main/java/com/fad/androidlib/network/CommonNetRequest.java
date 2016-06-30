package com.fad.androidlib.network;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.fad.androidlib.entity.CommonResult;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import com.fad.androidlib.R;

/**
 * 实现的主要功能:
 * http post get网络请求，封装了线程调用
 *
 * @author zf 创建日期：2016/1/28
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class CommonNetRequest {
    /**
     * 调用方式，post或get
     */
    private String sMethod;

    /**
     * 保存格式化后的参数字符串
     */
    private String sParams;

    /**
     * 服务器接口URL
     */
    private String sUrl;

    /**
     * 网络超时20秒
     */
    private static final int TIMEOUT = 20 * 1000;

    /**
     * 线程池，多个请求并发时互不影响
     */
    private ArrayList<SendTask> taskList;

    /**
     * 网络回调
     */
    private CommonNetRequestListener networkListener = null;

    /**
     * 上下文
     */
    private Context cContext;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public CommonNetRequest(Context context) {
        cContext = context;
        taskList = new ArrayList<SendTask>();
    }

    /**
     * 设置请求的方式、url、参数和网络回调
     *
     * @param method post或get请求方式
     * @param url    服务器url
     * @param params 参数
     * @param lis    网络回调
     * @return
     */
    public boolean setUrlAndParameters(String method, String url, Map<String, String> params, CommonNetRequestListener lis) {
        if (lis != null)
            this.networkListener = lis;

        sMethod = method;
        sUrl = url;
        sParams = "";
        if (params != null) {
            if (method.equals("post")) {
                for (String key : params.keySet()) {
                    System.out.println("key= " + key + " and value= " + params.get(key));
                    sParams += key;
                    sParams += "=";
                    try {
                        sParams += URLEncoder.encode(params.get(key), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return false;
                    }
                    sParams += "&";
                }
                int end = sParams.length() - 1;
                sParams = sParams.substring(0, end);
            } else if (method.equals("get")) {
                for (String key : params.keySet()) {
                    System.out.println("key= " + key + " and value= " + params.get(key));
                    sParams += key;
                    sParams += "=";
                    try {
                        sParams += URLEncoder.encode(params.get(key), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return false;
                    }
                    sParams += "&";
                }
                int end = sParams.length() - 1;
                sParams = sParams.substring(0, end);
            }
        }
        return true;
    }

    /**
     * 设置网络回调
     *
     * @param lis 网络回调
     */
    public void setNetworkListener(CommonNetRequestListener lis) {
        if (lis != null)
            this.networkListener = lis;
    }

    /**
     * 发送请求，添加进线程池，并开启线程进行网络调用
     */
    public void sendRequest() {
        SendTask task = new SendTask();
        taskList.add(task);
        task.execute();
    }

    /**
     * 取消所有的请求
     */
    public void cancelAllTask() {
        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).cancel(true);
        }
    }

    /**
     * 发送网络请求的线程
     */
    private class SendTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... param) {
            String ret = null;
            HttpURLConnection urlConnection = null;
            CommonResult r = null;
            boolean isException = false;
            URL url = null;

            try {
                if (sMethod.equals("get")) {
                    url = TextUtils.isEmpty(sParams) ? new URL(sUrl) : new URL(sUrl + "?" + sParams);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");// 设置请求的方式
                    urlConnection.setReadTimeout(TIMEOUT);// 设置超时的时间
                    urlConnection.setConnectTimeout(TIMEOUT);// 设置链接超时的时间
                } else {
                    url = new URL(sUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");// 设置请求的方式
                    urlConnection.setReadTimeout(TIMEOUT);// 设置超时的时间
                    urlConnection.setConnectTimeout(TIMEOUT);// 设置链接超时的时间
                    urlConnection.setUseCaches(false);
                    //urlConnection.setRequestProperty("Content-Type", ("application/xml; charset=utf-8").replaceAll("\\s", ""));
                    urlConnection.setRequestProperty("Accept-Charset", "utf-8");
                    urlConnection.setRequestProperty("Content-Length", String.valueOf(sParams.getBytes().length));
                    //设置输入和输出流
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);

                    //Send request
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    wr.writeBytes(sParams);
                    wr.flush();
                    wr.close();
                }

                // 获取响应的状态码 404 200 505 302
                if (urlConnection.getResponseCode() == 200) {
                    // 获取响应的输入流对象
                    InputStream is = urlConnection.getInputStream();
                    // 创建字节输出流对象
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    // 定义读取的长度
                    int len = 0;
                    // 定义缓冲区
                    byte buffer[] = new byte[1024];
                    // 按照缓冲区的大小，循环读取
                    while ((len = is.read(buffer)) != -1) {
                        // 根据读取的长度写入到os对象中
                        os.write(buffer, 0, len);
                    }
                    // 释放资源
                    is.close();
                    os.close();
                    // 返回字符串
                    ret = new String(os.toByteArray());
                } else {
                    isException = true;
                    r = new CommonResult();
                    r.setSuccess(false);
                    r.setMsg(cContext.getResources().getString(R.string.error) + urlConnection.getResponseCode());
                }
            } catch (SocketTimeoutException e) { // 超时
                e.printStackTrace();
                isException = true;
                r = new CommonResult();
                r.setSuccess(false);
                r.setMsg(cContext.getResources().getString(R.string.net_error_timeout));
            } catch (IOException e) {
                e.printStackTrace();
                isException = true;
                r = new CommonResult();
                r.setSuccess(false);
                r.setMsg(cContext.getResources().getString(R.string.net_error));
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            if (isException == true) {
                ret = new Gson().toJson(r);
            }

            return ret;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (networkListener != null)
                networkListener.onCommonNetRequestCallback(result);

            taskList.remove(this);
        }
    }
}
