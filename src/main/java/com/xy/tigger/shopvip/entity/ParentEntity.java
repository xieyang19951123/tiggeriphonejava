package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ps_parent")
public class ParentEntity {

    private  Integer id;

    private  String childs;

    private String pname;

    private Integer page;

    private String addresswork;

    private String piphone;

    private String techang;

    private String partjob;

    @TableLogic(value = "1",delval = "0")
    private Integer showStatus;

    private String intersing;

    private Integer uid;
}
