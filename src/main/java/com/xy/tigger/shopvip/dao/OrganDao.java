package com.xy.tigger.shopvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.OrganEntity;
import com.xy.tigger.shopvip.entity.PayVideoEntity;
import com.xy.tigger.shopvip.entity.vo.OragnVo;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrganDao extends BaseMapper<OrganEntity> {

    List<PayVideoVo> selectPayvideoByoid(Integer oid);

    List<OragnVo> selectOrganList(@Param("oid") Integer oid,@Param("val")String val);
}
