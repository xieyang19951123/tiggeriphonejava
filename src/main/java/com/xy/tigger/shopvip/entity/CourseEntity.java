package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("ps_course")
public class CourseEntity {


    private Integer id;

    private String courseName;

    private String cid;

    @TableField(exist = false)
    private Integer[] cids;

    private Integer oid;

    private String introduce;

    private BigDecimal coursePrice;

    private Integer coursePoint;

    private Integer image;

    private Integer tryvideo;

    private Integer homeShow;

    private Integer recommend;

    @TableLogic(value = "1",delval = "0")
    private Integer showStatus;

    private Integer sharePoint;

    private Integer byPoint;

    private Integer byPointFather;

}
