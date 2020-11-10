package com.xy.tigger.shopvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.shopvip.entity.ProductsOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductOrderDao extends BaseMapper<ProductsOrder> {

    List<OrderEntity> selectOrders(String ids);
}
