package com.xy.tigger.shopvip.entity.vo;

import com.xy.tigger.shopvip.entity.OrganEntity;
import lombok.Data;

import java.util.List;

@Data
public class OragnVo extends OrganEntity {


    private String image;
    private List<String> name;

    private String coverSrc;
}
