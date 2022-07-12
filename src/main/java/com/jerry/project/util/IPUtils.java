package com.jerry.project.util;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtils {

    public static String getCommentIp(String ip) throws Exception {
        String html = doGetAsStr("https://www.ipshudi.com/"+ip+"/");
        String div = getAddress(html, "<td>\n<span>.*</span>\n");
        String address = div == null ? "未知" : div.split("<span>")[1].split("</span>")[0];
        String res = address.replace("中国 ","");
        String[] ads = res.split(" ");
        if(ads.length > 2){
            return ads[0] + " " + ads[1];
        }else{
            return res;
        }
    }

    public static String getIpAddress(String ip) throws Exception {
        String html = doGetAsStr("https://www.ipshudi.com/"+ip+"/");
        String div = getAddress(html, "<td>\n<span>.*</span>\n");
        String div1 = getAddress(html, "<td class=\"th\">运营商</td><td><span>.*</span>");
        return (div == null ? "未知归属地" : div.split("<span>")[1].split("</span>")[0]) + " "
                + (div1 == null ? "" : div1.split("<span>")[1].split("</span>")[0]);
    }

    private static String doGetAsStr(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new Exception("地址为空");
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String text = EntityUtils.toString(httpEntity);
            return text;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static String getAddress(String s, String pattern) {
        Pattern patt = Pattern.compile(pattern);
        Matcher matcher = patt.matcher(s);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }
}
