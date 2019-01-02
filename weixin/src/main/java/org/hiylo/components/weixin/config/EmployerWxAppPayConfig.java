/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: EmployerWxAppPayConfig.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.config;

import org.hiylo.components.weixin.pay.IWXPayDomain;
import org.hiylo.components.weixin.pay.WXPayDomainSimpleImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
@ConfigurationProperties
public class EmployerWxAppPayConfig extends AbstractWXPayConfig {
    private byte[] certData;
    private static final String FILE_PATH = "/opt/modules/apiclient_cert_employer.p12";
    @Value("${wechat.employer.appid}")
    private String appid;
    @Value("${wechat.employer.appsecret}")
    private String appsecret;
    @Value("${wechat.employer.partner}")
    private String partner;
    @Value("${wechat.employer.partnerkey}")
    private String partnerkey;

    public EmployerWxAppPayConfig() throws Exception {
        File file = new File(FILE_PATH);
        // File file = new File("C:/Users/hiylo/apiclient_cert.p12");
        InputStream certStream = null;
        try {
            certStream = new FileInputStream(file);
            this.certData = new byte[(int) file.length()];
            certStream.read(this.certData);
        } finally {
            if (certStream != null) {
                certStream.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
    }

    @Override
    public String getAppID() {
        return appid;
    }

    @Override
    public String getMchID() {
        return partner;
    }

    @Override
    public String getKey() {
        return partnerkey;
    }

    public String getSecret() {
        return appsecret;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 10000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return new WXPayDomainSimpleImpl();
    }
}
