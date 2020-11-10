package com.xy.tigger.shopvip.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComputersPoint {

    private BigDecimal userpoint;

    private BigDecimal point;

    private BigDecimal price;

    private BigDecimal balance;
}
