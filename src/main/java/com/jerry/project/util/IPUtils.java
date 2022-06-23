package com.jerry.project.util;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtils {

    public static String getIpAddress(String ip) throws Exception {
        String html = doGetAsStr("https://www.ip.cn/ip/"+ip+".html");
        String div = getAddress(html, "<div id=\"tab0_address\">.*</div>");
        return div.split("<div id=\"tab0_address\">")[1].split("</div>")[0];
    }

    private static String doGetAsStr(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new Exception("地址为空");
        }

        // URI m_url = new URI(url);
        // URLDecoder.decode(url,"UTF-8");

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
