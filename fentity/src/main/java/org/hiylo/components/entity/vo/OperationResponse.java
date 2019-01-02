/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: OperationResponse.java
 * Data: 18-4-27 上午11:41
 * Author: hiylo
 *
 *
 */

/**
 * This is the common structure for all responses
 * if the response contains a list(array) then it will have 'items' field
 * if the response contains a single item then it will have 'item'  field
 */


package org.hiylo.components.entity.vo;

import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @author hiylo
 * @date 2018年4月10日 11:13:23
 */
public class OperationResponse {

    public enum ResponseStatusEnum {SUCCESS, ERROR, WARNING, NO_ACCESS}

    ;
    private ResponseStatusEnum operationStatus;
    private String operationMessage;
    public List<ObjectError> allErrors;
    private Object data;

    public OperationResponse() {
    }

    public OperationResponse(ResponseStatusEnum operationStatus, String operationMessage) {
        this.operationStatus = operationStatus;
        this.operationMessage = operationMessage;
    }

    public OperationResponse(ResponseStatusEnum operationStatus, String operationMessage, List<ObjectError> allErrors) {
        this.operationStatus = operationStatus;
        this.operationMessage = operationMessage;
        this.allErrors = allErrors;
    }

    public OperationResponse success(String message) {
        this.operationMessage = message;
        this.operationStatus = ResponseStatusEnum.SUCCESS;
        return this;
    }


    public OperationResponse error(String message, List<ObjectError> errors) {
        this.operationMessage = message;
        this.operationStatus = ResponseStatusEnum.ERROR;
        this.allErrors = errors;
        return this;
    }

    public ResponseStatusEnum getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(ResponseStatusEnum operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getOperationMessage() {
        return operationMessage;
    }

    public void setOperationMessage(String operationMessage) {
        this.operationMessage = operationMessage;
    }

    public List<ObjectError> getAllErrors() {
        return allErrors;
    }

    public void setAllErrors(List<ObjectError> allErrors) {
        this.allErrors = allErrors;
    }

    public Object getData() {
        return data;
    }

    public OperationResponse setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "OperationResponse{" +
                "operationStatus=" + operationStatus +
                ", operationMessage='" + operationMessage + '\'' +
                ", allErrors=" + allErrors +
                ", data=" + data +
                '}';
    }
}
