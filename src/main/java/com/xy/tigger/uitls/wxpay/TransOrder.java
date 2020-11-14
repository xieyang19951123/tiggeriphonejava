package com.xy.tigger.uitls.wxpay;

//import com.sun.javafx.binding.StringFormatter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@Service
public class TransOrder {

    /**
     * 获取随机订单号
     */

    public static String getOrder(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String format = simpleDateFormat.format(new Date());
        return format+UUID.randomUUID().toString().replaceAll("-","").substring(0,10);

    }

    public static void main(String[] args) {
        System.out.println(getOrder());
    }


    public static String getRecommend(){
        return RandomStringUtils.randomAlphanumeric(6);//获取随机的推荐码
    }
}
