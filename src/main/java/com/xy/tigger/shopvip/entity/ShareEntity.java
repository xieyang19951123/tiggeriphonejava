package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ps_share")
public class ShareEntity {

    private Integer id;

    private Integer uid;

    private Integer remmondid;

    private Integer productid;

    private Integer showStatus;

    private Integer shareType;

}
