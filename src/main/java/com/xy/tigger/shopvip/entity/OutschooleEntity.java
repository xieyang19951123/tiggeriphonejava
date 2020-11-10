package com.xy.tigger.shopvip.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ps_outschoole")
public class OutschooleEntity {

    private  Integer id;

    private String cName;

    private String classTime;

    private String teaching;

    private String tSystem;

    private String oName;

    private String teacher;

    private String startTime;

    private String endTime;

    @TableLogic(value = "1",delval = "0")
    private  Integer showStatus;

    private  Integer cId;
}
