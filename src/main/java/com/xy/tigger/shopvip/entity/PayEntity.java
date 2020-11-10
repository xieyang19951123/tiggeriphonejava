package com.xy.tigger.shopvip.entity;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Data
public class PayEntity {

    private String openId;

    private String title;

    private BigDecimal money;

    private String notifyUrl;

    private HttpServletRequest httpServletRequest;


    private String order;

}
