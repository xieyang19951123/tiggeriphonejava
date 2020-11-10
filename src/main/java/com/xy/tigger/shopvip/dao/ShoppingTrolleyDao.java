package com.xy.tigger.shopvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.ShoppingTrolley;
import com.xy.tigger.shopvip.entity.vo.ShoppingTrolleyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingTrolleyDao extends BaseMapper<ShoppingTrolley> {
    List<ShoppingTrolleyVo> selectShoppings(Integer id);
}
