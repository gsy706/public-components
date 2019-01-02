/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: AliyunPush.java
 * Data: 4/10/18 12:15 PM
 * Author: hiylo
 */

package org.hiylo.components.push;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.*;
import com.aliyuncs.utils.ParameterHelper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties
public class AliyunPush {
    public static final String ENV_PRODUCE = "PRODUCT";
    public static final String ENV_DEV = "DEV";
    private static final Logger logger = LoggerFactory.getLogger(AliyunPush.class);
    @Value("${aliyun.push.appKey}")
    private long appKey;
    @Value("${aliyun.accessId}")
    private String accessId;
    @Value("${aliyun.accessSecret}")
    private String accessSecret;
    private String appSecret;
    private Gson gson = new Gson();

    public List<QueryAliasesResponse.AliasInfo> getAliases(String deviceId)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        QueryAliasesRequest request = new QueryAliasesRequest();
        request.setAppKey(this.appKey);
        request.setDeviceId(deviceId);
        QueryAliasesResponse response = (QueryAliasesResponse) client.getAcsResponse(request);
        return response.getAliasInfos();
    }

    public BindAliasResponse bindAlias(String deviceId, String alias)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        BindAliasRequest request = new BindAliasRequest();
        request.setAppKey(this.appKey);
        request.setDeviceId(deviceId);
        request.setAliasName(alias);
        return client.getAcsResponse(request);
    }

    public UnbindAliasResponse unbindAlias(String deviceId, String alias)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        UnbindAliasRequest request = new UnbindAliasRequest();
        request.setAppKey(this.appKey);
        request.setDeviceId(deviceId);
        request.setAliasName(alias);
        return client.getAcsResponse(request);
    }

    public List<ListSummaryAppsResponse.SummaryAppInfo> getListSummaryApps()
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        ListSummaryAppsRequest request = new ListSummaryAppsRequest();
        ListSummaryAppsResponse response = (ListSummaryAppsResponse) client.getAcsResponse(request);
        return response.getSummaryAppInfos();
    }

    public Boolean checkDevice(String deviceIds)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        CheckDeviceRequest request = new CheckDeviceRequest();
        request.setAppKey(this.appKey);
        request.setDeviceId(deviceIds);
        CheckDeviceResponse response = client.getAcsResponse(request);
        return response.getAvailable();
    }

    public QueryDeviceInfoResponse.DeviceInfo getDeviceInfo(String deviceIds)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        QueryDeviceInfoRequest request = new QueryDeviceInfoRequest();
        request.setAppKey(this.appKey);
        request.setDeviceId(deviceIds);
        QueryDeviceInfoResponse response = (QueryDeviceInfoResponse) client.getAcsResponse(request);
        System.out.printf("RequestId: %s\n", response.getRequestId());
        return response.getDeviceInfo();
    }

    public PushMessageToAndroidResponse pushMessageToAndroid(PushTarget pushTarget, String targetValue, String title, String body)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushMessageToAndroidRequest androidRequest = new PushMessageToAndroidRequest();

        androidRequest.setProtocol(ProtocolType.HTTPS);

        androidRequest.setMethod(MethodType.POST);
        androidRequest.setAppKey(this.appKey);
        androidRequest = processPushMessageToAndroidRequest(pushTarget, targetValue, title, body, androidRequest);
        PushMessageToAndroidResponse response = (PushMessageToAndroidResponse) client.getAcsResponse(androidRequest);
        return response;
    }

    public PushNoticeToAndroidResponse pushNoticeToAndroid(PushTarget pushTarget, String targetValue, String title, String body, Map<String, String> ext)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushNoticeToAndroidRequest androidRequest = new PushNoticeToAndroidRequest();

        androidRequest.setProtocol(ProtocolType.HTTPS);

        androidRequest.setMethod(MethodType.POST);
        androidRequest.setAppKey(Long.valueOf(this.appKey));
        androidRequest = processPushNoticeToAndroidRequest(pushTarget, targetValue, title, body, androidRequest);
        androidRequest.setExtParameters(this.gson.toJson(ext));

        PushNoticeToAndroidResponse response = (PushNoticeToAndroidResponse) client.getAcsResponse(androidRequest);
        return response;
    }

    public PushMessageToiOSResponse pushMessageToIOS(PushTarget pushTarget, String targetValue, String title, String body)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushMessageToiOSRequest iOSRequest = new PushMessageToiOSRequest();

        iOSRequest.setProtocol(ProtocolType.HTTPS);

        iOSRequest.setMethod(MethodType.POST);
        iOSRequest.setAppKey(Long.valueOf(this.appKey));
        iOSRequest = processPushMessageToIOSRequest(pushTarget, targetValue, title, body, iOSRequest);
        PushMessageToiOSResponse response = (PushMessageToiOSResponse) client.getAcsResponse(iOSRequest);
        return response;
    }

    public PushNoticeToiOSResponse pushNoticeToIOS(PushTarget pushTarget, String targetValue, String title, String body, Map<String, String> ext, String env)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushNoticeToiOSRequest iOSRequest = new PushNoticeToiOSRequest();

        iOSRequest.setProtocol(ProtocolType.HTTPS);

        iOSRequest.setMethod(MethodType.POST);
        iOSRequest.setAppKey(Long.valueOf(this.appKey));

        iOSRequest.setApnsEnv("PRODUCT");
        iOSRequest = processPushNoticeToIOSRequest(pushTarget, targetValue, title, body, iOSRequest);
        iOSRequest.setExtParameters(this.gson.toJson(ext));

        PushNoticeToiOSResponse response = (PushNoticeToiOSResponse) client.getAcsResponse(iOSRequest);
        return response;
    }

    public PushResponse advancedPush(PushPlateform pushPlateform, PushType pushType, PushTarget pushTarget, OpenType openType, String openValue, String targetValue, String title, String body, Map<String, String> ext, String env)
            throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushRequest pushRequest = new PushRequest();

        pushRequest.setProtocol(ProtocolType.HTTPS);

        pushRequest.setMethod(MethodType.POST);

        pushRequest.setAppKey(Long.valueOf(this.appKey));
        pushRequest = processPushRequest(pushTarget, targetValue, title, body, pushRequest);
        switch (pushType) {
            case message:
                pushRequest.setPushType("MESSAGE");
                break;
            case notification:
                pushRequest.setPushType("NOTICE");
                break;
            default:
                break;
        }
        switch (pushPlateform) {
            case all:
                pushRequest.setDeviceType("ALL");
                break;
            case ios:
                pushRequest.setDeviceType("iOS");
                break;
            case android:
                pushRequest.setDeviceType("ANDROID");
                break;
            default:
                break;
        }
        pushRequest.setTitle(title);

        pushRequest.setBody(body);

        pushRequest.setIOSMutableContent(Boolean.TRUE);

        pushRequest.setIOSApnsEnv(env);

        pushRequest.setIOSRemind(Boolean.TRUE);

        pushRequest.setIOSRemindBody(body);

        pushRequest.setIOSExtParameters(this.gson.toJson(ext));

        pushRequest.setAndroidNotifyType("BOTH");

        pushRequest.setAndroidNotificationBarType(1);

        pushRequest.setAndroidNotificationBarPriority(1);
        switch (openType) {
            case URL:
                pushRequest.setAndroidOpenType("URL");
                break;
            case NONE:
                pushRequest.setAndroidOpenType("NONE");
                break;
            case ACTIVITY:
                pushRequest.setAndroidOpenType("ACTIVITY");
                break;
            case APPLICATION:
                pushRequest.setAndroidOpenType("APPLICATION");
                break;
            default:
                break;
        }
        pushRequest.setAndroidOpenUrl(openValue);

        pushRequest.setAndroidActivity(openValue);

        pushRequest.setAndroidMusic("default");

        pushRequest.setAndroidExtParameters(this.gson.toJson(ext));

        Date pushDate = new Date(System.currentTimeMillis());

        String pushTime = ParameterHelper.getISO8601Time(pushDate);
        pushRequest.setPushTime(pushTime);

        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 43200000L));

        pushRequest.setExpireTime(expireTime);

        pushRequest.setStoreOffline(Boolean.TRUE);

        return client.getAcsResponse(pushRequest);
    }

    private PushRequest processPushRequest(PushTarget pushTarget, String targetValue, String title, String body, PushRequest request) {
        switch (pushTarget) {
            case ALL:
                request.setTarget("ALL");
                request.setTargetValue("all");
                break;
            case TAG:
                request.setTarget("TAG");
                request.setTargetValue(targetValue);
                break;
            case ALIAS:
                request.setTarget("ALIAS");
                request.setTargetValue(targetValue);
                break;
            case ACCOUNT:
                request.setTarget("ACCOUNT");
                request.setTargetValue(targetValue);
                break;
            case DEVICE:
                request.setTarget("DEVICE");
                request.setTargetValue(targetValue);
                break;
            default:
                break;
        }
        request.setTitle(title);
        request.setBody(body);
        return request;
    }

    private PushNoticeToiOSRequest processPushNoticeToIOSRequest(PushTarget pushTarget, String targetValue, String title, String body, PushNoticeToiOSRequest request) {
        switch (pushTarget) {
            case ALL:
                request.setTarget("ALL");
                request.setTargetValue("all");
                break;
            case TAG:
                request.setTarget("TAG");
                request.setTargetValue(targetValue);
                break;
            case ALIAS:
                request.setTarget("ALIAS");
                request.setTargetValue(targetValue);
                break;
            case ACCOUNT:
                request.setTarget("ACCOUNT");
                request.setTargetValue(targetValue);
                break;
            case DEVICE:
                request.setTarget("DEVICE");
                request.setTargetValue(targetValue);
                break;
            default:
                break;
        }
        request.setTitle(title);
        request.setBody(body);
        return request;
    }

    private PushMessageToiOSRequest processPushMessageToIOSRequest(PushTarget pushTarget, String targetValue, String title, String body, PushMessageToiOSRequest request) {
        switch (pushTarget) {
            case ALL:
                request.setTarget("ALL");
                request.setTargetValue("all");
                break;
            case TAG:
                request.setTarget("TAG");
                request.setTargetValue(targetValue);
                break;
            case ALIAS:
                request.setTarget("ALIAS");
                request.setTargetValue(targetValue);
                break;
            case ACCOUNT:
                request.setTarget("ACCOUNT");
                request.setTargetValue(targetValue);
                break;
            case DEVICE:
                request.setTarget("DEVICE");
                request.setTargetValue(targetValue);
                break;
            default:
                break;
        }
        request.setTitle(title);
        request.setBody(body);
        return request;
    }

    private PushMessageToAndroidRequest processPushMessageToAndroidRequest(PushTarget pushTarget, String targetValue, String title, String body, PushMessageToAndroidRequest request) {
        switch (pushTarget) {
            case ALL:
                request.setTarget("ALL");
                request.setTargetValue("all");
                break;
            case TAG:
                request.setTarget("TAG");
                request.setTargetValue(targetValue);
                break;
            case ALIAS:
                request.setTarget("ALIAS");
                request.setTargetValue(targetValue);
                break;
            case ACCOUNT:
                request.setTarget("ACCOUNT");
                request.setTargetValue(targetValue);
                break;
            case DEVICE:
                request.setTarget("DEVICE");
                request.setTargetValue(targetValue);
                break;
            default:
                break;
        }
        request.setTitle(title);
        request.setBody(body);
        return request;
    }

    private PushNoticeToAndroidRequest processPushNoticeToAndroidRequest(PushTarget pushTarget, String targetValue, String title, String body, PushNoticeToAndroidRequest request) {
        switch (pushTarget) {
            case ALL:
                request.setTarget("ALL");
                request.setTargetValue("all");
                break;
            case TAG:
                request.setTarget("TAG");
                request.setTargetValue(targetValue);
                break;
            case ALIAS:
                request.setTarget("ALIAS");
                request.setTargetValue(targetValue);
                break;
            case ACCOUNT:
                request.setTarget("ACCOUNT");
                request.setTargetValue(targetValue);
                break;
            case DEVICE:
                request.setTarget("DEVICE");
                request.setTargetValue(targetValue);
                break;
            default:
                break;
        }
        request.setTitle(title);
        request.setBody(body);
        return request;
    }

    public List<ListTagsResponse.TagInfo> listTags()
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        ListTagsRequest request = new ListTagsRequest();
        request.setAppKey(Long.valueOf(this.appKey));

        ListTagsResponse response = (ListTagsResponse) client.getAcsResponse(request);
        List<ListTagsResponse.TagInfo> tagInfos = response.getTagInfos();
        return tagInfos;
    }

    public List<QueryTagsResponse.TagInfo> queryTag(PushTarget pushTarget, String deviceIds, String tag)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        QueryTagsRequest request = new QueryTagsRequest();
        request.setAppKey(Long.valueOf(this.appKey));
        switch (pushTarget) {
            case DEVICE:
                request.setKeyType("DEVICE");
                break;
            case ACCOUNT:
                request.setKeyType("ACCOUNT");
                break;
            case ALIAS:
                request.setKeyType("ALIAS");
                break;
            default:
                break;
        }
        request.setClientKey(deviceIds);
        QueryTagsResponse response = (QueryTagsResponse) client.getAcsResponse(request);
        return response.getTagInfos();
    }

    public BindTagResponse bindTag(PushTarget pushTarget, String deviceIds, String tag)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        BindTagRequest request = new BindTagRequest();
        request.setAppKey(this.appKey);
        switch (pushTarget) {
            case DEVICE:
                request.setKeyType("DEVICE");
                break;
            case ACCOUNT:
                request.setKeyType("ACCOUNT");
                break;
            case ALIAS:
                request.setKeyType("ALIAS");
                break;
            default:
                break;
        }
        request.setClientKey(deviceIds);

        request.setTagName(tag);

        BindTagResponse response = (BindTagResponse) client.getAcsResponse(request);
        logger.info("request type is : [" + request.getKeyType() + "] device id is : [" + deviceIds + "] request response id is : [" + response.getRequestId() + ", tags is : " + tag + "]");
        return response;
    }

    public UnbindTagResponse unbindTag(PushTarget pushTarget, String deviceIds, String tag)
            throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.accessId, this.accessSecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        UnbindTagRequest request = new UnbindTagRequest();
        request.setAppKey(this.appKey);
        request.setClientKey(deviceIds);
        switch (pushTarget) {
            case DEVICE:
                request.setKeyType("DEVICE");
                break;
            case ACCOUNT:
                request.setKeyType("ACCOUNT");
                break;
            case ALIAS:
                request.setKeyType("ALIAS");
                break;
            default:
                break;
        }
        request.setTagName(tag);
        return client.getAcsResponse(request);
    }

    public  enum PushType {
        /**
         * 通知
         */
        notification,
        /**
         * 消息
         */
        message;

         PushType() {
        }
    }

    /**
     * 推送目标平台
     */
    public enum PushPlateform {
        /**
         * ios系统
         */
        @SuppressWarnings("AlibabaEnumConstantsMustHaveComment") ios,
        /**
         * android系统
         */
        android,
        /**
         * 所有系统
         */
        all;

        PushPlateform() {
        }
    }

    public  enum PushTarget {
        DEVICE, ACCOUNT, ALIAS, TAG, ALL;

         PushTarget() {
        }
    }

    public  enum OpenType {
        APPLICATION, ACTIVITY, URL, NONE;

         OpenType() {
        }
    }


    public static void main(String[] args) {

    }
}
