package com.xy.tigger.uitls.JSR;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class MobileValidator  implements ConstraintValidator<Mobile, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(value))

            return true;

        //返回匹配结果
        return mobilePattern.matcher(value).matches();
    }

    private String mobileReg = "^1(3|4|5|6|7|8|9)\\d{9}$";

    private Pattern mobilePattern = Pattern.compile(mobileReg);

    public void initialize(Mobile mobile) {

    }
}
