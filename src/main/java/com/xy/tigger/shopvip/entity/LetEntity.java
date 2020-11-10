package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("ps_let")
@Data
public class LetEntity {

    private Integer id;


    private String val;
}
