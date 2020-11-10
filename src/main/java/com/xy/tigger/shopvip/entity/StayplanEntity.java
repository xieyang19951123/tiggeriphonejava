package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ps_stayplan")
public class StayplanEntity {

    private Integer id;

    private  String plantime;

    private String planname;

    private Integer page;

    private String kemu;

    private String target;

    private Integer uid;

    @TableLogic(value = "1",delval = "0")
    private Integer showStatus;
}
