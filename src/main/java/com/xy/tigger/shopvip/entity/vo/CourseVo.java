package com.xy.tigger.shopvip.entity.vo;

import com.xy.tigger.shopvip.entity.CourseEntity;
import lombok.Data;

import java.util.List;

@Data
public class CourseVo extends CourseEntity {

    private String videoName;

    private String username;

    private String coverSrc;

    private String aSrc;

    private List<String> name;
}
