/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: SkuException.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:34
 */
public class SkuException extends BaseException implements Serializable {
    private static final long serialVersionUID = 2560758395331000337L;

    public SkuException(String code, String message) {
        super(code, message);
    }
}