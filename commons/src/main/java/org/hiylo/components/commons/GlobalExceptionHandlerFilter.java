package org.hiylo.components.commons;

import org.hiylo.components.entity.vo.OperationResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_EXTENDED;

//@ControllerAdvice
public class GlobalExceptionHandlerFilter extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        OperationResponse operationResponse = new OperationResponse();
        List<ObjectError> allErrors = new ArrayList<>();
        ObjectError objectError = new ObjectError("接口名：",request.getDescription(false).toString());
        allErrors.add(objectError);
        objectError = new ObjectError(e.getClass().getName(),e.getMessage());
        allErrors.add(objectError);

        StackTraceElement[] error = e.getStackTrace();
        String stackTraceStr = "";
        for (StackTraceElement stackTraceElement : error) {
            stackTraceStr=stackTraceStr+"\n"+stackTraceElement.toString();
        }
        objectError = new ObjectError(e.getClass().getName(),stackTraceStr);
        allErrors.add(objectError);
        operationResponse.setAllErrors(allErrors);
        operationResponse.setOperationMessage("发生异常！");
        operationResponse.setOperationStatus(OperationResponse.ResponseStatusEnum.ERROR);

        printLog(e, request);
        return new ResponseEntity<Object>(operationResponse, NOT_EXTENDED);

    }

    private void printLog(Exception ex, WebRequest request) {
//        log.error("************************异常开始*******************************");
//        log.error(ex.toString());
//        log.error("请求地址：" + request.getContextPath());
        Iterator<String> iterator = request.getParameterNames();
//        log.error("请求参数");
        while (iterator.hasNext()) {
            String name = iterator.next().toString();
//            log.error(name + "---" + request.getParameter(name));
        }

        StackTraceElement[] error = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : error) {
//            log.error(stackTraceElement.toString());
        }
//        log.error("************************异常结束*******************************");
    }



    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public OperationResponse jsonHandler(HttpServletRequest request, Exception e) throws Exception {
        OperationResponse operationResponse = new OperationResponse();
        List<ObjectError> allErrors = new ArrayList<>();
        ObjectError objectError = new ObjectError("接口名：",request.getRequestURL().toString());
        allErrors.add(objectError);
        objectError = new ObjectError(e.getClass().getName(),e.getMessage());
        allErrors.add(objectError);
        objectError = new ObjectError(e.getClass().getName(),e.getLocalizedMessage());
        allErrors.add(objectError);
        objectError = new ObjectError(e.getClass().getName(),e.getStackTrace().toString());
        allErrors.add(objectError);
        operationResponse.setAllErrors(allErrors);
        operationResponse.setOperationMessage("发生异常！");
        operationResponse.setOperationStatus(OperationResponse.ResponseStatusEnum.ERROR);

        printLog(e, request);
        return operationResponse;
    }

    private void printLog(Exception ex, HttpServletRequest request) {
//        log.error("************************异常开始*******************************");
//        log.error(ex.toString());
//        log.error("请求地址：" + request.getRequestURL());
        Enumeration<String> enumeration = request.getParameterNames();
//        log.error("请求参数");
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
//            log.error(name + "---" + request.getParameter(name));
        }

        StackTraceElement[] error = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : error) {
//            log.error(stackTraceElement.toString());
        }
//        log.error("************************异常结束*******************************");
    }
}
