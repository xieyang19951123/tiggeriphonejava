package com.xy.tigger.shopvip.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xy.tigger.shopvip.entity.PayVideoEntity;
import com.xy.tigger.shopvip.entity.ShopUserEntity;
import com.xy.tigger.shopvip.entity.vo.CourseVo;
import com.xy.tigger.shopvip.entity.vo.OragnVo;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import com.xy.tigger.shopvip.entity.vo.TryVideoVo;
import com.xy.tigger.shopvip.service.CourseService;
import com.xy.tigger.shopvip.service.OrganService;
import com.xy.tigger.shopvip.service.PayVideoService;
import com.xy.tigger.shopvip.service.TryVideoService;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shop/payVideo")
public class PayVideoController {

    @Autowired
    private PayVideoService payVideoService;

    @Autowired
    private TryVideoService tryVideoService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private OrganService organService;


    @RequestMapping("getVideo/{cid}")
    public R getVideo(@PathVariable Integer cid){
        List<PayVideoVo> video = payVideoService.getVideo(cid);
        return R.ok().put("page",video);
    }

    @RequestMapping("getTryVideo")
    public R getTryVideo(){
        return  R.ok().put("page",tryVideoService.getVideo());
    }




    //获取相应的推荐视频

    @GetMapping("/getPayVideo")
    public R getCorrelation(@RequestParam("cid")String cid){
        if(StringUtils.isEmpty(cid)){
            return R.error("获取相关视频失败");
        }

        List<PayVideoVo> correlation = payVideoService.getCorrelation(cid);

        return R.ok().put("page",correlation);
    }

    //购买视频
    @PostMapping
    public R payVideoMoney(@RequestBody String str){
        if(StringUtils.isEmpty(str)){
            return R.error("购买失败去，请稍后重试");
        }
        //商品id
        Integer id = JSON.parseObject(str).getInteger("id");
        //用户id
        Integer uid = JSON.parseObject(str).getInteger("uid");
        //金额
        BigDecimal money = JSON.parseObject(str).getBigDecimal("money");


        payVideoService.payViodeMoney(id,uid,money);

        return  R.ok();
    }

    //获取视频
    @GetMapping("getpayVideo")
    public R getpayVideo(@RequestParam("id")Integer id){
        if(id == null){
            return R.error("id为空");
        }
        PayVideoEntity payVideoEntity = payVideoService.getpayVideo(id);
        if(StringUtils.isEmpty(payVideoEntity.getAuth())){
            return R.error("获取凭证失败");
        }
        return R.ok().put("page",payVideoEntity);
    }


    //  点击分类
    @RequestMapping("getVideoCategory")
    public R getVideoCategory(@RequestParam("id")Integer id){
        if(id == null){
            return R.error("id为空");
        }
        List<PayVideoVo> correlation = payVideoService.getCorrelation(Arrays.asList(id).toString());
        //List<TryVideoVo> relevant = tryVideoService.getRelevant(Arrays.asList(id).toString());
        List<CourseVo> courseVos = courseService.getRelevant(id);
        return R.ok().put("payvideo",correlation ).put("course",courseVos);
    }

    @GetMapping("getpayvideomoduo")
    public  R getPayvideomoduo(){
        return R.ok().put("page",payVideoService.getPayvideomoduo());
    }

    //模糊查询
    @GetMapping("getvideobyvlue")
    public R getvideobyvlue(@RequestParam("val") String val){
        if(StringUtils.isEmpty(val)){
            return R.error("val为空");
        }
        List<PayVideoVo> payVideoVos = payVideoService.getvideobyvlue(val);
        List<CourseVo> relevant = courseService.getCourseByval(val);
        List<OragnVo> organVal = organService.getOrganVal(val);
        return R.ok().put("payvideo",payVideoVos ).put("tryvideo",relevant).put("organVal",organVal);
    }

    @GetMapping("getpayvideobyid")
    public R getPayVideoByid(@RequestParam("id")Integer id){
        if(id == null){
            return R.error("id为空");
        }
        PayVideoVo payVideoVo = payVideoService.getPayVideoByid(id);
        return R.ok().put("page",payVideoVo);
    }

    @PostMapping("payvideoorder")
    public R payVideoOrder(@RequestBody String str, HttpServletRequest request){
        if(StringUtils.isEmpty(str)){
            return R.error();
        }
        BigDecimal point = JSONObject.parseObject(JSON.parseObject(str).getString("point"), BigDecimal.class);
        BigDecimal totalprice = JSONObject.parseObject(JSON.parseObject(str).getString("totalprice"), BigDecimal.class);
        ShopUserEntity user = JSONObject.parseObject(JSON.parseObject(str).getString("user"), ShopUserEntity.class);
        PayVideoVo payVideoVo = JSONObject.parseObject(JSON.parseObject(str).getString("payvideo"), PayVideoVo.class);
        R r = payVideoService.payVideoOrder(point, totalprice, user, payVideoVo, request);

        return r;
    }

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


            return payVideoService.payBack(resXml);
        }catch (Exception e){
            log.error("微信支付回调通知失败",e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }

}
