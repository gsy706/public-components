/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: ResponseHandle.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.pay;

import com.github.wxpay.sdk.WXPayUtil;
import org.hiylo.components.weixin.config.EmployerWxAppPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 微信支付订单号工具类
 *
 * @author hiylo
 * @date 2018年4月3日 16:14:09
 */
@Component
public final class ResponseHandle {
    private WXPayRequest wxPayRequest;
    @Autowired
    private EmployerWxAppPayConfig wxAppPayConfig;
    @PostConstruct
    public void init() throws Exception {
        wxPayRequest = new WXPayRequest(wxAppPayConfig);
    }

    /**
     * 不需要证书的请求
     *
     * @param urlSuffix        String
     * @param reqData          向wxpay post的请求数据
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs    超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithoutCert(String urlSuffix, Map<String, String> reqData,
                                     int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = WXPayUtil.mapToXml(reqData);

        String resp = this.wxPayRequest.requestWithoutCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, false);
        return resp;
    }


    /**
     * 需要证书的请求
     *
     * @param urlSuffix        String
     * @param reqData          向wxpay post的请求数据  Map
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs    超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithCert(String urlSuffix, Map<String, String> reqData,
                                  int connectTimeoutMs, int readTimeoutMs)  {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = null;
        try {
            reqBody = WXPayUtil.mapToXml(reqData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String resp = this.wxPayRequest.requestWithCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, false);
        return resp;
    }
}
