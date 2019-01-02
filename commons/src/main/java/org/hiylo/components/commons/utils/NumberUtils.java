/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: AmountUtils.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.commons.utils;

import java.text.DecimalFormat;

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:34
 */
public class NumberUtils {
    /**
     * 将double类型的数值转化为保留两位小数的字符串
     *
     * @param price double类型的数值
     * @return 保留两位小数的字符串
     */
    public static String castDoublePriceToString(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(price);
    }

    public static boolean greateeZore(int... values) {
        for (int value : values) {
            if (value <= 0) {
                return false;
            }
        }
        return true;
    }
}
