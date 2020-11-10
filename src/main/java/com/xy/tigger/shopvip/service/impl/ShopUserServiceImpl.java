package com.xy.tigger.shopvip.service.impl;

import com.xy.tigger.shopvip.dao.*;
import com.xy.tigger.shopvip.entity.*;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.uitls.*;
import com.xy.tigger.uitls.wxpay.TransOrder;
import com.xy.tigger.uitls.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xy.tigger.shopvip.service.ShopUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Service("shopUserService")
public class ShopUserServiceImpl extends ServiceImpl<ShopUserDao, ShopUserEntity> implements ShopUserService {

    @Autowired
    private ShopUserDao shopUserDao;

    @Autowired
    private WXPayUtils wxPayUtils;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RuleDao ruleDao;

    @Autowired
    private PayVideoDao payVideoDao;

    @Autowired
    private UserVideoDao userVideoDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopUserEntity> page = this.page(
                new Query<ShopUserEntity>().getPage(params),
                new QueryWrapper<ShopUserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public ShopUserEntity userLogin(ShopUserVip shopUserVip, HttpServletRequest request) {

         //用户进行登录验证
        String md5Str = MD5utils.getMD5Str(shopUserVip.getPsPassword());
        ShopUserEntity shopUserEntity = new ShopUserEntity();
        shopUserEntity.setPsUsername(shopUserVip.getPsUsername());
        shopUserEntity.setPsPassword(md5Str);
        shopUserEntity.setUserRock(1);
        System.out.println(md5Str);
        ShopUserEntity shopUserEntityDao = baseMapper.selectOne(new QueryWrapper<ShopUserEntity>(shopUserEntity));
        if(shopUserEntityDao != null){
                shopUserEntityDao.setOpenId(shopUserVip.getOpenId());
                shopUserDao.updateOpenId(shopUserEntityDao);
            return shopUserEntityDao;
        }


        return null;
    }

    @Override
    public Map<String, String> becamevip(ShopUserEntity usr,HttpServletRequest request) {
        Integer shopUserEntities = shopUserDao.selectListVip();
        BigDecimal paymoeny = null;
        if(!(shopUserEntities <200) ){
            paymoeny = new BigDecimal(200);
        }else {
            paymoeny = new BigDecimal(100);

        }
        PayEntity payEntity = new PayEntity();
        String order = TransOrder.getOrder();
        payEntity.setTitle("成为会员");
        payEntity.setOrder(order);
        payEntity.setOpenId(usr.getOpenId());
        payEntity.setMoney(paymoeny);
        payEntity.setHttpServletRequest(request);
//        payEntity.setNotifyUrl("http://106.75.162.179:8081/shopvip/shopuser/payCallback");
        payEntity.setNotifyUrl("http://object.ngrok2.xiaomiqiu.cn/shopvip/shopuser/payCallback");
        Map<String, String> stringStringMap = wxPayUtils.payMoney(payEntity);
        if(stringStringMap!=null){
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setCId(0);
            orderEntity.setNumbers(0);
            orderEntity.setOCode(order);
            orderEntity.setOMoney(paymoeny);
            orderEntity.setPayStatus(1);
            orderEntity.setUId(usr.getId());
            orderEntity.setPayType(1);
            orderEntity.setShowStatus(1);
            orderEntity.setCreatTime(new Date());
            orderDao.insert(orderEntity);
        }
        return stringStringMap;
    }

    @Override
    public String payback(String resXml) {
        Map<String, String> stringStringMap = wxPayUtils.payBack(resXml);
        String out_trade_no = stringStringMap.get("out_trade_no");
        if(StringUtils.isEmpty(out_trade_no)){
            return stringStringMap.get("xmlBack");
        }
        //查询订单
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOCode(out_trade_no);
        orderEntity.setShowStatus(1);
        OrderEntity orderEntity1 = orderDao.selectOne(new QueryWrapper<OrderEntity>(orderEntity));
        orderEntity1.setPayType(2);
        ShopUserEntity shopUserEntity = shopUserDao.selectById(orderEntity1.getUId());
        //shopUserEntity.setShopVip(1);
        shopUserEntity.setShopVip(1);
        shopUserEntity.setExpireTime(new Date());
        shopUserDao.updateById(shopUserEntity);
        Rule rule = ruleDao.selectById(1);
        //查询父id
        ShopUserEntity parentShopUser = shopUserDao.selectById(shopUserEntity.getParentId());
        if(parentShopUser != null){
            parentShopUser.setPoint(parentShopUser.getPoint().add(rule.getParentPoint()).setScale(2));
            shopUserDao.updateById(parentShopUser);//更新父
        }
        shopUserEntity.setPoint(shopUserEntity.getPoint().add(rule.getPoint()).setScale(2));
        shopUserDao.updateById(shopUserEntity);//更新自己

        //查询视频
        PayVideoEntity payVideoEntity = payVideoDao.selectById(orderEntity1.getCId());
        UserVideo userVideo = new UserVideo();
        userVideo.setVideoId(orderEntity1.getCId());
        userVideo.setOId(payVideoEntity.getOid());
        userVideo.setUId(shopUserEntity.getId());
        if(orderEntity1.getPayType() ==4){
            userVideo.setOType(2);
        }else {
            userVideo.setOType(1);
        }
        userVideo.setShowStatus(1);
        userVideoDao.insert(userVideo);
        return stringStringMap.get("xmlBack");
    }

    @Override
    public R topUp(OrderEntity orderEntity,HttpServletRequest request) {
        ShopUserEntity shopUserEntity = shopUserDao.selectById(orderEntity.getUId());
        String openId = shopUserEntity.getOpenId();

        String ordercode = TransOrder.getOrder();
        PayEntity payEntity = new PayEntity();
        payEntity.setHttpServletRequest(request);
        payEntity.setMoney(orderEntity.getPRealMoney());
        payEntity.setOpenId(openId);
        payEntity.setOrder(ordercode);
        payEntity.setTitle("用户充值");
        payEntity.setNotifyUrl("http://106.75.162.179:8081/shopvip/shopuser/topupaycall");
        Map<String, String> resultMap = wxPayUtils.payMoney(payEntity);
        if(resultMap == null){
            return  R.error(1001,"下单失败");
        }
        orderEntity.setOCode(ordercode);
        orderEntity.setCreatTime(new Date());
        orderEntity.setPostage(new BigDecimal(0));
        orderDao.insert(orderEntity);

        return R.ok().put("page",resultMap);
    }

    @Override
    public String topuppaycall(String resXml) {
        Map<String, String> stringStringMap = wxPayUtils.payBack(resXml);
        String out_trade_no = stringStringMap.get("out_trade_no");
        if(StringUtils.isEmpty(out_trade_no)){
            return stringStringMap.get("xmlBack");
        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOCode(out_trade_no);
        orderEntity.setShowStatus(1);
        OrderEntity orderEntity1 = orderDao.selectOne(new QueryWrapper<OrderEntity>(orderEntity));
        orderEntity1.setPayType(2);
        ShopUserEntity shopUserEntity = shopUserDao.selectById(orderEntity1.getUId());
        shopUserEntity.getBalance().add(orderEntity1.getPRealMoney());
        shopUserDao.updateById(shopUserEntity);

        return stringStringMap.get("xmlBack");
    }


}