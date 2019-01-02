/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: Sha1Util.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.util;


import org.hiylo.components.commons.encrypttools.Md5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;


public class Sha1Util {
    public static String getNonceStr() {
        Random random = new Random();
        return Md5.sign(String.valueOf(random.nextInt(10000)), "", "UTF-8");
    }

    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public static String createSHA1Sign(SortedMap<String, String> signParams) throws Exception {
        StringBuffer sb = new StringBuffer();
        Set es = signParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + v + "&");
        }

        String params = sb.substring(0, sb.lastIndexOf("&"));

        return getSha1(params);
    }

    public static String getSha1(String str) {
        if ((str == null) || (str.length() == 0)) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest mdTemp = null;
        try {
            mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                buf[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
