package com.company.project.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

/**
 * Created by rock on 2017/3/19.
 *
 * @author rock
 * @date 2017/03/19
 */
public class HTTPUtil {

    public static String get(String urlStr,Map<String,Object> paramMap) throws Exception{
        try {
            if(paramMap!=null){
                StringBuilder sb=new StringBuilder(urlStr+"?");
                String seq="";
                for (String key : paramMap.keySet()) {
                    sb.append(seq);
                    sb.append(key);
                    sb.append("=");
                    sb.append(paramMap.get(key));

                    seq="&";
                }
                urlStr=sb.toString();
            }
            System.out.println("请求参数"+urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream()),"UTF-8"));
                String output;
                StringBuilder builder = new StringBuilder();
                while ((output = br.readLine()) != null) {
                    builder.append(output);
                }
                return new String(builder.toString().getBytes("UTF-8"), "UTF-8");
            } else {
                StringBuilder sb = new StringBuilder("Post request failed!Server error code is ");
                sb.append(conn.getResponseCode());
                sb.append(";");
                sb.append(conn.getResponseMessage());
                throw new Exception(sb.toString());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String get(String urlStr,Map<String,Object> paramMap, String cookie) throws Exception{
        try {
            if(paramMap!=null){
                StringBuilder sb=new StringBuilder(urlStr+"?");
                String seq="";
                for (String key : paramMap.keySet()) {
                    sb.append(seq);
                    sb.append(key);
                    sb.append("=");
                    sb.append(paramMap.get(key));

                    seq="&";
                }
                urlStr=sb.toString();
            }
            System.out.println("请求参数"+urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Cookie", cookie);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream()),"UTF-8"));
                String output;
                StringBuilder builder = new StringBuilder();
                while ((output = br.readLine()) != null) {
                    builder.append(output);
                }
                return new String(builder.toString().getBytes("UTF-8"), "UTF-8");
            } else {
                StringBuilder sb = new StringBuilder("Post request failed!Server error code is ");
                sb.append(conn.getResponseCode());
                sb.append(";");
                sb.append(conn.getResponseMessage());
                throw new Exception(sb.toString());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public static String post(String urlStr,Map<String,String> paramMap, String cookie) throws Exception{
        try {
            System.out.println("请求参数"+urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestProperty("","");
            if(paramMap!=null){
                for (String key : paramMap.keySet()) {
                    conn.setRequestProperty(key, paramMap.get(key)+"");
                }
            }
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream()),"UTF-8"));
                String output;
                StringBuilder builder = new StringBuilder();
                while ((output = br.readLine()) != null) {
                    builder.append(output);
                }
                return new String(builder.toString().getBytes("UTF-8"), "UTF-8");
            } else {
                StringBuilder sb = new StringBuilder("Post request failed!Server error code is ");
                sb.append(conn.getResponseCode());
                sb.append(";");
                sb.append(conn.getResponseMessage());
                throw new Exception(sb.toString());
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String post(String uri, String requstPayLoad, String cookie, String contentType){
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        PostMethod post = new PostMethod(uri);
        post.setRequestHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        post.setRequestHeader("User-Agent"," Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        post.setRequestHeader("Accept-Language","zh-cn,zh;q=0.8");
        post.setRequestHeader("Accept-Encoding","gzip, deflate, sdch");
        post.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");///application/json
        post.setRequestHeader("Connection","keep-alive");
        if(cookie !=null && cookie.length() >0){
            post.setRequestHeader("Cookie",cookie);
        }
        if(contentType !=null && contentType.length() >0){
            post.setRequestHeader("Content-Type",contentType);
        }

        String html = "";
        try {
            RequestEntity entity = new StringRequestEntity(requstPayLoad, "text/html", "utf-8");
            post.setRequestEntity(entity);
            httpClient.executeMethod(post);
            html = post.getResponseBodyAsString();
            System.out.println(html);
        } catch (IOException e) {
            html = "";
            e.printStackTrace();
        }
        return html;
    }

    public static void main(String[] args) throws Exception {
        String str = "{\"deliveryCode\":\"1121121\",\"weight\":\"12\"}";
        String cookie = "cna=7wuEEyTdUw8CAWoLIhPfMgPK; JSESSIONID=FF6YZ3XUQ2-OW6VAJ3ETKGBLNKT7VTQ1-JEEQ5AHJ-F; "
            + "_temp0=4ONIQWzt9LAi6GJUMOIKXYGbJ%2FhEZvYwclzrj%2FYELVeghKatCSbh4Mpal%2FJ71WQeR2crgwuRz"
            + "%2B5WklJl3tMGBKBYv2vg2iu8j7Fw6e73ZBvcTKb8JVn1EOx7ZDq%2FFnw2OcEUaXGu0lWRdr%2BSVZO9Nw%3D%3D; "
            + "t=5f8d20a9414067aeb790622d2c484306; _tb_token_=f4085780b3fbd; "
            + "cookie2=19471e052f55cf0cefe2d712622938b3; account=Y2FpbmlhbzI1NzIz; "
            + "accountId=\"NDM5ODA1MDE0MzEzMQ==\"; cpcode=cainiao; isLogin=true; lvt=1526577723872; "
            + "cncc=9805c4db21d0e3c65006cb6a4a8ac7d7; "
            +
            "TwAhx8HL=D5F51846024F2DBC1BFCF8A9156B38C3035FD0FC3F4B73244B10B36488CCC70D71DD6F0247CF377A047EE481AEADA5C0562946D4723B4658EEE27A50EE5BB39D081CC56F7700F3CE8DC74A4B79226B4C90C24C31545E874C9871FE5CFA7E775CB51B9728C76402205062BB977E31EAF52C02FE3154AEC803BE71C94328B703DFE3B16B863CEEEBF7D2EFBE88EAFE1463037F2E1EEEC9DC6B17626943D107FF5E; u9sJLjkn=9517c0cddd90bc8456529eb06e1135b7; isg=BCkpBZ90V6VNzmsRGn3I5CNYONWJ9RzugfIA2sseAJBPkkykGUTV-RbEUTakSbVg\n";

        String contenttype = "application/json";
//        String html = HTTPUtil.post("http://11.163.213.13/instock/save",str, cookie, contenttype);//
//        System.out.println(html);
        //JSONObject jsonObject = JSON.parseObject(httpPostResult);
        //if(jsonObject == null || jsonObject.getBoolean("successful") == false){
        //    System.out.println("jsonObject is null or succss= false"); ;
        //    return;
        //}
        //JSONObject contentJsonObj = (JSONObject)jsonObject.get("object");
        //com.alibaba.fastjson.JSONArray jSONArray= contentJsonObj.getJSONObject("mixFlowInst").getJSONArray("crList") ;
        //if (jSONArray == null || jSONArray.size() == 0){
        //    return;
        //}
        //List<String> list = new ArrayList<>();
        //for(Object object: jSONArray){
        //    int crid = ((JSONObject)object).getInteger("id");
        //    System.out.println(crid);
        //    list.add(crid+"");
        //}
        //System.out.println(String.join(",", list));
    }
}
