/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: BaseException.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:20
 */
public class BaseException extends Exception implements Serializable {
    private static final long serialVersionUID = 2543258395234000337L;
    private String code;

    public BaseException(String code, String message) {
        super("{\"retCode\":\"" + code + "\",\"retObject\":\"" + message + "\"}");
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
