package com.xy.tigger.shopvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.TryVideoEntity;
import com.xy.tigger.shopvip.entity.vo.TryVideoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TryVideoDao extends BaseMapper<TryVideoEntity> {


    List<TryVideoVo> getVideo();

    List<TryVideoVo> getVideoRelevant();

    List<TryVideoVo> getVideoAll(@Param("val") String val);

}
