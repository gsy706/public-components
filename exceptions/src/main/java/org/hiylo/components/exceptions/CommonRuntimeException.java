/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: CommonRuntimeException.java
 * Data: 2/9/18 11:03 AM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author 潘江楠
 * @ClassName: CommonRuntimeException
 * @Description: 公共业务异常
 * @date 2016年12月6日 上午10:08:43
 */
public class CommonRuntimeException extends BaseRuntimeException implements Serializable {
    private static final long serialVersionUID = 2560758391939000337L;

    public CommonRuntimeException(String code, String message) {
        super(code, message);
    }

}
