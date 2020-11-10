package com.xy.tigger.shopvip.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xy.tigger.uitls.GetAcessTokenUtils;
import com.xy.tigger.uitls.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.RandomUtils;
import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/wx")
@RequiredArgsConstructor
@Slf4j
public class WxAuthController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GetAcessTokenUtils getAcessTokenUtils;

    @Value("${vendor.wx.config.app_id}")
    private String appId;

    @Value("${vendor.wx.pay.secret}")
    private String secret;


    @Autowired
    private RedisUtil redisUtil;


    /**
     * 获取微信分享的签名配置,对jsapi_ticket进行缓存防止请求过多被封号
     * 允许跨域（只有微信公众号添加了js安全域名的网站才能加载微信分享，故这里不对域名进行校验）
     */
    @GetMapping("/getShareSignature2")
    public Map<String,Object>  getShareSignature2(HttpServletRequest request, HttpServletResponse response) throws WxErrorException {
        String wxShareUrl=request.getParameter("shareurl");

        String token = getToken();
        String ticket = getticket(token);
        long timeStampSec = System.currentTimeMillis()/1000;
        long timestamp = System.currentTimeMillis() / 1000L;//当前时间戳
        String nonceStr = getRandomStr(8);//随机数
        String[] signArr = new String[]{"url=" + wxShareUrl, "jsapi_ticket=" + ticket, "noncestr=" + nonceStr, "timestamp=" + timestamp};
        Arrays.sort(signArr);
        String signStr = org.apache.commons.lang.StringUtils.join(signArr, "&");
        String resSign = DigestUtils.sha1Hex(signStr);//加密
        Map<String,Object> map=new HashMap();
        map.put("appId",appId);
        map.put("timeStamp",timestamp);
        map.put("nonceStr",nonceStr);
        map.put("signature",resSign);//将map放回给前端大功告成!

        return map;
    }
    public static String getRandomStr(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int randomNum;
        char randomChar;
        Random random = new Random();
        // StringBuffer类型的可以append增加字符
        StringBuffer str = new StringBuffer();

        for (int i = 0; i < length; i++) {
            // 可生成[0,n)之间的整数，获得随机位置
            randomNum = random.nextInt(base.length());
            // 获得随机位置对应的字符
            randomChar = base.charAt(randomNum);
            // 组成一个随机字符串
            str.append(randomChar);
        }
        return str.toString();
    }
    public  String getticket(String access_token){
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ access_token +"&type=jsapi";//这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.parseObject(message);
            System.out.println("JSON字符串："+demoJson);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ticket;
    }

    public   String getToken(){
        String access_token = "";
        String grant_type = "client_credential";//获取access_token填写client_credential
        //这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grant_type + "&appid=" +appId + "&secret=" + secret;
        String access_token1 = (String)redisUtil.get("access_token");
        if(StringUtils.isNotEmpty(access_token1)){
            return access_token1;
        }
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.parseObject(message);
            System.out.println("JSON字符串：" + demoJson);
            access_token = demoJson.getString("access_token");
            if (StringUtils.isNotBlank(access_token)) {
                //String key = RedisDBase.BGY_MEMBER + "access_token";
                redisUtil.set("access_token", access_token, 7200);
            } else {
                return message + "微信返回错误提示";
            }
            is.close();
        } catch (Exception e) {
            log.error("call getAccessToken error",e);
        }
        return access_token;

    }

}