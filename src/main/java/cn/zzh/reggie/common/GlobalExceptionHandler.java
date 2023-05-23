package cn.zzh.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/*
    全局异常处理器
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @param E
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException E){
        log.info(E.getLocalizedMessage());

        if(E.getMessage().contains("Duplicate entry")){
            String[] spilt = E.getMessage().split(" ");
            String msg = spilt[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }
    /**
     * 异常处理方法
     * @param E
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException E){
        log.info(E.getLocalizedMessage());
        return R.error(E.getMessage());
    }
}
