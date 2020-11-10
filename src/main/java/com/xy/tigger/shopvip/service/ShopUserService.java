package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.shopvip.entity.ShopUserEntity;
import com.xy.tigger.uitls.R;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:13
 */
public interface ShopUserService extends IService<ShopUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    ShopUserEntity userLogin(ShopUserVip shopUserVip, HttpServletRequest request);

    Map<String, String> becamevip(ShopUserEntity usr,HttpServletRequest request);

    String payback(String resXml);

    R topUp(OrderEntity orderEntity,HttpServletRequest request);

    String topuppaycall(String resXml);
}

