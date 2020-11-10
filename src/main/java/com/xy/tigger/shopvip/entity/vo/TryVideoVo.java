package com.xy.tigger.shopvip.entity.vo;

import com.xy.tigger.shopvip.entity.TryVideoEntity;
import lombok.Data;

import java.util.List;

@Data
public class TryVideoVo extends TryVideoEntity {

    private String aSrc ;

    private List<String> name;
    private String coverSrc;

    private String playAuth;

}
