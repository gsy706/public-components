/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: StringUtils.java
 * Data: 2/8/18 7:04 PM
 * Author: hiylo
 */

package org.hiylo.components.commons.utils;

public class StringUtils {

    public static boolean isNotEmpty(String... values) {
        for (String value : values) {
            if (value != null) {
                if (!org.apache.commons.lang.StringUtils.isEmpty(value.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}
