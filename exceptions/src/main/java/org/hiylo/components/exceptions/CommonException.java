/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: CommonException.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author 潘江楠
 * @ClassName: CommonException
 * @Description: 公共业务异常
 * @date 2016年12月6日 上午10:08:43
 */
public class CommonException extends BaseException implements Serializable {
    private static final long serialVersionUID = 2560758391939000337L;

    public CommonException(String code, String message) {
        super(code, message);
    }

}
