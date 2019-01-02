/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: Oss.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */
package org.hiylo.components.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectResult;
import org.hiylo.components.exceptions.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:34
 */
@Component
@ConfigurationProperties
public class Oss {
    private final static String DEFAULT_ENCODING = "UTF-8";
    /**
     * 图片_类型_用户头像_模板编号
     */
    public static final int PICTURE_TYPE_CODE_USER_DISPLAY_IMAGE = 1;
    /**
     * 图片_类型_商品评价_模板编号
     */
    public static final int PICTURE_TYPE_CODE_PRODUCE_EVALUATE_IMAGE = 2;
    /**
     * 图片_类型_模版
     */
    public static final Map<Integer, String> PICTURE_TYPE_TEMPLATES = new HashMap<Integer, String>() {
        {
            /**
             * 用户头像图片
             */
            put(PICTURE_TYPE_CODE_USER_DISPLAY_IMAGE, "users-image");
            /**
             * 商品评价图片
             */
            put(PICTURE_TYPE_CODE_PRODUCE_EVALUATE_IMAGE, "produce-evaluate-image");
        }
    };
//    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${aliyun.accessId}")
    private String accessId;
    @Value("${aliyun.accessSecret}")
    private String accessSecret;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    private OSSClient ossClient;

//    public static void main(String[] args) {
//        try {
//            new Oss().uploadAndSaveFile(new FileInputStream(new File("C:\\Users\\hiylo\\Desktop", "1003956-20160929094610156-2054520507.png")), "112", "ontheway-general");
//        } catch (CommonException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean uploadAndSaveFile(InputStream inputStream, String fileName, String bucketName) throws CommonException {
        if (ossClient == null) {
            ossClient = new OSSClient(endpoint, accessId, accessSecret);
        }
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
        }
        PutObjectResult result = ossClient.putObject(bucketName, fileName, inputStream);
        if (result.getResponse().isSuccessful()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean uploadAndSaveFile(byte[] content, String fileName, String bucketName) throws CommonException {
        if (ossClient == null) {
            ossClient = new OSSClient(endpoint, accessId, accessSecret);
        }
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
        }
        PutObjectResult result = ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(content));
        if (result.getResponse().isSuccessful()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean uploadAndSaveFile(String content, String fileName, String bucketName) throws CommonException {
        if (ossClient == null) {
            ossClient = new OSSClient(endpoint, accessId, accessSecret);
        }
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
        }
        PutObjectResult result = null;
        try {
            result = ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(content.getBytes(DEFAULT_ENCODING)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (result.getResponse().isSuccessful()) {
            return true;
        } else {
            return true;
        }
    }

    public boolean uploadAndSaveFile(String content, String fileName, String bucketName, String charset) throws CommonException {
        if (ossClient == null) {
            ossClient = new OSSClient(endpoint, accessId, accessSecret);
        }
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
        }
        PutObjectResult result = null;
        try {
            result = ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(content.getBytes(charset)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (result.getResponse().isSuccessful()) {
            return true;
        } else {
            return true;
        }
    }
    public String uploadFile(String key, InputStream input, String bucketName) {
        if (ossClient == null) {
            ossClient = new OSSClient(endpoint, accessId, accessSecret);
        }
        ossClient.putObject(bucketName, key, input);
        ossClient.shutdown();
        return key;
    }

    public List<String> getFileViaPrifex(String bucketName, String keyPrifex) {
        List<String> keys = new ArrayList<String>(20);
        if (ossClient == null) {
            ossClient = new OSSClient(endpoint, accessId, accessSecret);
        }
        ObjectListing objectListing = ossClient.listObjects(bucketName, keyPrifex);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        for (OSSObjectSummary s : sums) {
            keys.add(s.getKey());
        }
        return keys;
    }

    public String getFile(String key, String bucketName) {
        if (ossClient == null) {
            ossClient = new OSSClient(endpoint, accessId, accessSecret);
        }
        GetObjectRequest request = new GetObjectRequest(bucketName, key);
        ossClient.getObject(request, new File("/opt/modules/logs/" + key.split("/")[key.split("/").length - 1]));
        return "/opt/modules/logs/" + key.split("/")[key.split("/").length - 1];
    }
}
