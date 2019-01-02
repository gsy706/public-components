/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components_jhb
 * File: HelperAppInfo.java
 * Data: 5/8/18 2:45 PM
 * Author: hiylo
 */

package org.hiylo.components.alipay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class HelperAppInfo {
    @Value("${alipay.helper.appId}")
    private String appId;
    private String charset = "UTF-8";
    //私钥
    @Value("${alipay.helper.privateKey}")
    private String privateKey ;
    // 商户的公钥钥
    @Value("${alipay.helper.publicKey}")
    private String publicKey;
    // 支付宝的公钥，无需修改该值（不要删除也不要修改，在接收通知的时候需要进行签名认证）
    @Value("${alipay.helper.aliPublicKey}")
    private String aliPublicKey;
    private String signType = "RSA2";
    @Value("${alipay.version}")
    private String version;
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAliPublicKey() {
        return aliPublicKey;
    }

    public void setAliPublicKey(String aliPublicKey) {
        this.aliPublicKey = aliPublicKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
