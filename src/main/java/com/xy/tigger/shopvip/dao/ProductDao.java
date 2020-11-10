package com.xy.tigger.shopvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.CommentEntity;
import com.xy.tigger.shopvip.entity.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductDao extends BaseMapper<CommentEntity> {
    List<CommentVo> getProducts(@Param("show")Integer show,@Param("val")String val);

    List<String> getFjsrc(String id);
}
