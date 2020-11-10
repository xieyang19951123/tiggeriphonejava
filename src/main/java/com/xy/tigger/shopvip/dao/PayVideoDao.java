package com.xy.tigger.shopvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.PayVideoEntity;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PayVideoDao extends BaseMapper<PayVideoEntity> {


    List<PayVideoVo> selectPayVideoVo();

    List<String> selectCategroy(String id);

    List<PayVideoVo> selectPayVideoList();

    List<PayVideoVo> selectPayVideomoduo();

    List<PayVideoVo> selectPayvideoByVal(@Param("val") String val);

    PayVideoVo getPayVideoByid(Integer id);

}
