/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: WXPayRequest.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.pay;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.hiylo.components.weixin.config.AbstractWXPayConfig;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;

public class WXPayRequest {
    private AbstractWXPayConfig config;

    public WXPayRequest(AbstractWXPayConfig config) throws Exception {
        this.config = config;
    }

    private String requestOnce(final String domain, String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert) throws Exception {
        BasicHttpClientConnectionManager connManager;
        if (useCert) {
            // 证书
            char[] password = config.getMchID().toCharArray();
            InputStream certStream = config.getCertStream();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());

            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(),
                    null,
                    null,
                    null
            );
        } else {
            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
        }

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        String url = urlSuffix;
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + config.getMchID());
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");

    }


    private String request(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert, boolean autoReport) throws Exception {
        Exception exception = null;
        long elapsedTimeMillis = 0;
        long startTimestampMs = WXPayUtil.getCurrentTimestampMs();
        boolean firstHasDnsErr = false;
        boolean firstHasConnectTimeout = false;
        boolean firstHasReadTimeout = false;
        IWXPayDomain.DomainInfo domainInfo = config.getWXPayDomain().getDomain(config);
        if (domainInfo == null) {
            throw new Exception("WXPayConfig.getWXPayDomain().getDomain() is empty or null");
        }
        try {
            String result = requestOnce(domainInfo.domain, urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, useCert);
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            config.getWXPayDomain().report(domainInfo.domain, elapsedTimeMillis, null);
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout);
            return result;
        } catch (UnknownHostException ex) {  // dns 解析错误，或域名不存在
            exception = ex;
            firstHasDnsErr = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            WXPayUtil.getLogger().warn("UnknownHostException for domainInfo {}", domainInfo);
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout
            );
        } catch (ConnectTimeoutException ex) {
            exception = ex;
            firstHasConnectTimeout = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            WXPayUtil.getLogger().warn("connect timeout happened for domainInfo {}", domainInfo);
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout
            );
        } catch (SocketTimeoutException ex) {
            exception = ex;
            firstHasReadTimeout = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            WXPayUtil.getLogger().warn("timeout happened for domainInfo {}", domainInfo);
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout);
        } catch (Exception ex) {
            exception = ex;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout);
        }
        config.getWXPayDomain().report(domainInfo.domain, elapsedTimeMillis, exception);
        throw exception;
    }

    public String requestWithoutCert(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean autoReport) throws Exception {
        return this.request(urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, false, autoReport);
    }

    public String requestWithCert(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean autoReport) {
        try {
            return this.request(urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, true, autoReport);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
