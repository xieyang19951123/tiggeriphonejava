package com.xy.tigger.shopvip.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xy.tigger.shopvip.entity.AddressEntity;
import com.xy.tigger.shopvip.entity.OrganEntity;
import com.xy.tigger.shopvip.entity.ShoppingTrolley;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.shopvip.service.OrderService;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.R;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:14
 */
@RestController
@RequestMapping("shopvip/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("shopvip:order:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("shopvip:order:info")
    public R info(@PathVariable("id") Integer id){
		OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("shopvip:order:save")
    public R save(@RequestBody OrderEntity order){
		orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("shopvip:order:update")
    public R update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("shopvip:order:delete")
    public R delete(@RequestBody Integer[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    //商品创建订单
    @PostMapping("productOrder")
    public R productOrder(@RequestBody String str, HttpServletRequest request){
        System.out.println(str);
        if(StringUtils.isEmpty(str)){
            return R.error("参数为空");
        }
        List<ShoppingTrolley> products = JSONArray.parseArray(JSON.parseObject(str).getString("products"), ShoppingTrolley.class);
        AddressEntity address = JSONObject.parseObject(JSON.parseObject(str).getString("address"), AddressEntity.class);
        BigDecimal point = JSONObject.parseObject(JSON.parseObject(str).getString("point"), BigDecimal.class);
        BigDecimal total = JSONObject.parseObject(JSON.parseObject(str).getString("total"), BigDecimal.class);
        String openid = JSON.parseObject(str).getString("openid");
        if(StringUtils.isEmpty(openid)||address==null || products == null || point ==null || total == null){
            return R.error("下单失败");
        }
        if(StringUtils.isEmpty(openid)){
            return R.error(40001,"opeid为空");
        }
        R r = orderService.productOrder(products, address, point, total, openid, request);
        return r;
    }


    //支付成功后商品回调
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


            return orderService.payBack(resXml);
        }catch (Exception e){
            log.error("微信支付回调通知失败",e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }

    @GetMapping("selectOrder")
    public R selectOrder(@RequestParam("id")Integer id){

        if(id== null){
            return R.error("id为空");
        }
        R r = orderService.selectOrder(id);
        return r;
    }



}
