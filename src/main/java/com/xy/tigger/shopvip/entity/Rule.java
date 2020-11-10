package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("ps_rule")
public class Rule {

    private Integer id;

    private String ruleName;


    private BigDecimal parentPoint;

    private  BigDecimal point;

    private Integer showStatus;
}
