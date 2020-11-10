package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("ps_point_record")
public class PointRecordEntity {

    private Integer id;

    private Integer uid;

    private BigDecimal ponit;

    private Integer showStatus;

    private String message;

    private Date createTime;
}
