package com.example.androiddemo.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.androiddemo.app.BaseApplication;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils  {

    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json;charset=" + CHARSET_UTF8;
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    private static final int DEFAULT_READ_TIMEOUT = 5000;

    public interface HttpCallback {
        void onSuccess(String response);

        void onFailure(Exception e);
    }

    /**
     * 发送GET请求
     *
     * @param url      请求地址
     * @param callback 回调函数
     */
    public static void get(final String url, final HttpCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                InputStream is = null;
                BufferedReader reader = null;
                try {
                    conn = (HttpURLConnection) new URL(BaseApplication.BASE_URL + url).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
                    conn.setReadTimeout(DEFAULT_READ_TIMEOUT);
                    conn.connect();
                    Log.e("Http: ", BaseApplication.BASE_URL + url);
                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + conn.getResponseCode());
                    }

                    is = conn.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is, CHARSET_UTF8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    if (callback != null) {
                        final String result = response.toString();
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    int code = jsonObject.getInt("code");
                                    if (code == 200){
                                        callback.onSuccess(jsonObject.getString("data"));
                                    }else {
                                        Toast.makeText(BaseApplication.app, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                } catch (final Exception e) {
                    if (callback != null) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(e);
                            }
                        });
                    }
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 发送POST请求
     *
     * @param url      请求地址
     * @param json     请求参数，以json的String类型传递
     * @param callback 回调函数
     */
    public static void post(final String url, final String json, final HttpCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                OutputStream os = null;
                InputStream is = null;
                BufferedReader reader = null;
                try {
                    conn = (HttpURLConnection) new URL(BaseApplication.BASE_URL + url).openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
                    conn.setReadTimeout(DEFAULT_READ_TIMEOUT);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);
                    conn.connect();
                    Log.e("Http入参: ", json);
                    os = conn.getOutputStream();
                    os.write(json.getBytes(CHARSET_UTF8));
                    os.flush();

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + conn.getResponseCode());
                    }

                    is = conn.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is, CHARSET_UTF8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    if (callback != null) {
                        final String result = response.toString();
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    int code = jsonObject.getInt("code");
                                    if (code == 200){
                                        callback.onSuccess(jsonObject.getString("data"));
                                    }else {
                                        Log.e("Http出参: ", result);
                                        Toast.makeText(BaseApplication.app, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                } catch (final Exception e) {
                    if (callback != null) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BaseApplication.app, "服务器异常", Toast.LENGTH_SHORT).show();
                                callback.onFailure(e);
                            }
                        });
                    }
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 发送put请求
     *
     * @param url      请求地址
     * @param requestBody     请求参数，以json的String类型传递
     * @param listener 回调函数
     */
    public static void put(String url, String requestBody, final HttpCallback listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                InputStream inputStream = null;
                BufferedReader reader = null;

                try {
                    URL requestUrl = new URL(BaseApplication.BASE_URL + url);
                    conn = (HttpURLConnection) requestUrl.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json");

                    if (requestBody != null && !requestBody.isEmpty()) {
                        OutputStream outputStream = conn.getOutputStream();
                        outputStream.write(requestBody.getBytes());
                        outputStream.flush();
                    }

                    if (conn.getResponseCode() != 200) {
                        throw new IOException("Unexpected HTTP response code: " + conn.getResponseCode());
                    }

                    inputStream = conn.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer response = new StringBuffer();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    if (listener != null) {
                        // 将回调函数的执行切换到主线程
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.toString());
                                    int code = jsonObject.getInt("code");
                                    if (code == 200){
                                        listener.onSuccess(jsonObject.getString("data"));
                                    }else {
                                        Toast.makeText(BaseApplication.app, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    if (listener != null) {
                        // 将回调函数的执行切换到主线程
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BaseApplication.app, "服务器异常", Toast.LENGTH_SHORT).show();
                                listener.onFailure(e);
                            }
                        });
                    }
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}