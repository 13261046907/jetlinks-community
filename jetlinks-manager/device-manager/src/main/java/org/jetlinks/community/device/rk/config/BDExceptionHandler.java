package org.jetlinks.community.device.rk.config;

import org.jetlinks.community.device.rk.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class BDExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public BDExceptionHandler() {
    }

    @ExceptionHandler({Exception.class})
    public Object handleException(Exception e, HttpServletRequest request) {
        this.logger.info("报错请求地址 : " + request.getRequestURL().toString(), e);
        return R.error(1003, "系统异常", e.getMessage());
    }
}
