package com.xy.tigger.shopvip.controller;

import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.shopvip.entity.vo.CourseVo;
import com.xy.tigger.shopvip.service.CourseService;
import com.xy.tigger.uitls.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("shopvip/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "getCourseRecommend/{cid}",method = RequestMethod.GET)
    public  R getCourseRecommend(@PathVariable Integer cid){

        List<CourseVo> courseVos = courseService.getCourseRecommend(cid);
        return R.ok().put("page",courseVos);
    }

    @RequestMapping(value = "getCourseById/{id}",method = RequestMethod.GET)
    public  R getCourseById(@PathVariable Integer id){
        return courseService.getCourseById(id);
    }


    @RequestMapping(value = "byCourseOrder",method = RequestMethod.POST)
    public  R byCourseOrder(@RequestBody OrderEntity orderEntity, HttpServletRequest httpServletRequest){

        return courseService.byCourseOrder(orderEntity,httpServletRequest);
    }

    @RequestMapping(value = "payCallBack",method = RequestMethod.POST)
    public  String payCallBack(HttpServletRequest request){
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


            return null;
        }catch (Exception e){
            log.error("微信支付回调通知失败",e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }
}
