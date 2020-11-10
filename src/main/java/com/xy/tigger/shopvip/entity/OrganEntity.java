package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ps_organ")
public class OrganEntity {

    private Integer id;

    private String  username;

    private String password;

    private String icon;

    private String loginname;

    private String synopsis;

    @TableLogic(value = "1",delval = "0")
    private Integer showStatus;
    private Integer imageId;

    @TableField(exist = false)
    private Integer[] cids;

    private String cid;

    private Integer homeShow;

    private Integer recommend;



}
