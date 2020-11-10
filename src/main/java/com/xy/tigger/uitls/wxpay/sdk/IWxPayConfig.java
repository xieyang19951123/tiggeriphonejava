package com.xy.tigger.uitls.wxpay.sdk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class IWxPayConfig extends WXPayConfig { // 继承sdk WXPayConfig 实现sdk中部分抽象方法

    private byte[] certData;

    @Value("${vendor.wx.config.app_id}")
    private String app_id;

    @Value("${vendor.wx.pay.key}")
    private String wx_pay_key;

   @Value("${vendor.wx.pay.mch_id}")
    private String wx_pay_mch_id;

    public IWxPayConfig() throws Exception { // 构造方法读取证书, 通过getCertStream 可以使sdk获取到证书
        String certPath = "apiclient_cert.p12";
        //File file = new File(this.getClass().getClassLoader().getResource(certPath).getFile());
         File file = new File("/app/apiclient_cert.p12");

        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return app_id;
    }

    @Override
    public String getMchID() {
        return wx_pay_mch_id;
    }

    @Override
    public String getKey() {
        return wx_pay_key;
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public IWXPayDomain getWXPayDomain() { // 这个方法需要这样实现, 否则无法正常初始化WXPay
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }
            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }
}