package com.xy.tigger.uitls.exception;

import com.xy.tigger.shopvip.entity.eum.BizCodeEnums;
import com.xy.tigger.uitls.R;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice("com.xy.tigger.shopvip.controller")
public class TiggerlExceptionControllerAdvice {
    @ExceptionHandler(value = Exception.class)
    public R handleValidException(MethodArgumentNotValidException e){
        log.error("数据校验错误:{}，异常类型:{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String,String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach(item->{
            String defaultMessage = item.getDefaultMessage();
            String field = item.getField();
            map.put(field,defaultMessage);
        });
        return R.error(BizCodeEnums.VALID_EXCEPTION.getCode(),BizCodeEnums.VALID_EXCEPTION.getMsg()).put("data",map);
    }


    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        //异常状态码，5位数字
        //前两位位业务场景，后三位表示错误码  10001  10通用  001参数格式异常
        //11 商品 12订单  13 购物车 14 物流
        return R.error(BizCodeEnums.UNKNOWN_EXCEPTION.getCode(),BizCodeEnums.UNKNOWN_EXCEPTION.getMsg());
    }
}
