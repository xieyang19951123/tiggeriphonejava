package com.xy.tigger.shopvip.controller;

import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.shopvip.service.ConsumerService;
import com.xy.tigger.uitls.R;
import com.xy.tigger.uitls.wxpay.sdk.MyWXpay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("user/")
@Slf4j
public class ConsumeController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private MyWXpay myWXpay;

    /**
     * 用户进行注册
     * @param shopUserVip
     * @return
     */
    @RequestMapping("regist")
    public R login(@Valid @RequestBody ShopUserVip shopUserVip, HttpServletRequest request){

        R regist = consumerService.regist(shopUserVip, request);
        //System.out.println(regist);
        log.info("用户进行注册");
        return  regist;
    }

    /**
     * 支付回调
     * @param
     * @return
     */
    @RequestMapping(value = "/payCallback", method = RequestMethod.POST)
    public String payCallback(HttpServletRequest request, HttpServletResponse response) {
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
            return myWXpay.payBack(resXml);
        }catch (Exception e){
            log.error("微信支付回调通知失败",e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }


}
