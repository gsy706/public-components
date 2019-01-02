/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: ValidateUtils.java
 * Data: 2/9/18 11:37 AM
 * Author: hiylo
 */

package org.hiylo.components.commons.utils;

import org.springframework.validation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hiylo
 * @date 2018年2月9日 11:43:53
 */
public class ValidateUtils {
    /**
     * 判断String 不是null 不是 "" ,另外每一个string都会被trim
     *
     * @param values 需要验证的值
     * @return 成功或者失败
     */
    public static boolean isNotEmpty(String... values) {
        return StringUtils.isNotEmpty(values);
    }

    /**
     * 是不是大于0
     *
     * @param values 需要判断的值
     * @return 成功或者失败
     */
    public static boolean greateeZore(int... values) {
        return NumberUtils.greateeZore(values);
    }

    /**
     * 将需要的异常拉出来, 放过其它异常
     *
     * @param bindingResult bindingResult
     * @param fields 需要的值的field
     * @return
     */
    public static List<ObjectError> process(BindingResult bindingResult, String... fields) {
        List<ObjectError> objectErrors = new ArrayList<>(10);
        Arrays.stream(fields).forEach(f -> {
            objectErrors.addAll(bindingResult.getFieldErrors(f));
        });
       return objectErrors;
    }

}
