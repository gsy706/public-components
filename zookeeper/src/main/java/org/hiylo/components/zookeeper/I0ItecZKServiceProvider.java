/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: I0ItecZKServiceProvider.java
 * Data: 3/15/18 6:06 AM
 * Author: hiylo
 */

package org.hiylo.components.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class I0ItecZKServiceProvider {
    @Value("${zookeeper.locations}")
    public String ZOOKEEPER_SERVER_LOCATION; //"zookeeper:2181", "zookeeper1:2181"

    private static Logger logger = Logger
            .getLogger(I0ItecZKServiceProvider.class);

    protected static ZkClient zkClient;

    @PostConstruct
    private void init() {
        zkClient = new ZkClient(ZOOKEEPER_SERVER_LOCATION);
    }

    public final void put(final String path, final String value) {
        String[] paths = path.split("/");
        boolean serviceExists = zkClient
                .exists(path);
        if (!serviceExists) {
            this.createPath(path);
        }
        if (!zkClient.exists(path + "/"
                + value)) {
            zkClient.createEphemeral(path + "/" + value);
        } else {
            zkClient.delete(path + "/" + value);
            this.put(path, value);
        }
    }

    public final List<String> get(final String path) {
        boolean serviceExists = zkClient
                .exists(path);
        // 如果服务提供者信息存在,则获取服务提供者列表
        if (serviceExists) {
            return zkClient.getChildren(path);
        } else {
            throw new RuntimeException("service not exists");
        }
    }

    public void resetZookeeper(String path) {
        System.out.println(path);
        try {
            ZooKeeper zookeeper = new ZooKeeper(
                    ZOOKEEPER_SERVER_LOCATION, 300000, null);
            List<String> names = zookeeper.getChildren(path, null);
            for (String name : names) {
                String tempPath = "";
                if ("/".equals(path)) {
                    tempPath = path + name;
                } else {
                    tempPath = path + "/" + name;
                }
                this.resetZookeeper(tempPath);
                zookeeper.delete(tempPath, -1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public final void subscribeChildChanges(final String path, final Object obj) {
        if (!(obj instanceof I0ItecZKServiceProviderSubscribe)) {
            throw new RuntimeException("not a subscribe");
        }
        if (zkClient == null) {
            zkClient = new ZkClient(ZOOKEEPER_SERVER_LOCATION);
        }
        boolean serviceExists = zkClient
                .exists(path);
        if (!serviceExists) {
            this.createPath(path);
        }
        zkClient.subscribeChildChanges(path,
                (parentPath, currentChilds) -> obj.getClass().getMethod("callback", List.class).invoke(obj, currentChilds));

    }

    private void createPath(String path) {
        String[] paths = path.split("/");
        String ps = "";
        for (String p : paths) {
            if (StringUtils.hasText(p)) {
                ps += "/" + p;
                boolean exists = zkClient.exists(ps);
                if (!exists) {
                    zkClient.createPersistent(ps);
                }
            }
        }
    }
}
