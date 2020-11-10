package com.xy.tigger.shopvip.dao;

import com.xy.tigger.shopvip.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:14
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

   List<OrderVo> selectOrder(@Param("id") Integer id,@Param("ids") String ids);


	
}
