/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: PushProcessor.java
 * Data: 4/17/18 11:10 AM
 * Author: hiylo
 */

package org.hiylo.components.push.api;

import org.hiylo.components.push.vo.PushMessage;

public interface PushProcessor {

    void process(PushMessage content);
}
