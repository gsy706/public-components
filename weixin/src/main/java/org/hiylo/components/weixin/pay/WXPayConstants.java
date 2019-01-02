/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: WXPayConstants.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.pay;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量
 */
public class WXPayConstants {
    public static final String PKCS8RSA = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3Y+Zk2Ri0NkxAcaN93Ic" +
            "sWUteJM3mTpkVq/thUZhB68CC40ue3a+JqwhHy9H4TKVMsGBTbLpko4xev7s4fxC" +
            "zzlRqyC1WiLEW22NY7DWZl4otPmvm005DJtfM5Agrzz/DoFxGHb4iuloYnXZ3TsE" +
            "B9MfWNZY2NeI4iNlBy34KMiHBUVxehlVW5FCFamYFcClP5HqaZ4+IkhSH7+Y8CP7" +
            "I2er490GboR70V3Und7PMUKHGpev/B9bRxOLNUywv0bLxEom0MCtBjUP71/8Uz3y" +
            "iP06T8GAJ8rEUNLQyVSzJr8nKiOh2SUY7fPWWERalj4+u7ko8mfMIQdrGCwMNt38" +
            "tQIDAQAB";

    public static final String PKCS1RSA = "MIIBCgKCAQEA3Y+Zk2Ri0NkxAcaN93IcsWUteJM3mTpkVq/thUZhB68CC40ue3a+" +
            "JqwhHy9H4TKVMsGBTbLpko4xev7s4fxCzzlRqyC1WiLEW22NY7DWZl4otPmvm005" +
            "DJtfM5Agrzz/DoFxGHb4iuloYnXZ3TsEB9MfWNZY2NeI4iNlBy34KMiHBUVxehlV" +
            "W5FCFamYFcClP5HqaZ4+IkhSH7+Y8CP7I2er490GboR70V3Und7PMUKHGpev/B9b" +
            "RxOLNUywv0bLxEom0MCtBjUP71/8Uz3yiP06T8GAJ8rEUNLQyVSzJr8nKiOh2SUY" +
            "7fPWWERalj4+u7ko8mfMIQdrGCwMNt38tQIDAQAB";

    public enum SignType {
        MD5, HMACSHA256
    }

    public static final String DOMAIN_API = "api.mch.weixin.qq.com";
    public static final String DOMAIN_API2 = "api2.mch.weixin.qq.com";
    public static final String DOMAIN_APIHK = "apihk.mch.weixin.qq.com";
    public static final String DOMAIN_APIUS = "apius.mch.weixin.qq.com";


    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    public static final String MICROPAY_URL_SUFFIX = "/pay/micropay";
    public static final String UNIFIEDORDER_URL_SUFFIX = "/pay/unifiedorder";
    public static final String ORDERQUERY_URL_SUFFIX = "/pay/orderquery";
    public static final String REVERSE_URL_SUFFIX = "/secapi/pay/reverse";
    public static final String CLOSEORDER_URL_SUFFIX = "/pay/closeorder";
    public static final String REFUND_URL_SUFFIX = "/secapi/pay/refund";
    public static final String REFUNDQUERY_URL_SUFFIX = "/pay/refundquery";
    public static final String DOWNLOADBILL_URL_SUFFIX = "/pay/downloadbill";
    public static final String REPORT_URL_SUFFIX = "/payitil/report";
    public static final String SHORTURL_URL_SUFFIX = "/tools/shorturl";
    public static final String AUTHCODETOOPENID_URL_SUFFIX = "/tools/authcodetoopenid";

    // sandbox
    public static final String SANDBOX_MICROPAY_URL_SUFFIX = "/sandboxnew/pay/micropay";
    public static final String SANDBOX_UNIFIEDORDER_URL_SUFFIX = "/sandboxnew/pay/unifiedorder";
    public static final String SANDBOX_ORDERQUERY_URL_SUFFIX = "/sandboxnew/pay/orderquery";
    public static final String SANDBOX_REVERSE_URL_SUFFIX = "/sandboxnew/secapi/pay/reverse";
    public static final String SANDBOX_CLOSEORDER_URL_SUFFIX = "/sandboxnew/pay/closeorder";
    public static final String SANDBOX_REFUND_URL_SUFFIX = "/sandboxnew/secapi/pay/refund";
    public static final String SANDBOX_REFUNDQUERY_URL_SUFFIX = "/sandboxnew/pay/refundquery";
    public static final String SANDBOX_DOWNLOADBILL_URL_SUFFIX = "/sandboxnew/pay/downloadbill";
    public static final String SANDBOX_REPORT_URL_SUFFIX = "/sandboxnew/payitil/report";
    public static final String SANDBOX_SHORTURL_URL_SUFFIX = "/sandboxnew/tools/shorturl";
    public static final String SANDBOX_AUTHCODETOOPENID_URL_SUFFIX = "/sandboxnew/tools/authcodetoopenid";

    public static final Map<String, String> BANK = new HashMap(){
        {
            put("1002", "工商银行");
            put("1005", "农业银行");
            put("1026", "中国银行");
            put("1003", "建设银行");
            put("1001", "招商银行");
            put("1066", "邮储银行");
            put("1020", "交通银行");
            put("1004", "浦发银行");
            put("1006", "民生银行");
            put("1009", "兴业银行");
            put("1010", "平安银行");
            put("1021", "中信银行");
            put("1025", "华夏银行");
            put("1027", "广发银行");
            put("1022", "光大银行");
            put("1032", "北京银行");
            put("1056", "宁波银行");
        }
    };
}

