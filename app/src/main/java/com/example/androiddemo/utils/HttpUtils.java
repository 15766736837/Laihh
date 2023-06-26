package com.example.androiddemo.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils {
    public interface Callback {
        void onSuccess(String response);
        void onError(Exception e);
    }

    public static void request(final String url, final String method, final Map<String, String> params, final Callback callback) {
        new Thread(() -> {
            try {
                String response = executeRequest(url, method, params);
                runOnUIThread(() -> callback.onSuccess(response));
            } catch (final Exception e) {
                runOnUIThread(() -> callback.onError(e));
            }
        }).start();
    }

    private static String executeRequest(String url, String method, Map<String, String> params) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (params != null && method.equals("POST")) {
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                postData.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
            }
            postData.deleteCharAt(postData.length() - 1);
            outputStream.write(postData.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            inputStream.close();
            return response.toString();
        } else {
            throw new IOException("HTTP request failed, response code: " + responseCode);
        }
    }

    private static void runOnUIThread(Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}