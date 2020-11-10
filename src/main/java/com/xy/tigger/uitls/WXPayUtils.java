package com.xy.tigger.uitls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.shopvip.entity.PayEntity;
import com.xy.tigger.shopvip.entity.ShopUserEntity;
import com.xy.tigger.shopvip.service.OrderService;
import com.xy.tigger.uitls.wxpay.TransOrder;
import com.xy.tigger.uitls.wxpay.sdk.IWxPayConfig;
import com.xy.tigger.uitls.wxpay.sdk.WXPay;
import com.xy.tigger.uitls.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Component
@Slf4j
public class WXPayUtils {


    @Autowired
    private IWxPayConfig iWxPayConfig;

    @Autowired
    private OrderService orderService;


    public Map<String, String>  payMoney(PayEntity payEntity){
        //获取用户ip地址
        String ipAddr = IpUtil.getIpAddr(payEntity.getHttpServletRequest());

        WXPay wxpay = null;
        Map<String, String> result = new HashMap<>();

        try{
            wxpay =  new WXPay(iWxPayConfig);
            Map<String, String> data = new HashMap<String, String>();
            data.put("body", payEntity.getTitle());
            data.put("out_trade_no", payEntity.getOrder()); // 订单唯一编号, 不允许重复
            data.put("total_fee", String.valueOf(payEntity.getMoney().multiply(new BigDecimal(100)).setScale( 0, BigDecimal.ROUND_DOWN )));
            data.put("spbill_create_ip", ipAddr); // 下单ip
            data.put("openid", payEntity.getOpenId()); // 微信公众号统一标示openid
            data.put("notify_url", payEntity.getNotifyUrl()); // 订单结果通知, 微信主动回调此接口
            data.put("trade_type", "JSAPI"); // 固定填写
            Map<String, String> response = wxpay.unifiedOrder(data); // 微信sdk集成方法, 统一下单接口unifiedOrder, 此处请求   MD5加密   加密方式
            String returnCode = response.get("return_code");
            System.out.println("=========================");
            System.out.println(response);
            if (!"SUCCESS".equals(returnCode)) {
                return null;
            }

            String resultCode = response.get("result_code");
            if (!"SUCCESS".equals(resultCode)) {
                return null;
            }
            String prepay_id = response.get("prepay_id");
            if (prepay_id == null) {
                return null;
            }

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
            return result;
        }catch (Exception e){
            return null;
        }


    }

    /**
     * 支付回调
     * @param notifyData
     * @return
     */
    public Map<String,String> payBack(String notifyData){

        log.info("payBack() start, notifyData={}", notifyData);
        String xmlBack="";
        Map<String, String> notifyMap = null;
        Map<String,String> restlut = new HashMap<>();
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
                    restlut.put("xmlBack",xmlBack);
                    return restlut;
                }
                restlut.put("out_trade_no",out_trade_no);
                // 业务逻辑处理 ****************************
                log.info("微信支付回调成功订单号: {}", notifyMap);

                xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[SUCCESS]]></return_msg>" + "</xml> ";
                restlut.put("xmlBack",xmlBack);
                return restlut;
            } else {
                log.error("微信支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                restlut.put("xmlBack",xmlBack);
                return restlut;
            }
        } catch (Exception e) {
            log.error("微信支付回调通知失败",e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        restlut.put("xmlBack",xmlBack);
        return restlut;
    }

}
