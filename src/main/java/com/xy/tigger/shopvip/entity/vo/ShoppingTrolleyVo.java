package com.xy.tigger.shopvip.entity.vo;

import com.xy.tigger.shopvip.entity.ShoppingTrolley;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShoppingTrolleyVo extends ShoppingTrolley {

    private BigDecimal point;

    private String aSrc;

    private String coverSrc;

    private String comName;

    private BigDecimal postage;


}
