package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("ps_shopping_trolley")
public class ShoppingTrolley {

    private Integer productId;//商品名

    //private String coverSrc;//封面

    private BigDecimal price;//单价

    private Integer total;//数量

    @TableLogic(value = "1",delval = "0")
    private Integer showStatus;


    private Integer uId;

    private Integer id;

    @TableField(exist = false)
    private BigDecimal postage;
    //private BigDecimal


}
