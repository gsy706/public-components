/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: AliSms.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */
package org.hiylo.components.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import org.hiylo.components.commons.utils.GenerateUtil;
import org.hiylo.components.commons.utils.RegExpUtils;
import org.hiylo.components.couchbase.CommonCouchBase;
import org.hiylo.components.exceptions.CommonException;
import org.hiylo.components.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import static org.hiylo.components.sms.SmsUtil.EXCEPTION_CODE_SMS_CODE_MISSING;

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:34
 */
@Component
@ConfigurationProperties
public class AliSms {
    /**
     * 短信_验证码_注册_模板编号
     */
    @Deprecated
    public static final int SMS_CHECK_CODE_REGISTER = 0;
    /**
     * 短信_验证码_找回密码_模板编号
     */
    @Deprecated
    public static final int SMS_CHECK_CODE_FIND_PSW = 1;
    /**
     * 短信_验证码_绑定手机_模板编号
     */
    private static final String EXCEPTION_CODE_WRONG_PHONE_NUMBER = "0x10000007";
    @Autowired
    private CommonCouchBase commonCouchBase;
    @Value("${aliyun.accessId}")
    private String accessId;
    @Value("${aliyun.accessSecret}")
    private String accessSecret;

    public static void main(String[] args) throws ParseException, CommonException {
    }

    /**
     * register短信  阿里云新SMS业务 推荐的短信发送方式
     *
     * @param mobile            手机号码
     * @param smsTemplateNumber 模板编号
     */
    public String sendSMSCheckCode(String mobile, String smsTemplateNumber, String signName, int smsCodeLength) throws CommonException, ClientException, UserException {
        if (!RegExpUtils.isPhoneNumber(mobile)) {
            throw new UserException(EXCEPTION_CODE_WRONG_PHONE_NUMBER, "手机号码格式错误");
        }
        String couchSMSCheckCode = commonCouchBase.get(mobile + smsTemplateNumber);
        // 判断短信验证码是否存在
        if (StringUtils.hasText(couchSMSCheckCode)) {
            return couchSMSCheckCode;
        }
        /**
         * 生成数字验证码
         */
        String checkCode = GenerateUtil.generateStrNumber(smsCodeLength);
        String content = "{\"code\":\"" + checkCode + "\"}";
        if (send(mobile, content, smsTemplateNumber, signName)) {
            commonCouchBase.put(mobile + smsTemplateNumber, checkCode, 10 * 60 * 1000);
            return checkCode;
        }
        return null;
    }

    /**
     * 发送短信 阿里云原SMS业务
     *
     * @return 成功 失败
     * @throws ClientException
     */
    public boolean send(String phoneNumber, String content, String smsTemplate, String signName) throws ClientException, UserException {
        if (!RegExpUtils.isPhoneNumber(phoneNumber)) {
            throw new UserException(EXCEPTION_CODE_WRONG_PHONE_NUMBER, "手机号码格式错误");
        }
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        //短信API产品名称（短信产品名固定，无需修改）
        final String product = "Dysmsapi";
        //短信API产品域名（接口地址固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";
        //替换成你的AK

        //初始化ascClient,暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessId,
                accessSecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(smsTemplate);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(content);
        //可选-上行短信扩展码(无特殊需求用户请忽略此字段)
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");
//请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        System.out.println("Code: " + sendSmsResponse.getCode() + ", Message: " + sendSmsResponse.getMessage());
        if (sendSmsResponse.getCode() != null && "OK".equals(sendSmsResponse.getCode())) {
//请求成功
            return true;
        }
        return false;
    }

    public String getRandNum(int charCount) {
        StringBuffer charValue = new StringBuffer();
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue.append(String.valueOf(c));
        }
        return charValue.toString();
    }

    public int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }
}
