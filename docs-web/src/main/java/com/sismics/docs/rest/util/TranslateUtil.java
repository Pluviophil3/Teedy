package com.sismics.docs.rest.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Random;
import java.util.Scanner;

public class TranslateUtil {
    private static final String APP_ID = "20250519002361129";
    private static final String SECURITY_KEY = "lVG2K1s6hwGFDxjDcZdQ";
    private static final String API_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    public static String translateZhToEn(String query) throws Exception {
        if (query == null || query.trim().isEmpty()) return "";

        String from = "zh";
        String to = "en";
        String salt = String.valueOf(new Random().nextInt(10000));
        String sign = md5(APP_ID + query + salt + SECURITY_KEY);

        String params = "q=" + java.net.URLEncoder.encode(query, "UTF-8")
                + "&from=" + from
                + "&to=" + to
                + "&appid=" + APP_ID
                + "&salt=" + salt
                + "&sign=" + sign;

        URL url = new URL(API_URL + "?" + params);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (InputStream is = conn.getInputStream();
             Scanner scanner = new Scanner(is)) {
            String json = scanner.useDelimiter("\\A").next();
            // 简单提取翻译结果（仅演示用途，正式场合请使用 JSON 解析库）
            int start = json.indexOf("\"dst\":\"") + 7;
            int end = json.indexOf("\"", start);
            return json.substring(start, end).replace("\\n", "\n");
        }
    }

    private static String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1) sb.append('0');
            sb.append(s);
        }
        return sb.toString();
    }
}
