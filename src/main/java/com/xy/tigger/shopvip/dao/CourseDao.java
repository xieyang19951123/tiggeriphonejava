package com.xy.tigger.shopvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.CourseEntity;
import com.xy.tigger.shopvip.entity.vo.CourseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseDao extends BaseMapper<CourseEntity> {
    List<CourseVo> getCourseRecommend(@Param("id")Integer id,@Param("oid")Integer oid,@Param("val")String val);
}
