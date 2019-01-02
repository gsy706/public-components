/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: FileException.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

/**
 * @author Johnny
 * @ClassName: PictureException
 * @Description: 图片业务异常
 * @date 2016年1月17日 14:15:43
 */
public class FileException extends BaseException implements Serializable {
    private static final long serialVersionUID = -2740402812439388308L;

    public FileException(String code , String message) {
        super(code, message);
    }

}
