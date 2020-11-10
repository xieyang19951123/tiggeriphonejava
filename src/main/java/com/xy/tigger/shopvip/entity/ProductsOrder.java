package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xy.tigger.shopvip.entity.vo.OrderVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@TableName("ps_products_order")
public class ProductsOrder {

    private Integer id;

    private String orderCode;

    private BigDecimal pMoney;

    private String orderId;

    private BigDecimal point;

    private BigDecimal pRealMoney;

    private Integer showStatus;

    private Integer payStatus;

    private Integer uid;

    private BigDecimal postage;

    @TableField(exist = false)
    private List<OrderVo> orderVos;
}
