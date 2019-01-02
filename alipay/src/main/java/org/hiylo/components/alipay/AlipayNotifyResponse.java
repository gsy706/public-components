/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: AlipayNotifyResponse.java
 * Data: 4/2/18 11:57 PM
 * Author: hiylo
 */

package org.hiylo.components.alipay;

public class AlipayNotifyResponse {
    private String amount;
    private String outTradeNo;
    private String tradeNo;
    private String passbackParams;

    public AlipayNotifyResponse() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPassbackParams() {
        return passbackParams;
    }

    public void setPassbackParams(String passbackParams) {
        this.passbackParams = passbackParams;
    }

    public AlipayNotifyResponse(String amount, String outTradeNo, String tradeNo, String passbackParams) {
        this.amount = amount;
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
        this.passbackParams = passbackParams;
    }
}
