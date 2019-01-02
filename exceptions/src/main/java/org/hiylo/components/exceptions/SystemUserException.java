/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: SystemUserException.java
 * Data: 4/11/18 2:20 PM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author hiylo
 * @date 2018年3月29日 14:47:36
 */
public class SystemUserException extends BaseException implements Serializable {

    public SystemUserException(String code, String message) {
        super(code, message);
    }
}
