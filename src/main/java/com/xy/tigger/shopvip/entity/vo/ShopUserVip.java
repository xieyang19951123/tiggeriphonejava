package com.xy.tigger.shopvip.entity.vo;

import com.xy.tigger.shopvip.entity.ShopUserEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopUserVip extends ShopUserEntity {

    private String openId; //用户openID

    private String code;//用于校验的code

    private BigDecimal paymoney;

}
