/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: PaymentException.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */
package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author 朱玺
 * @ClassName: PaymentException
 * @date 2016年12月19日 下午4:11:08
 */
public class PaymentException extends BaseException implements Serializable {
    /**
     */
    private static final long serialVersionUID = -6589911143820148356L;

    public PaymentException(String code, String message) {
        super(code, message);
    }

}
