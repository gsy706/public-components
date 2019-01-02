/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: HelperTopay.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.pay;

import com.github.wxpay.sdk.WXPay;
import org.hiylo.components.exceptions.PaymentException;
import org.hiylo.components.weixin.config.AbstractWXPayConfig;
import org.hiylo.components.weixin.config.HelperWxAppPayConfig;
import org.hiylo.components.weixin.util.CommonUtil;
import org.hiylo.components.weixin.util.Sha1Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * TODO 支付 ClassName: TopayServlet
 *
 * @author hiylo
 * @Description:
 * @date Jan 13, 2017 3:20:15 PM
 */
@Component
public class HelperTopay {
    @Autowired
    private HelperWxAppPayConfig wxAppPayConfig;
    private WXPay wxPay;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @PostConstruct
    public void init() {
        wxPay = new WXPay(wxAppPayConfig);
    }

    public SortedMap getPackage(String orderItemName, String orderNo, double price, String notify_url, String tradeType, String ip, String openId, String attach) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String totalFee = String.valueOf((int) price * 100);
        String mch_id = wxAppPayConfig.getMchID();
        String nonce_str = CommonUtil.getNonceStr();
        String out_trade_no = orderNo;
        String spbill_create_ip = ip;
        String trade_type = tradeType;
        String openid = openId;
        SortedMap packageParams = new TreeMap();
        packageParams.put("appid", wxAppPayConfig.getAppID());
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", orderItemName);
        packageParams.put("attach", attach);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", totalFee);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);
        packageParams.put("openid", openid);
        logger.info(packageParams.toString());
        String prepay_id = "";
        try {
            Map<String, String> map = wxPay.unifiedOrder(packageParams);
            System.out.println(map);
            if (!"SUCCESS".equals(map.get("return_code"))) {
                throw new PaymentException(AbstractWXPayConfig.EXCEPTION_CODE_PAYMENT_REQUEST_THIRD_PART_FAIL, "请求微信支付异常");
            }
            // String return_code = (String) map.get("return_code");
            prepay_id = (String) map.get("prepay_id");
            if ("".equals(prepay_id)) {
                throw new PaymentException(AbstractWXPayConfig.EXCEPTION_CODE_PAYMENT_REQUEST_THIRD_PART_FAIL, "请求微信支付异常");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        SortedMap finalpackage = new TreeMap();
        String appid2 = wxAppPayConfig.getAppID();
        String timestamp = Sha1Util.getTimeStamp();
        String nonceStr2 = nonce_str;
        String prepay_id2 = "prepay_id=" + prepay_id;
        String packages = prepay_id2;
        finalpackage.put("appId", appid2);
        finalpackage.put("timeStamp", timestamp);
        finalpackage.put("nonceStr", nonceStr2);
        finalpackage.put("package", packages);
        finalpackage.put("signType", "MD5");

        String finalsign = WXPayUtil.generateSignature(finalpackage, wxAppPayConfig.getKey());
        finalpackage.put("paySign", finalsign);
        logger.info(finalpackage.toString());
        return finalpackage;
    }
}
