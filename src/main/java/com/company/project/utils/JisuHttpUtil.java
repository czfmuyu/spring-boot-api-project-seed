package com.company.project.utils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class JisuHttpUtil {
    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36";
    // HTTP GET request
    public static String sendGet(String url, String charset) throws Exception {
        URL realurl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) realurl.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        // add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
//        con.setRequestProperty("Accept-Encoding", "gzip, deflate");
//        con.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
//        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

        // int responseCode = con.getResponseCode();
        // System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
        String inputLine;
        StringBuffer result = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
            result.append("\r\n");
        }
        in.close();
        con.disconnect();
        return result.toString();
    }

    // HTTP POST request
    @SuppressWarnings("deprecation")
    public static String sendPost(String url, Map<String, String> param, String charset) throws Exception {
        URL realurl = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) realurl.openConnection();

        con.setRequestMethod("POST");
        // add reuqest header
        con.setRequestProperty("User-Agent", USER_AGENT);
        // con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // String urlParameters =
        // "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
        StringBuffer buffer = new StringBuffer();
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), charset))
                        .append("&");
            }
        }
        buffer.deleteCharAt(buffer.length() - 1);
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(buffer.toString());
        wr.flush();
        wr.close();
        // int responseCode = con.getResponseCode();
        // System.out.println("Post parameters : " + urlParameters);
        // System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
        String inputLine;
        StringBuffer result = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
            result.append("\r\n");
        }
        in.close();
        con.disconnect();
        return result.toString();
    }
}