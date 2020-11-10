package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ps_child")
public class Child {

    private Integer id;

    private String zhName;

    private String enName;

    private String birth;
    private String interest;
    private String spec;
    private String dream;
    private String iphone;
    private String sponsor;
    private String school;
    private String heathl;
    private String kinkUp;

    private Integer uid;

    @TableLogic(value = "1",delval = "0")
    private Integer showStatus;


}
