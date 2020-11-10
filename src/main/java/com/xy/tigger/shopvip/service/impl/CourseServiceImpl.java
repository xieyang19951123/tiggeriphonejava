package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.CourseDao;
import com.xy.tigger.shopvip.dao.OrderDao;
import com.xy.tigger.shopvip.dao.PayVideoDao;
import com.xy.tigger.shopvip.dao.ShopUserDao;
import com.xy.tigger.shopvip.entity.CourseEntity;
import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.shopvip.entity.PayEntity;
import com.xy.tigger.shopvip.entity.ShopUserEntity;
import com.xy.tigger.shopvip.entity.vo.CourseVo;
import com.xy.tigger.shopvip.service.CourseService;
import com.xy.tigger.uitls.HttpGetAndPost;
import com.xy.tigger.uitls.R;
import com.xy.tigger.uitls.WXPayUtils;
import com.xy.tigger.uitls.wxpay.TransOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private PayVideoDao payVideoDao;

    @Autowired
    private WXPayUtils wxPayUtils;

    @Autowired
    private ShopUserDao shopUserDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public List<CourseVo> getCourseRecommend(Integer cid) {
        List<CourseVo>  courseVos =  courseDao.getCourseRecommend(null,null,null);
        List<CourseVo> collect = courseVos.stream().filter(item -> {
            if (cid != 0) {

                return item.getCid().contains(cid.toString());
            }
            return true;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public R getCourseById(Integer id) {
        List<CourseVo> courseRecommend = courseDao.getCourseRecommend(id,null,null);
        List<CourseVo> courseRecommend1 = courseDao.getCourseRecommend(null,null,null);
        List<CourseVo> collect1 = courseRecommend1.stream().filter(item -> {
            List<String> list = Arrays.asList(courseRecommend.get(0).getCid().replaceAll(" ", "").replaceAll("\\[", "").replaceAll("]", "").split(","));
            List<String> collect = list.stream().filter(l -> {
                //boolean contains =
                return item.getCid().contains(l);

            }).collect(Collectors.toList());
            if(item.getId() == courseRecommend.get(0).getId()){
                return false;
            }
            return collect.size() > 0;
            //return  false;
        }).collect(Collectors.toList());

        return R.ok().put("page",courseRecommend).put("related",collect1);
    }

    @Override
    public List<CourseVo> getRelevant(Integer id) {

        List<CourseVo> courseRecommend = courseDao.getCourseRecommend(null, null,null);
        List<CourseVo> collect = courseRecommend.stream().filter(item -> {
            String s = id.toString();
            return item.getCid().contains(s);
        }).map(item->{
            String cid = item.getCid().replaceAll(" ","").replaceAll("\\[","(").replaceAll("]",")");
            if(!cid.equals("()")){

                List<String> list = payVideoDao.selectCategroy(cid);
                item.setName(list);
            }
            return  item;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public R byCourseOrder(OrderEntity orderEntity, HttpServletRequest request) {

        //shopUserDao
        ShopUserEntity shopUserEntity = shopUserDao.selectById(orderEntity.getUId());
        String openId = shopUserEntity.getOpenId();
        System.out.println(orderEntity);
        String ordercode = TransOrder.getOrder();
        PayEntity payEntity = new PayEntity();
        payEntity.setHttpServletRequest(request);
        payEntity.setMoney(orderEntity.getPRealMoney());
        payEntity.setMoney(new BigDecimal(0.01));
        payEntity.setOpenId(openId);
        payEntity.setOrder(ordercode);
        payEntity.setTitle("课程支付");
        payEntity.setNotifyUrl("http://object.ngrok2.xiaomiqiu.cn/shop/payVideo/payCallback");
        //payEntity.setNotifyUrl("http://106.75.162.179:8081/shop/payVideo/payCallback");
        Map<String, String> resultMap = wxPayUtils.payMoney(payEntity);
        //System.out.println("============");
        if(resultMap == null ){
            return R.error(10001,"下单失败");
        }
        orderEntity.setOCode(ordercode);
        orderEntity.setCreatTime(new Date());
        orderEntity.setPostage(new BigDecimal(0));
        orderDao.insert(orderEntity);
        return R.ok().put("page",resultMap);
    }

    @Override
    public List<CourseVo> getCourseByval(String val) {
        List<CourseVo> courseRecommend = courseDao.getCourseRecommend(null, null,val);
        List<CourseVo> collect = courseRecommend.stream().map(item->{
            String cid = item.getCid().replaceAll(" ","").replaceAll("\\[","(").replaceAll("]",")");
            if(!cid.equals("()")){

                List<String> list = payVideoDao.selectCategroy(cid);
                item.setName(list);
            }
            return  item;
        }).collect(Collectors.toList());
        return collect;
    }
}
