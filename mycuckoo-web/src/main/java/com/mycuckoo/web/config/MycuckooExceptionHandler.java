package com.mycuckoo.web.config;

import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 功能说明: 全局异常处理
 *
 * @author rutine
 * @version 3.0.0
 * @time 2017-08-27 10:29
 */
@RestControllerAdvice
public class MycuckooExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(MycuckooExceptionHandler.class);

    @ExceptionHandler(value = {ApplicationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResponse<String> applicationException(ApplicationException ex) {
        logger.error("应用异常", ex);

        return AjaxResponse.create(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = {SystemException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResponse<String> systemException(SystemException ex) {
        logger.info("系统异常", ex);

        return AjaxResponse.create(500, ex.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResponse<String> illegalArgumentException(IllegalArgumentException ex) {
        logger.info("非法参数", ex);

        return AjaxResponse.create(500, ex.getMessage());
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResponse<String> illegalStateException(IllegalStateException ex) {
        logger.info("非法数据状态", ex);

        return AjaxResponse.create(500, ex.getMessage());
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResponse<String> unknownException(Exception ex) {
        logger.info("全局异常处理", ex);

        return AjaxResponse.create(500, "系统异常");
    }
}
