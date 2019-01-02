/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: PushMessage.java
 * Data: 4/13/18 5:02 PM
 * Author: hiylo
 */

package org.hiylo.components.push.vo;

import org.hiylo.components.push.AliyunPush;

import java.io.Serializable;
import java.util.Map;

public class PushMessage implements Serializable {
    private String helperCode;
    private String employerCode;
    private String title;
    private String content;
    private Map ext;
    /**
     * 此字段只有广播适用
     * 1 推送给雇主端   2 推送给雇工端  3 推送给所有
     */
    private Byte type;
    private AliyunPush.OpenType openType;
    private String openValue;
    private long timestamp;
    private boolean success;

    public String getHelperCode() {
        return helperCode;
    }

    public void setHelperCode(String helperCode) {
        this.helperCode = helperCode;
    }

    public String getEmployerCode() {
        return employerCode;
    }

    public void setEmployerCode(String employerCode) {
        this.employerCode = employerCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map getExt() {
        return ext;
    }

    public void setExt(Map ext) {
        this.ext = ext;
    }

    public AliyunPush.OpenType getOpenType() {
        return openType;
    }

    public void setOpenType(AliyunPush.OpenType openType) {
        this.openType = openType;
    }

    public String getOpenValue() {
        return openValue;
    }

    public void setOpenValue(String openValue) {
        this.openValue = openValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public PushMessage() {
    }

    public PushMessage(String helperCode, String employerCode, String title, String content, Map ext, AliyunPush.OpenType openType, String openValue, long timestamp, Byte type) {
        this.helperCode = helperCode;
        this.employerCode = employerCode;
        this.title = title;
        this.content = content;
        this.ext = ext;
        this.openType = openType;
        this.openValue = openValue;
        this.timestamp = timestamp;
        this.type = type;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "helperCode='" + helperCode + '\'' +
                ", employerCode='" + employerCode + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", ext=" + ext +
                ", openType=" + openType +
                ", openValue='" + openValue + '\'' +
                ", timestamp=" + timestamp +
                ", success=" + success +
                '}';
    }
}
