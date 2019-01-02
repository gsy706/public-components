/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components_jhb
 * File: EmployerTopay.java
 * Data: 5/8/18 2:45 PM
 * Author: hiylo
 */

package org.hiylo.components.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class EmployerAliTopay {
    @Autowired
    private EmployerAppInfo appInfo;

    public String prepare(String body, String subject, String outTradeNo, String timeoutExpress, String totleAmount) throws AlipayApiException {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appInfo.getAppId(), appInfo.getPrivateKey(), "json", "utf-8", appInfo.getPublicKey(), "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setOutTradeNo(outTradeNo);
        model.setTimeoutExpress(timeoutExpress);
        model.setTotalAmount(totleAmount);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(appInfo.getNotifyUrl());
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public AlipayNotifyResponse aliPayNotify(Map requestParams) throws UnsupportedEncodingException, AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>(requestParams.size());

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        //验证签名
        boolean flag = AlipaySignature.rsaCheckV1(params, appInfo.getAliPublicKey(), "UTF-8", "RSA2");
        if (flag) {
            if ("TRADE_SUCCESS".equals(params.get("trade_status"))) {
                return new AlipayNotifyResponse(params.get("buyer_pay_amount"), params.get("out_trade_no"), params.get("trade_no"), URLDecoder.decode(params.get("passback_params"), "UTF-8"));
            }
        }
        return null;
    }
}
