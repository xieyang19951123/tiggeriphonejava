package com.xy.tigger.shopvip.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.dao.RuleDao;
import com.xy.tigger.shopvip.entity.*;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.shopvip.service.OrganService;
import com.xy.tigger.uitls.HttpGetAndPost;
import com.xy.tigger.uitls.MD5utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xy.tigger.shopvip.service.ShopUserService;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.R;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:13
 */
@RestController
@RequestMapping("shopvip/shopuser")
@Slf4j
public class ShopUserController {
    @Autowired
    private ShopUserService shopUserService;


    @Autowired
    private OrganService organService;

    @Autowired
    private RuleDao ruleDao;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("shopvip:shopuser:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = shopUserService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("shopvip:shopuser:info")
    public R info(@PathVariable("id") Integer id){
		ShopUserEntity shopUser = shopUserService.getById(id);

        return R.ok().put("shopUser", shopUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("shopvip:shopuser:save")
    public R save(@RequestBody ShopUserEntity shopUser){
		shopUserService.save(shopUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("shopvip:shopuser:update")
    public R update(@RequestBody ShopUserEntity shopUser){
		shopUserService.updateById(shopUser);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("shopvip:shopuser:delete")
    public R delete(@RequestBody Integer[] ids){
		shopUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 用户登录
     */
    @PostMapping("userLogin")
    public R userLogin(@RequestBody String str, HttpServletRequest request){

        if(StringUtils.isEmpty(str)){
            return R.error(400,"登录失败");
        }

        ShopUserVip shopUserVip = new ShopUserVip();
        shopUserVip= JSON.parseObject(str, ShopUserVip.class);
//        try {
//            System.out.println(shopUserVip);
//            if(shopUserVip.getOpenId() == null){
//                return R.error("请先关注公众号");
//            }
//
//        }catch (Exception e){
//            return R.error(401,"登录失败");
//        }
        ShopUserEntity shopUserEntity = shopUserService.userLogin(shopUserVip, request);

        if(shopUserEntity != null){
            return R.ok("登录成功").put("page",shopUserEntity);
        }else{

//            OrganEntity organEntity = organService.login(shopUserVip, request);
//            if(organEntity != null){
//                return R.error(201,"登录成功").put("page",organEntity);
//            }
            return R.error("密码或用户名错误");
        }

    }

    //成为会员
    @PostMapping("becamevip")
    public  R becamevip(@RequestBody String str,HttpServletRequest request){
        if(StringUtils.isEmpty(str)){
            return R.error("参数为空");
        }
        ShopUserEntity usr = JSONObject.parseObject(JSON.parseObject(str).getString("usr"), ShopUserEntity.class);
        Map<String, String> result = shopUserService.becamevip(usr,request);
        return R.ok().put("page",result);
    }

    //会员回调
    @RequestMapping(value = "/payCallback", method = RequestMethod.POST)
    public String payCallback(HttpServletRequest request){
        log.info("进入微信支付异步通知");
        String resXml="";
        try{
            //
            InputStream is = request.getInputStream();
            //将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml=sb.toString();
            log.info("微信支付异步通知请求包: {}", resXml);

            return shopUserService.payback(resXml);
        }catch (Exception e){
            log.error("微信支付回调通知失败",e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }
    @GetMapping("getOpenid")
    public R getOpenid(@RequestParam String code){
        if (StringUtils.isEmpty(code)) {
            return R.error(10001,"校验code为空");
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("appid","wx601b22b03c6ca4ed");
        param.put("secret","ab9a133c6ebfc4c9d985c9c07790ef7a");
        param.put("code",code);
        param.put("grant_type","authorization_code");
        Map<String,String> openId = HttpGetAndPost.doGet(param);
        log.info(openId.get("openid"));
        if(StringUtils.isEmpty(openId.get("openid"))){
            return R.error(10001,"openid为空");
        }
        return R.ok().put("page",openId.get("openid"));
    }

    @GetMapping("selectuserbyid")
    public  R selectUserById(@RequestParam("id") Integer id){
        if(id == null){
            return R.error("id为空");
        }
        BaseMapper<ShopUserEntity> baseMapper = shopUserService.getBaseMapper();
        ShopUserEntity shopUserEntity = baseMapper.selectById(id);
        return R.ok().put("page",shopUserEntity);
    }

    @PostMapping("forgetPassword")
    public  R forgetPassword(@RequestBody ShopUserEntity shopUserEntity){

        if(shopUserEntity == null){
            return R.error("参数为空");
        }
        BaseMapper<ShopUserEntity> baseMapper = shopUserService.getBaseMapper();
        ShopUserEntity shopUserEntity1 = baseMapper.selectOne(new QueryWrapper<>(shopUserEntity));

        if(shopUserEntity1 == null){
            return R.error("请输入正确的手机号和推荐码");
        }
        shopUserEntity1.setUserRock(0);
        baseMapper.updateById(shopUserEntity1);
        return R.ok();
    }


    @PostMapping("updatepassword")
    public R updatepassword(@RequestBody String str){
        if(StringUtils.isEmpty(str)){
            return R.error("参数错误");
        }
        String id = JSONObject.parseObject(str).getString("id");
        String password = JSONObject.parseObject(str).getString("password");
        String newpassword = JSONObject.parseObject(str).getString("newpassword");
        if(!StringUtils.isEmpty(password)&&!StringUtils.isEmpty(id)&&!StringUtils.isEmpty(newpassword)){

            Integer integer = Integer.valueOf(id);
            ShopUserEntity shopUserEntity = new ShopUserEntity();
            shopUserEntity.setId(integer);
            shopUserEntity.setPsPassword(MD5utils.getMD5Str(password));
            BaseMapper<ShopUserEntity> baseMapper = shopUserService.getBaseMapper();
            ShopUserEntity shopUserEntity1 = baseMapper.selectOne(new QueryWrapper<>(shopUserEntity));
            if(shopUserEntity1 == null){
                return R.error("密码错误");
            }
            shopUserEntity1.setPsPassword(MD5utils.getMD5Str(newpassword));
            baseMapper.updateById(shopUserEntity1);
            return R.ok();
        }

        return R.error("其中一个参数为空");
    }

    //计算用户能使用的积分
    @RequestMapping(value = "computersPoint",method = RequestMethod.POST)
    public  R computersPoint(@RequestBody ComputersPoint computersPoint){

        if(computersPoint!=null){
            BigDecimal point = computersPoint.getPoint();//商品积分
            BigDecimal price = computersPoint.getPrice();//商品价格
            //BigDecimal balance = computersPoint.getBalance();
            BigDecimal userpoint = computersPoint.getUserpoint();//用户积分
            if(point.compareTo(userpoint)==1){//商品积分大于用户持有积分
                return R.ok().put("totalprice",price.subtract(userpoint)).put("point",userpoint);
            }
            if(point.compareTo(userpoint)!=1) {
                return R.ok().put("totalprice",price.subtract(point)).put("point",point);
            }
        }
        return R.ok();
    }

    @RequestMapping(value = "computersBalance",method = RequestMethod.POST)
    public R compersBalance(@RequestBody ComputersPoint computersPoint){
        if(computersPoint!=null){
            BigDecimal price = computersPoint.getPrice();//商品价格
            BigDecimal balance = computersPoint.getBalance();

            if(price.compareTo(balance)==1){//商品佳哥大于用户余额

                return R.ok().put("totalprice",price.subtract(balance)).put("balance",balance);
            }
            if(price.compareTo(balance)!=1) {
                return R.ok().put("totalprice",0).put("balance",price);
            }
        }
        return R.error();
    }


    @RequestMapping(value = "topUp",method = RequestMethod.POST)
    public R topUp(@RequestBody  OrderEntity orderEntity,HttpServletRequest request){
        return shopUserService.topUp(orderEntity,request);
    }

    @RequestMapping(value = "topupaycall",method = RequestMethod.POST)
    public String topupaycall(HttpServletRequest request){
        log.info("进入微信支付异步通知");
        String resXml="";
        try{
            //
            InputStream is = request.getInputStream();
            //将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml=sb.toString();
            log.info("微信支付异步通知请求包: {}", resXml);

            return shopUserService.topuppaycall(resXml);
        }catch (Exception e){
            log.error("微信支付回调通知失败",e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }

    //获取的积分规则
    @RequestMapping(value = "getpointrule",method = RequestMethod.GET)
    public R getpointrule(){
        List<Rule> rules = ruleDao.selectList(null);

        return R.ok().put("page",rules);
    }

}
