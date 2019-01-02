/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: ServiceTypeException.java
 * Data: 3/29/18 2:47 PM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author hiylo
 * @date 2018年3月29日 14:47:36
 */
public class ServiceTypeException extends BaseException implements Serializable {

    public ServiceTypeException(String code, String message) {
        super(code, message);
    }
}
