package com.xy.tigger.shopvip.entity.vo;

import com.xy.tigger.shopvip.entity.OrderEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVo extends OrderEntity {

    private String introduce;

    private BigDecimal price;
}
