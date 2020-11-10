package com.xy.tigger.shopvip.entity.vo;

import com.xy.tigger.shopvip.entity.CommentEntity;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo extends CommentEntity {


    private String aSrc;

    private String coverSrc;

    private List<String> name;

    private List<String> accessoryEntities;
}
