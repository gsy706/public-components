/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: AlipayCore.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.commons.encrypttools;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/* *
 *类名：AlipayFunction
 *功能：支付宝接口公用函数类
 *详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:34
 */
public class AlipayCore {

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new LinkedHashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || "".equals(value) || "sign".equalsIgnoreCase(key)
                    || "sign_type".equalsIgnoreCase(key)) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuffer prestr = new StringBuffer();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                prestr.append(key + "=" + value);
            } else {
                prestr.append(key + "=" + value + "&");
            }
        }

        return prestr.toString();
    }

    /**
     * 把数组所有元素排序，并按照“参数=URLEncoder.encode(参数值,"utf-8")”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串, 值经过encode
     */
    public static String createLinkStringWithValue2Encode(Map<String, String> params)
            throws UnsupportedEncodingException {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuffer prestr = new StringBuffer();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                prestr.append(key + "=" + URLEncoder.encode(value, "utf-8"));
            } else {
                prestr.append(key + "=" + URLEncoder.encode(value, "utf-8") + "&");
            }
        }

        return prestr.toString();
    }

    /**
     * 把数组所有元素排序，并按照“参数=URLDecoder.decode(参数值,"utf-8")”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串, 值经过decode
     */
    public static String createLinkStringWithValue2Decode(Map<String, String> params)
            throws UnsupportedEncodingException {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuffer prestr = new StringBuffer();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                prestr.append(key + "=" + URLDecoder.decode(value, "utf-8"));
            } else {
                prestr.append(key + "=" + URLDecoder.decode(value, "utf-8") + "&");
            }
        }

        return prestr.toString();
    }

    /**
     * 把数组所有元素的值进行URLDecode
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 值经过decode的Map<String,String>
     */
    public static Map<String, String> paraDecode(Map<String, String> params) throws UnsupportedEncodingException {

        List<String> keys = new ArrayList<String>(params.keySet());

        Map<String, String> retMap = new HashMap(0, 0.75F);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            retMap.put(key, URLDecoder.decode(value, "utf-8"));
        }
        return retMap;
    }

}
