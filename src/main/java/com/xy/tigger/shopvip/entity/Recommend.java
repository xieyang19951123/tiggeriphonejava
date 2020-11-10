package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ps_recommend")
public class Recommend {

    private Integer id;

    private Integer courseid;

    private Integer uid;

    private Integer pid;

    private Integer showStatus;
}
