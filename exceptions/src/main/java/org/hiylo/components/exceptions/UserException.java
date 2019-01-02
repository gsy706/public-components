/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: UserException.java
 * Data: 4/10/18 12:40 AM
 * Author: hiylo
 */

package org.hiylo.components.exceptions;

import java.io.Serializable;

public class UserException  extends BaseException implements Serializable {
    public UserException(String code, String message) {
        super(code, message);
    }
}
