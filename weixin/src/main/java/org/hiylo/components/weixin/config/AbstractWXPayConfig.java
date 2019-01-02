/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: AbstractWXPayConfig.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.config;

import org.hiylo.components.weixin.pay.IWXPayDomain;

@SuppressWarnings("ALL")
public abstract class AbstractWXPayConfig implements com.github.wxpay.sdk.WXPayConfig {
    public static final String EXCEPTION_CODE_PAYMENT_TOTAL_FEE_CANT_BE_ZERO = "0x10700001";
    public static final String EXCEPTION_CODE_PAYMENT_REQUEST_THIRD_PART_FAIL = "0x10700002";
    /**
     * 获取WXPayDomain, 用于多域名容灾自动切换
     * @return
     */
    public abstract IWXPayDomain getWXPayDomain();

    /**
     * 是否自动上报。
     * 若要关闭自动上报，子类中实现该函数返回 false 即可。
     *
     * @return
     */
    public boolean shouldAutoReport() {
        return true;
    }

    /**
     * 进行健康上报的线程的数量
     *
     * @return
     */
    public int getReportWorkerNum() {
        return 6;
    }


    /**
     * 健康上报缓存消息的最大数量。会有线程去独立上报
     * 粗略计算：加入一条消息200B，10000消息占用空间 2000 KB，约为2MB，可以接受
     *
     * @return
     */
    public int getReportQueueMaxSize() {
        return 10000;
    }

    /**
     * 批量上报，一次最多上报多个数据
     *
     * @return
     */
    public int getReportBatchSize() {
        return 10;
    }

}
