package com.xy.tigger.shopvip.entity.vo;

import com.xy.tigger.shopvip.entity.PayVideoEntity;
import lombok.Data;

import java.util.List;

@Data
public class PayVideoVo extends PayVideoEntity {

    private String aSrc;

    private List<String> name;

    private String coverSrc;



    private String organname;
}
