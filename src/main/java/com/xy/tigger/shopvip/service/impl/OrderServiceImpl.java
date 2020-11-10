package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.xy.tigger.shopvip.dao.*;
import com.xy.tigger.shopvip.entity.*;
import com.xy.tigger.shopvip.entity.vo.OrderVo;
import com.xy.tigger.uitls.R;
import com.xy.tigger.uitls.WXPayUtils;
import com.xy.tigger.uitls.wxpay.TransOrder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.Query;

import com.xy.tigger.shopvip.service.OrderService;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private WXPayUtils wxPayUtils;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductOrderDao productOrderDao;

    @Autowired
    private ShoppingTrolleyDao shoppingTrolleyDao;

    @Autowired
    private ShopUserDao shopUserDao;

    @Autowired
    private RuleDao ruleDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ShareDao shareDao;

    @Autowired
    private PointRecordDao pointRecordDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R productOrder(List<ShoppingTrolley> products,
                          AddressEntity address,
                          BigDecimal point ,
                          BigDecimal total,
                          String openid,
                          HttpServletRequest request) {
        String ordercode = TransOrder.getOrder();
        PayEntity payEntity = new PayEntity();
        payEntity.setHttpServletRequest(request);
        payEntity.setMoney(total);
        payEntity.setOpenId(openid);
        payEntity.setOrder(ordercode);
        payEntity.setTitle("商品支付");
        //payEntity.setNotifyUrl("http://106.75.162.179:8081/shopvip/order/payCallback");
        payEntity.setNotifyUrl("http://object.ngrok2.xiaomiqiu.cn/shopvip/order/payCallback");
        Map<String, String> resultMap = wxPayUtils.payMoney(payEntity);
        ShoppingTrolley shoppingTrolley = new ShoppingTrolley();

        if(resultMap == null){
            return R.error(4001,"下单失败");
        }
        ProductsOrder productsOrder = new ProductsOrder();
        List<Integer> ids = new ArrayList<>();
        List<Integer> shoppingids = new ArrayList<>();
        products.stream().forEach(item->{
            shoppingTrolley.setUId(address.getUId());
            shoppingTrolley.setProductId(item.getId());
            shoppingTrolley.setShowStatus(1);
            ShoppingTrolley shoppingTrolley1 = shoppingTrolleyDao.selectOne(new QueryWrapper<>(shoppingTrolley));

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setPayType(2);
            orderEntity.setOCode(ordercode);
            orderEntity.setCreatTime(new Date());
            orderEntity.setNumbers(item.getTotal());
            orderEntity.setUId(address.getUId());
            orderEntity.setShowStatus(1);
            orderEntity.setCId(item.getId());
            orderEntity.setOMoney(item.getPrice().multiply(new BigDecimal(item.getTotal())));
            orderEntity.setUAddress(address.getId());
            orderEntity.setPayStatus(1);
            orderEntity.setCId(item.getId());
            orderEntity.setPostage(item.getPostage());
            orderDao.insert(orderEntity);
            ids.add(orderEntity.getId());
            if(shoppingTrolley1 != null){

                shoppingids.add(shoppingTrolley1.getId());
            }

        });
        if(shoppingids.size()>0){

            shoppingTrolleyDao.deleteBatchIds(shoppingids);
        }
        productsOrder.setShowStatus(1);
        productsOrder.setOrderCode(ordercode);
        productsOrder.setOrderId(ids.toString());
        productsOrder.setPMoney(total);
        productsOrder.setPoint(point);
        productsOrder.setPayStatus(1);
        productsOrder.setUid(address.getUId());
        productsOrder.setPRealMoney(total.add(point).setScale(2, BigDecimal.ROUND_HALF_UP));
        productOrderDao.insert(productsOrder);
        return R.ok().put("page",resultMap);
    }

    @Override
    public String payBack(String resXml) {
        Map<String, String> stringStringMap = wxPayUtils.payBack(resXml);
        String out_trade_no = stringStringMap.get("out_trade_no");
        if(StringUtils.isEmpty(out_trade_no)){
            return stringStringMap.get("xmlBack");
        }
        //根据订单号查询订单
        ProductsOrder productsOrder = new ProductsOrder();
        productsOrder.setOrderCode(out_trade_no);
        productsOrder.setShowStatus(1);
        ProductsOrder order1 = productOrderDao.selectOne(new QueryWrapper<ProductsOrder>(productsOrder));//获取订单
        if(order1 == null){
            return stringStringMap.get("xmlBack");
        }
        order1.setPayStatus(2);
        productOrderDao.updateById(order1);
        //获取用户id
        Integer uid = order1.getUid();
        ShopUserEntity shopUserEntity = shopUserDao.selectById(uid);
        //查询规则
        //Rule rule = ruleDao.selectById(2);




        String orderId = order1.getOrderId();
        String ids = orderId.replaceAll("\\[","(").replaceAll("]",")");

        List<OrderEntity> orderEntities = productOrderDao.selectOrders(ids);
        orderEntities.forEach(item->{
            item.setPayStatus(2);
            orderDao.updateById(item);
            CommentEntity commentEntity = commentDao.selectById(item.getCId());
            ShareEntity shareEntity = new ShareEntity();
            shareEntity.setUid(shopUserEntity.getId());
            shareEntity.setProductid(item.getCId());
            ShareEntity shareEntity1 = shareDao.selectOne(new QueryWrapper<>(shareEntity));
            shopUserEntity.setPoint(shopUserEntity.getPoint().add(new BigDecimal(commentEntity.getByPoint())).subtract(order1.getPoint()).setScale(2,BigDecimal.ROUND_DOWN));
            shopUserDao.updateById(shopUserEntity);//更新自己
            ShopUserEntity parentShopUser = shopUserDao.selectById(shopUserEntity.getParentId());
            if(parentShopUser!=null){
                parentShopUser.setPoint(parentShopUser.getPoint().add(new BigDecimal(commentEntity.getByPointFather())).setScale(2));
                PointRecordEntity pointRecordEntity = new PointRecordEntity();
                pointRecordEntity.setPonit(parentShopUser.getPoint());
                pointRecordEntity.setMessage("下级用户购买商品得"+commentEntity.getByPointFather()+"积分");
                pointRecordEntity.setCreateTime(new Date());
                pointRecordEntity.setUid(parentShopUser.getId());
                pointRecordEntity.setShowStatus(1);
                pointRecordDao.insert(pointRecordEntity);
                shopUserDao.updateById(parentShopUser);}
            else if(shareEntity1 !=null&& parentShopUser == null){
                ShopUserEntity shopUserEntity1 = shopUserDao.selectById(shareEntity1.getRemmondid());
                shopUserEntity1.setPoint(shopUserEntity1.getPoint().add(new BigDecimal(commentEntity.getSharePoint())).setScale(2));
                shopUserDao.updateById(shopUserEntity1);
                PointRecordEntity pointRecordEntity = new PointRecordEntity();
                pointRecordEntity.setUid(shareEntity1.getId());
                pointRecordEntity.setPonit(shopUserEntity1.getPoint());
                pointRecordEntity.setMessage("分享商品得"+commentEntity.getSharePoint()+"积分");
                pointRecordEntity.setCreateTime(new Date());
                pointRecordEntity.setShowStatus(1);
                pointRecordDao.insert(pointRecordEntity);
            }
        });

        return stringStringMap.get("xmlBack");
    }

    @Override
    public R selectOrder(Integer id) {
        List<OrderVo> orderVo = orderDao.selectOrder(id,null);
        ProductsOrder productsOrder = new ProductsOrder();
        productsOrder.setUid(id);
        productsOrder.setPayStatus(2);
        List<ProductsOrder> productsOrders = productOrderDao.selectList(new QueryWrapper<>(productsOrder));
        List<ProductsOrder> collect = productsOrders.stream().map(item -> {
            String ids = item.getOrderId().replaceAll("\\[", "(").replaceAll("]", ")");
            List<OrderVo> porderVos = orderDao.selectOrder(id, ids);
            item.setOrderVos(porderVos);
            return item;
        }).collect(Collectors.toList());
        return R.ok().put("orderVo",orderVo).put("productsOrders",productsOrders);
    }


}