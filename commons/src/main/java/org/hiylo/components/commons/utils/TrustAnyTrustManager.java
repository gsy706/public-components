/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: TrustAnyTrustManager.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.commons.utils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:34
 */
public class TrustAnyTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
