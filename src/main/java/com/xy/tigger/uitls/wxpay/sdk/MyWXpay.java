package com.xy.tigger.uitls.wxpay.sdk;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.xy.tigger.shopvip.dao.OrderDao;
import com.xy.tigger.shopvip.dao.ShopUserDao;
import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.shopvip.entity.ShopUserEntity;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.uitls.IpUtil;
import com.xy.tigger.uitls.R;
import com.xy.tigger.uitls.wxpay.TransOrder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class MyWXpay {

    @Autowired
    private  IWxPayConfig iWxPayConfig;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ShopUserDao shopUserDao;

    public   R placeOrder(ShopUserVip shopUserVip, HttpServletRequest request){
        String ipAddr = IpUtil.getIpAddr(request);
        // 发起微信支付
        WXPay wxpay = null;
        Map<String, String> result = new HashMap<>();
        try {
            // ******************************************
            //
            //  统一下单
            //
            // ******************************************
            wxpay = new WXPay(iWxPayConfig); // *** 注入自己实现的微信配置类, 创建WXPay核心类, WXPay 包括统一下单接口
            String order = TransOrder.getOrder()+shopUserVip.getIphone();
            order = order.substring(10,order.length());
            Map<String, String> data = new HashMap<String, String>();
            data.put("body", "订单详情");
            data.put("out_trade_no", order); // 订单唯一编号, 不允许重复
            data.put("total_fee", String.valueOf(shopUserVip.getPaymoney().multiply(new BigDecimal(100)).setScale( 0, BigDecimal.ROUND_DOWN ))); // 订单金额, 单位分
            data.put("spbill_create_ip", ipAddr); // 下单ip
            data.put("openid", shopUserVip.getOpenId()); // 微信公众号统一标示openid
            data.put("notify_url", "http://106.75.162.179:8081/user/payCallback"); // 订单结果通知, 微信主动回调此接口
            data.put("trade_type", "JSAPI"); // 固定填写

            log.info("发起微信支付下单接口, request={}", data);
            Map<String, String> response = wxpay.unifiedOrder(data); // 微信sdk集成方法, 统一下单接口unifiedOrder, 此处请求   MD5加密   加密方式
            log.info("微信支付下单成功, 返回值 response={}", response);
            String returnCode = response.get("return_code");
            if (!"SUCCESS".equals(returnCode)) {
                return R.error(500,"下单错误");
            }
            String resultCode = response.get("result_code");
            if (!"SUCCESS".equals(resultCode)) {
                return R.error(500,"下单错误");
            }
            String prepay_id = response.get("prepay_id");
            if (prepay_id == null) {
                return R.error(500,"下单错误");
            }

            // ******************************************
            //
            //  前端调起微信支付必要参数
            //
            // ******************************************
            String packages = "prepay_id=" + prepay_id;
            Map<String, String> wxPayMap = new HashMap<String, String>();
            wxPayMap.put("appId", iWxPayConfig.getAppID());
            wxPayMap.put("timeStamp", String.valueOf(System.currentTimeMillis()));
            wxPayMap.put("nonceStr", UUID.randomUUID().toString().substring(0,32));
            wxPayMap.put("package", packages);
            wxPayMap.put("signType", "MD5");
            // 加密串中包括 appId timeStamp nonceStr package signType 5个参数, 通过sdk WXPayUtil类加密, 注意, 此处使用  MD5加密  方式
            String sign = WXPayUtil.generateSignature(wxPayMap, iWxPayConfig.getKey());

            // ******************************************
            //
            //  返回给前端调起微信支付的必要参数
            //
            // ******************************************
            result.put("prepay_id", prepay_id);
            result.put("paySign", sign);
            result.putAll(wxPayMap);

            //生成订单
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setCId(0);
            orderEntity.setNumbers(0);
            orderEntity.setOCode(order);
            orderEntity.setOMoney(shopUserVip.getPaymoney());
            orderEntity.setPayStatus(1);
            orderEntity.setUId(shopUserVip.getId());
            orderEntity.setPayType(1);
            orderEntity.setShowStatus(1);
            orderEntity.setCreatTime(new Date());
            orderDao.insert(orderEntity);
            System.out.println(result);
            return R.ok().put("data",result) ;
        } catch (Exception e) {
            e.printStackTrace();
            return  R.error(500,"下单错误");
        }
    }

    /**
     * 回调处理
     *
     */

    public String payBack(String notifyData){
        log.info("payBack() start, notifyData={}", notifyData);
        String xmlBack="";
        Map<String, String> notifyMap = null;
        try {
            WXPay wxpay = new WXPay(iWxPayConfig);
            notifyMap = WXPayUtil.xmlToMap(notifyData);         // 转换成map
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                // 进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//订单号

                if (out_trade_no == null) {
                    log.info("微信支付回调失败订单号: {}", notifyMap);
                    xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    return xmlBack;
                }

                // 业务逻辑处理 ****************************
                log.info("微信支付回调成功订单号: {}", notifyMap);

                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOCode(out_trade_no);
                QueryWrapper<OrderEntity> orderEntityQueryWrapper = new QueryWrapper<>(orderEntity);
                OrderEntity orderEntity1 = orderDao.selectOne(orderEntityQueryWrapper);
                orderEntity1.setPayStatus(2);
                orderDao.updateById(orderEntity1);
                ShopUserEntity shopUserEntity = new ShopUserEntity();
                shopUserEntity.setId(orderEntity1.getUId());
                QueryWrapper<ShopUserEntity> shopUserEntityQueryWrapper = new QueryWrapper<>(shopUserEntity);
                ShopUserEntity shopUserEntity1 = shopUserDao.selectOne(shopUserEntityQueryWrapper);
                shopUserEntity1.setShopVip(1);
                //Date date1 = ;
                Calendar cal = Calendar.getInstance();

                cal.setTime(new Date());
                cal.add(Calendar.YEAR,1);
                Date date=cal.getTime();
                shopUserEntity1.setExpireTime(date);
                shopUserDao.updateById(shopUserEntity1);

                xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[SUCCESS]]></return_msg>" + "</xml> ";
                return xmlBack;
            } else {
                log.error("微信支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("微信支付回调通知失败",e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }
}
