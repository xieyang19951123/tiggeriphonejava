package com.xy.tigger.uitls;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpGetAndPost {

    private static  String  url = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public static Map<String,String> doGet( Map<String, String> param) {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String,String> parse =  JSON.parseObject(resultString,Map.class);
        return parse;
    }

    /**
     * 获取用户openId
     * @param code 校验码
     * @return openId
     */
    public static String getOpenId(String code){
        HashMap<String, String> param = new HashMap<>();
        param.put("appid","wx601b22b03c6ca4ed");
        param.put("secret","ab9a133c6ebfc4c9d985c9c07790ef7a");
        param.put("code",code);
        param.put("grant_type","authorization_code");
        Map<String,String> openId = HttpGetAndPost.doGet(param);
        if(StringUtils.isEmpty(openId.get("openid"))){
            return openId.get("openid");
        }
        return "";
    }
}
