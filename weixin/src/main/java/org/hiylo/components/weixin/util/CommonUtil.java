/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: CommonUtil.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.util;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.hiylo.components.commons.utils.MyX509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;

/**
 * @author hiylo
 * @date 2017年11月27日 11:31:17
 */
@Controller
public class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());

            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            conn.setRequestMethod(requestMethod);

            if (outputStr != null) {
                OutputStream outputStream = conn.getOutputStream();

                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            logger.error("连接超时：{}", ce);
        } catch (Exception e) {
            logger.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getFileExt(String contentType) {
        String fileExt = "";
        if ("image/jpeg".equals(contentType)) {
            fileExt = ".jpg";
        } else if ("audio/mpeg".equals(contentType)) {
            fileExt = ".mp3";
        } else if ("audio/amr".equals(contentType)) {
            fileExt = ".amr";
        } else if ("video/mp4".equals(contentType)) {
            fileExt = ".mp4";
        } else if ("video/mpeg4".equals(contentType)) {
            fileExt = ".mp4";
        }
        return fileExt;
    }

    public static String getNonceStr() {
        String currTime = TenpayUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = String.valueOf(TenpayUtil.buildRandom(4));
        String strReq = strTime + strRandom;
        return strReq;
    }
}
