/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: Transfers.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.pay;

import org.hiylo.components.weixin.config.EmployerWxAppPayConfig;
import org.hiylo.components.weixin.config.HelperWxAppPayConfig;
import org.hiylo.components.weixin.util.CommonUtil;
import org.hiylo.components.commons.Base64;
import org.hiylo.components.commons.Encript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * TODO 查账接口  重试逻辑
 * 微信转账到用户余额
 *
 * @author hiylo
 * @date 2018年4月3日 16:14:46
 */
@Component
public class Transfers {
    private final EmployerWxAppPayConfig employerWxAppPayConfig;
    private final HelperWxAppPayConfig helperWxAppPayConfig;
    private final ResponseHandle responseHandle;

    @Autowired
    public Transfers(EmployerWxAppPayConfig employerWxAppPayConfig, HelperWxAppPayConfig helperWxAppPayConfig, ResponseHandle responseHandle) {
        this.employerWxAppPayConfig = employerWxAppPayConfig;
        this.helperWxAppPayConfig = helperWxAppPayConfig;
        this.responseHandle = responseHandle;
    }

    /**
     * 转账到雇主余额
     *
     * @param tradeNo     流水号
     * @param openId      openid
     * @param realname    真实姓名 必须是和微信相同的
     * @param amount      转账金额, 单位分 比如1元 为100
     * @param description 描述
     * @param ip          创建交易的机器的ip
     * @return wechat返回结果
     */
    public String transferToEmployerBalance(String tradeNo, String openId, String realname, String amount, String description,
                                            String ip) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String mchid = employerWxAppPayConfig.getMchID();
        String nonceStr = CommonUtil.getNonceStr();
        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("mch_appid", employerWxAppPayConfig.getAppID());
        packageParams.put("mchid", mchid);
        packageParams.put("nonce_str", nonceStr);
        packageParams.put("partner_trade_no", tradeNo);
        packageParams.put("openid", openId);
        packageParams.put("check_name", "FORCE_CHECK");
        packageParams.put("re_user_name", realname);
        packageParams.put("amount", amount);
        packageParams.put("desc", description);
        packageParams.put("spbill_create_ip", ip);
        String sign = WXPayUtil.generateSignature(packageParams, employerWxAppPayConfig.getKey());
        packageParams.put("sign", sign);
        return responseHandle
                .requestWithCert("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers", packageParams, 1000 * 8, 1000 * 8);
    }

    /**
     * 转账到雇工余额
     *
     * @param tradeNo     流水号
     * @param openId      openid
     * @param realname    真实姓名 必须是和微信相同的
     * @param amount      转账金额, 单位分 比如1元 为100
     * @param description 描述
     * @param ip          创建交易的机器的ip
     * @return wechat返回结果
     */
    public String transferToHelperBalance(String tradeNo, String openId, String realname, String amount, String description,
                                          String ip) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String mchid = helperWxAppPayConfig.getMchID();
        String nonceStr = CommonUtil.getNonceStr();
        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("mch_appid", helperWxAppPayConfig.getAppID());
        packageParams.put("mchid", mchid);
        packageParams.put("nonce_str", nonceStr);
        packageParams.put("partner_trade_no", tradeNo);
        packageParams.put("openid", openId);
        packageParams.put("check_name", "FORCE_CHECK");
        packageParams.put("re_user_name", realname);
        packageParams.put("amount", amount);
        packageParams.put("desc", description);
        packageParams.put("spbill_create_ip", ip);
        String sign = WXPayUtil.generateSignature(packageParams, helperWxAppPayConfig.getKey());
        packageParams.put("sign", sign);
        return responseHandle
                .requestWithCert("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers", packageParams, 1000 * 8, 1000 * 8);
    }

    /**
     * @param tradeNo        流水号
     * @param realname       真实姓名 必须是和微信相同的
     * @param amount         转账金额, 单位分 比如1元 为100
     * @param description    描述
     * @param bankCode       银行编号
     * @param bankCardNumber 银行卡号
     * @param bankNote       银行流水描述
     * @return 微信接口结果
     */
    public String transferToBankCard(String tradeNo, String realname, String amount, String description,
                                     String bankCode, String bankCardNumber, String bankNote) {
        String mchid = employerWxAppPayConfig.getMchID();
        String nonceStr = CommonUtil.getNonceStr();
        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("bank_code", bankCode);
        packageParams.put("bank_note", bankNote);
        packageParams.put("enc_bank_no", Base64.encode(Encript.Rsa.encrypt(bankCardNumber, WXPayConstants.PKCS8RSA, 11, "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING")));
        packageParams.put("enc_true_name", Base64.encode(Encript.Rsa.encrypt(realname, WXPayConstants.PKCS8RSA, 11, "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING")));
        packageParams.put("mch_id", mchid);
        packageParams.put("nonce_str", nonceStr);
        packageParams.put("partner_trade_no", tradeNo);
        packageParams.put("amount", amount);
        packageParams.put("desc", description);
        String sign = null;
        try {
            sign = WXPayUtil.generateSignature(packageParams, employerWxAppPayConfig.getKey());
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        packageParams.put("sign", sign);
        System.out.println(packageParams);
        return responseHandle
                .requestWithCert("https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank", packageParams, 1000 * 8, 1000 * 8);
    }

    public String getPublicKey() throws Exception {
        String mchid = employerWxAppPayConfig.getMchID();
        String nonceStr = CommonUtil.getNonceStr();
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("mch_id", mchid);
        packageParams.put("nonce_str", nonceStr);
        String sign = WXPayUtil.generateSignature(packageParams, employerWxAppPayConfig.getKey());
        packageParams.put("sign", sign);
        String json = responseHandle.requestWithCert("https://fraud.mch.weixin.qq.com/risk/getpublickey", packageParams, 1000 * 8, 1000 * 8);
        System.out.println(json);
        return json;
    }
}
