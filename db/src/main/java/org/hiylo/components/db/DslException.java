/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: DslException.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.db;

/**
 * @author hiylo
 * @date 2017年10月19日 08:08:39
 */
public class DslException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DslException(String message) {
        super(message);
    }

    public DslException(String message, Throwable cause) {
        super(message, cause);
    }

    public DslException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DslException() {
        super();
    }

    public DslException(Throwable cause) {
        super(cause);
    }

}
