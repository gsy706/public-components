/*
 * Copyright (c)  2017 Hiylo Org. All rights reserved
 * Project: framework
 * File: SmsUtil.java
 * Data: 11/27/17 7:54 AM
 * Author: hiylo
 */

package org.hiylo.components.sms;

import org.hiylo.components.commons.utils.RegExpUtils;
import org.hiylo.components.couchbase.CommonCouchBase;
import org.hiylo.components.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author hiylo
 * @date 2017年10月19日 08:09:34
 */
@Component
public class SmsUtil {
    private static final String EXCEPTION_CODE_WRONG_PHONE_NUMBER = "0x10000007";
    public static final String EXCEPTION_CODE_SMS_CODE_MISSING = "0x10000029";
    @Autowired
    private CommonCouchBase commonCouchBase;

    public boolean checkCode(String code, String smsTemplateNumber, String username) throws UserException {
        if (!RegExpUtils.isPhoneNumber(username)) {
            throw new UserException(EXCEPTION_CODE_WRONG_PHONE_NUMBER, "手机号码格式错误");
        }
        String couchSMSCheckCode = commonCouchBase.get(username + smsTemplateNumber);
        // 判断短信验证码是否存在
        if (!StringUtils.hasText(couchSMSCheckCode) || !code.equals(couchSMSCheckCode)) {
            throw new UserException(EXCEPTION_CODE_SMS_CODE_MISSING, "短信验证码错误");
        }
        return true;
    }
}
