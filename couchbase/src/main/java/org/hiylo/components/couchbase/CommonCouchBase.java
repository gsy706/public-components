/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: CommonCouchBase.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */
package org.hiylo.components.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.StringDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 朱玺
 * @date 2016年12月13日 上午11:08:04
 */
@Component
@Configuration
@ConfigurationProperties
public class CommonCouchBase {
    private static final long COUCHBASE_DEFAULT_TIME_OUT = 3000L;
    @Value("${couchbase.bootstrap-hosts}")
    private List<String> bootstrapHosts;
    @Value("${couchbase.bucket.name}")
    private String bucketName;
    @Value("${couchbase.username}")
    private String username;
    @Value("${couchbase.password}")
    private String password;

    //    public static void main(String[] args) {
//        try {
//            Map<String, String> map = new HashMap(0, 0.75F);
//            map.put("1", "2");
//            map.put("3", "6");
//            new CommonCouchBase().put("15", "test", 2000);
//            System.out.println(new CommonCouchBase().get("15"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    protected List<String> getBootstrapHosts() {
        return bootstrapHosts;
    }

    protected String getBucketName() {
        return bucketName;
    }

    protected String getBucketUsername() {
        return username;
    }

    protected String getBucketPassword() {
        return password;
    }

    protected CouchbaseEnvironment getEnvironment() {
        return DefaultCouchbaseEnvironment.builder().managementTimeout(30000).
                analyticsTimeout(30000).connectTimeout(30000).kvTimeout(30000).
                queryTimeout(30000).searchTimeout(30000).viewTimeout(30000).build();
    }
    public CouchbaseEnvironment couchbaseEnvironment() {
        CouchbaseEnvironment env = this.getEnvironment();
        return env;
    }

    public Cluster couchbaseCluster() {
        return CouchbaseCluster.create(this.couchbaseEnvironment(), this.getBootstrapHosts());
    }

    public Bucket couchbaseClient() {

        Bucket bucket = this.couchbaseCluster().authenticate(this.getBucketUsername(), this.getBucketPassword()).openBucket(this.getBucketName());
        return bucket;
    }

    /**
     * 向Couchbase里面存值
     *
     * @param id    document id
     * @param key   json 键值对 键
     * @param value json 键值对 值 @
     */
    public void put(String id, String key, String value) {
        try {
            Map<String, String> map = new HashMap<>(1, 0.75F);
            map.put(key, value);
            JsonObject o = JsonObject.from(map);
            JsonDocument document = JsonDocument.create(id, o);
            this.couchbaseClient().upsert(document, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String id, String content) {
        try {
            StringDocument document = StringDocument.create(id, content);
            this.couchbaseClient().upsert(document, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String id, String content, int expiry) {
        try {
            StringDocument document = StringDocument.create(id, content);
            this.couchbaseClient().upsert(document, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
            this.couchbaseClient().touch(id, expiry, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 向Couchbase里面存值
     *
     * @param id  document id
     * @param map 键值对 @
     */
    public void put(String id, Map<String, ?> map) {
        try {
            JsonObject o = JsonObject.from(map);
            JsonDocument document = JsonDocument.create(id, o);
            this.couchbaseClient().upsert(document, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向Couchbase里面存值
     *
     * @param id     document id
     * @param key    键值对 键
     * @param value  键值对 值
     * @param expiry 过期时间单位秒 @
     */
    public void put(String id, String key, Object value, int expiry) {
        HashMap<String, Object> map = new HashMap<>(1, 0.75F);
        map.put(key, value);
        JsonDocument document = JsonDocument.create(id, JsonObject.from(map));
        this.couchbaseClient().upsert(document, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        this.couchbaseClient().touch(id, expiry, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
    }

    /**
     * 向Couchbase里面存值
     *
     * @param id     document id
     * @param key    键值对 键
     * @param value  键值对 值
     * @param expiry 过期时间单位秒 @
     */
    public void put(String id, String key, List value, int expiry) {
        JsonDocument document = JsonDocument.create(id, JsonObject.create().put(key, value));
        this.couchbaseClient().upsert(document, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        this.couchbaseClient().touch(id, expiry, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
    }

    /**
     * 向Couchbase里面存值
     *
     * @param id     document id
     * @param map    键值对
     * @param expiry 过期时间 单位秒 @
     */
    public void put(String id, Map<String, ?> map, int expiry) {
        JsonObject o = JsonObject.from(map);
        JsonDocument document = JsonDocument.create(id, o);
        this.couchbaseClient().upsert(document, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
        this.couchbaseClient().touch(id, expiry, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取Couchbase 中的内容
     *
     * @param id document id
     * @return json document @
     */
    public JsonDocument getJsonDocument(String id) {
        return this.couchbaseClient().get(id, COUCHBASE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);
    }

    public Object get(String id, String key) {
        return ((JsonDocument) this.getJsonDocument(id)).<Map<String, String>>content().get(key);
    }

    public List getList(String id, String key) {
        JsonDocument jsonDocument = this.getJsonDocument(id);
        if (jsonDocument != null) {
            return jsonDocument.content().getArray(key).toList();
        }
        return null;
    }

    public String get(String id) {
        StringDocument document = StringDocument.create(id);
        if (this.couchbaseClient().get(document) != null) {
            return this.couchbaseClient().get(document).content();
        }
        return null;
    }

}
