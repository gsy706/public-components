/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: GlobalExceptionHandler.java
 * Data: 18-4-27 上午11:41
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.commons;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import net.sf.json.JSONObject;
import org.hiylo.components.entity.vo.OperationResponse;
import org.hiylo.components.exceptions.BaseException;
import org.hiylo.components.exceptions.CommonException;
import org.hiylo.components.exceptions.FileException;
import org.hiylo.components.exceptions.UserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

/**
 * @author hiylo
 * @date 2018年4月25日 13:51:57
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 所有异常报错
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public OperationResponse allExceptionHandler(Exception e) {
        e.printStackTrace();
        if (e instanceof HystrixRuntimeException) {
            HystrixRuntimeException cause = (HystrixRuntimeException) e;
            Exception exception = (Exception) cause.getCause();
            if (exception.getClass().getSuperclass().isInstance(new BaseException("", ""))) {
                return this.parseException(exception);
            }
        } else if (e.getClass().getSuperclass().isInstance(new BaseException("", ""))) {
            return this.parseException(e);
        }
        return new OperationResponse().error("服务器异常", null);
    }

    private OperationResponse parseException(Exception exception) {
        logger.info(exception.getMessage());
        exception.printStackTrace();
        JSONObject json = JSONObject.fromObject(exception.getMessage());
        return new OperationResponse().error(json.getString("retObject"), null);
    }
}
