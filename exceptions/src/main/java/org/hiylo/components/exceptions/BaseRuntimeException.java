/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: BaseRuntimeException.java
 * Data: 2/9/18 11:03 AM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:20
 */
public class BaseRuntimeException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 2543258395234000337L;
    private String code;

    public BaseRuntimeException(String code, String message) {
        super(message);
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
