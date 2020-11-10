package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.dao.ShopUserDao;
import com.xy.tigger.shopvip.entity.ShopUserEntity;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.shopvip.service.ConsumerService;
import com.xy.tigger.uitls.HttpGetAndPost;
import com.xy.tigger.uitls.MD5utils;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.R;
import com.xy.tigger.uitls.wxpay.TransOrder;
import com.xy.tigger.uitls.wxpay.sdk.MyWXpay;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("consumerService")
public class ConsumerServiceImp implements ConsumerService {

    @Autowired
    private ShopUserDao shopUserDao;

    @Autowired
    private MyWXpay myWXpay;

    @Override
    public R regist(ShopUserVip shopUserVip, HttpServletRequest request) {
        //进行用户注册
        if (StringUtils.isEmpty(shopUserVip.getCode())) {
            return R.error(10001,"校验code为空");
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("appid","wx601b22b03c6ca4ed");
        param.put("secret","ab9a133c6ebfc4c9d985c9c07790ef7a");
        param.put("code",shopUserVip.getCode());
        param.put("grant_type","authorization_code");
        Map<String,String> openId = HttpGetAndPost.doGet(param);
        System.out.println(openId);
        log.info(openId.get("openid"));
        if(StringUtils.isEmpty(openId.get("openid"))){
            return R.error(10001,"用户未授权");
        }
        shopUserVip.setOpenId(openId.get("openid"));
        ShopUserEntity qw = new ShopUserEntity();
        qw.setIphone(shopUserVip.getIphone());
        ShopUserEntity shopUserEntity1 = selectOneShopUser(qw);
        //查询已支付成功的人数

        Integer shopUserEntities = shopUserDao.selectListVip();
        BigDecimal paymoeny = null;
        if(!(shopUserEntities <200) ){
            paymoeny = new BigDecimal(200);
        }else {
            paymoeny = new BigDecimal(100);

        }
        //手机是否已经注册
        if(shopUserEntity1 != null){
            if(shopUserEntity1.getShopVip() == 1){
                return R.error(10002,"该手机已经注册会员");
            }else{
                String recommend = dowhileRecommend();
                shopUserVip.setId(shopUserEntity1.getId());
                shopUserVip.setPsPassword(MD5utils.getMD5Str(shopUserVip.getPsPassword()));
                shopUserVip.setRecommend(recommend);
                shopUserVip.setShopVip(0);
                shopUserVip.setShowStatus(1);
                shopUserVip.setPaymoney(paymoeny);
                shopUserDao.updateById(shopUserVip);
                return R.error(10003,"该手机已经注册,但未支付").put("data",myWXpay.placeOrder(shopUserVip, request)).put("recommd",recommend).put("paymoney",paymoeny);
            }
        }
        qw.setPsUsername(shopUserVip.getIphone());
        qw.setBalance(new BigDecimal(0));
        qw.setCreateTime(new Date());
        qw.setPoint(new BigDecimal(0));
        qw.setPsPassword(shopUserVip.getPsPassword());
        //推荐码是否为空
        if(!StringUtils.isEmpty(shopUserVip.getRecommend())){
            ShopUserEntity selectEntity = new ShopUserEntity();
            selectEntity.setRecommend(shopUserVip.getRecommend());
            ShopUserEntity shopUserEntity = selectOneShopUser(selectEntity);
            if(shopUserEntity != null ){
                qw.setParentId(shopUserEntity.getId());//获取推荐人id
            }
        }
        String recommend = dowhileRecommend();
        qw.setRecommend(recommend);
        qw.setShopVip(0);
        qw.setShowStatus(1);
        qw.setPsPassword(MD5utils.getMD5Str(qw.getPsPassword()));
        shopUserDao.insert(qw);
        ShopUserEntity shopUserEntity = selectOneShopUser(qw);
        shopUserVip.setId(qw.getId());
        shopUserVip.setPaymoney(paymoeny);

        //支付
        R stringStringMap = myWXpay.placeOrder(shopUserVip, request);

        return stringStringMap.put("recommd",recommend).put("paymoney",paymoeny);
    }




    //条件查询
    public ShopUserEntity selectOneShopUser(ShopUserEntity selectEntity){
        QueryWrapper<ShopUserEntity> selectWrapper = new QueryWrapper<>(selectEntity);
        ShopUserEntity shopUserEntity = shopUserDao.selectOne(selectWrapper);
        return shopUserEntity;
    }

    //递归查询推荐是否已经存在
    public String dowhileRecommend(){
        String recommend = RandomStringUtils.randomAlphanumeric(6);
        ShopUserEntity shopUserEntity = new ShopUserEntity();
        shopUserEntity.setRecommend(recommend);
        QueryWrapper<ShopUserEntity> shopUserEntityQueryWrapper = new QueryWrapper<>(shopUserEntity);
        ShopUserEntity shopUserEntity1 = shopUserDao.selectOne(shopUserEntityQueryWrapper);
        if(shopUserEntity1!=null){
            dowhileRecommend();
        }
        return  recommend;
    }

}
