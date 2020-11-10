package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.xy.tigger.shopvip.entity.AddressEntity;
import com.xy.tigger.shopvip.entity.OrganEntity;
import com.xy.tigger.shopvip.entity.ShoppingTrolley;
import com.xy.tigger.shopvip.entity.vo.OrderVo;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.uitls.R;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:14
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R productOrder(List<ShoppingTrolley> products, AddressEntity address, BigDecimal point, BigDecimal total, String openid,
                   HttpServletRequest request);

    //支付进行回调
    String payBack(String resXml);


    //查询订单
    R selectOrder(Integer id);


}

