/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: I0ItecZKServiceProviderSubscribe.java
 * Data: 3/15/18 6:48 AM
 * Author: hiylo
 */

package org.hiylo.components.zookeeper;

import java.util.List;

public interface I0ItecZKServiceProviderSubscribe {
        /**
         * 监听事件的回调方法
         * @param childs
         */
        void callback(List<String> childs);
}
