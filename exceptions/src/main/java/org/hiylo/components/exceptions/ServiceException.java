/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: ServiceException.java
 * Data: 3/29/18 2:46 PM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author hiylo
 * @date 2018年3月29日 14:46:51
 */
public class ServiceException extends BaseException implements Serializable {
    public ServiceException(String code, String message) {
        super(code, message);
    }
}
