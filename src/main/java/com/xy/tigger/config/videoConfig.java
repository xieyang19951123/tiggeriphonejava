package com.xy.tigger.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class videoConfig {

    @Value("${AccessKey}")
    String accessKeyId ;

    @Value("${Secret}")
    String accessKeySecret;


    @Bean(value = "defaultAcsClient")
    public DefaultAcsClient getDefaultAcsClient(){
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
