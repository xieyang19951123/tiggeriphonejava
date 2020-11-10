package com.xy.tigger.shopvip.dao;

import com.xy.tigger.shopvip.entity.ShopUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:13
 */
@Mapper
public interface ShopUserDao extends BaseMapper<ShopUserEntity> {
    /**
     * 支付成功修改状态
     */

    void updateStatus(Integer id);


    Integer selectListVip();

    void updateOpenId(ShopUserEntity shopUserEntity);



}
