package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.*;
import com.xy.tigger.shopvip.entity.*;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import com.xy.tigger.shopvip.service.PayVideoService;
import com.xy.tigger.uitls.R;
import com.xy.tigger.uitls.VideoUtils;
import com.xy.tigger.uitls.WXPayUtils;
import com.xy.tigger.uitls.wxpay.TransOrder;
import com.xy.tigger.uitls.wxpay.sdk.IWxPayConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("payVideoService")
public class PayVideoServiceImpl extends ServiceImpl<PayVideoDao, PayVideoEntity> implements PayVideoService {

    @Autowired
    private PayVideoDao payVideoDao;

    @Autowired
    private IWxPayConfig iWxPayConfig;

    @Autowired
    private VideoUtils videoUtils;

    @Autowired
    private OrganDao organDao;

    @Autowired
    private WXPayUtils wxPayUtils;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RuleDao ruleDao;

    @Autowired
    private ShopUserDao shopUserDao;

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private UserVideoDao userVideoDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private ShareDao shareDao;

    @Autowired
    private PointRecordDao pointRecordDao;



    @Override
    public List<PayVideoVo> getVideo(Integer cid) {
        List<PayVideoVo> payVideoVos = payVideoDao.selectPayVideoVo();
        List<PayVideoVo> collect = payVideoVos.stream().filter(item->{
            if(cid!=0){

                return item.getCid().contains(cid.toString());
            }
            return  true;
        }).map(item -> {
            String s = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list = payVideoDao.selectCategroy(s);
            item.setName(list);
            return item;
        }).collect(Collectors.toList());
        return collect;
    }



    @Override
    public List<PayVideoVo> getCorrelation(String cid) {
        String s = cid.replaceAll("\\[", "(").replaceAll("]", ")").replaceAll(" ","");

        List<String> list = Arrays.asList(s.substring(1, s.length()).substring(0, s.length() - 2));
        List<PayVideoVo> payVideoEntities = payVideoDao.selectPayVideoList();
        List<PayVideoVo> collect = payVideoEntities.stream().filter(item -> {
            String str =  item.getCid().replaceAll(" ","");
            List<String> list1 = Arrays.asList(str.substring(1, str.length()).substring(0,str.length() - 2).split(","));

            List<String> collect1 = list1.stream().filter(li ->
                    list.contains(li)
            ).collect(Collectors.toList());
            return collect1.size()>0;
        }).map(item->{
            String s1 = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list1 = payVideoDao.selectCategroy(s1);
            item.setName(list1);
            return item;
        }).collect(Collectors.toList());
        return collect;
    }

    //购买视频
    @Override
    public void payViodeMoney(Integer id, Integer uid, BigDecimal money) {
        String order = TransOrder.getOrder();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPayStatus(2);
        orderEntity.setShowStatus(1);
        orderEntity.setUId(uid);
        orderEntity.setNumbers(1);
        orderEntity.setCreatTime(new Date());
        orderEntity.setOCode(order);
        orderEntity.setPayType(1);

    }

    @Override
    public PayVideoEntity getpayVideo(Integer id) {
        PayVideoEntity payVideoEntity = payVideoDao.selectById(id);
        OrganEntity organEntity = organDao.selectById(payVideoEntity.getOid());
        payVideoEntity.setOrganEntity(organEntity);
        String videoVoucher = videoUtils.getVideoVoucher(payVideoEntity.getVideoId());
        payVideoEntity.setAuth(videoVoucher);
        return payVideoEntity;
    }

    @Override
    public List<PayVideoVo> getPayvideomoduo() {
        List<PayVideoVo> payVideoVos = payVideoDao.selectPayVideomoduo();
        List<PayVideoVo> collect = payVideoVos.stream().map(item -> {
            String s = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list = payVideoDao.selectCategroy(s);
            item.setName(list);
            return item;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<PayVideoVo> getvideobyvlue(String val) {
        List<PayVideoVo> payVideoVos = payVideoDao.selectPayvideoByVal(val);
        List<PayVideoVo> collect = payVideoVos.stream().map(item -> {
            String s1 = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list1 = payVideoDao.selectCategroy(s1);
            item.setName(list1);
            return item;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public PayVideoVo getPayVideoByid(Integer id) {
        return payVideoDao.getPayVideoByid(id);
    }

    @Override
    public R payVideoOrder(BigDecimal point, BigDecimal totalprice, ShopUserEntity user, PayVideoVo payVideoVo, HttpServletRequest request) {
        String order = TransOrder.getOrder();
        PayEntity payEntity = new PayEntity();
//        payEntity.setNotifyUrl("http://106.75.162.179:8081/shop/payVideo/payCallback");
        payEntity.setNotifyUrl("http://object.ngrok2.xiaomiqiu.cn/shop/payVideo/payCallback");
        payEntity.setHttpServletRequest(request);
        payEntity.setMoney(new BigDecimal(1));
        //payEntity.setMoney(totalprice);
        payEntity.setOpenId(user.getOpenId());
        payEntity.setOrder(order);
        payEntity.setTitle("购买课程");
        Map<String, String> stringStringMap = wxPayUtils.payMoney(payEntity);
        if(stringStringMap == null){
            return R.error(4001,"下单失败");
        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPostage(new BigDecimal(0));
        orderEntity.setPayType(3);
        orderEntity.setShowStatus(1);
        orderEntity.setPayStatus(1);
        orderEntity.setOCode(order);
        orderEntity.setCId(payVideoVo.getId());
        orderEntity.setUId(user.getId());
        orderEntity.setOMoney(totalprice);
        orderEntity.setCreatTime(new Date());
        orderEntity.setNumbers(1);
        orderEntity.setPoint(point);
        orderEntity.setPRealMoney(point.add(totalprice).setScale(2));
        orderDao.insert(orderEntity);
        return R.ok().put("page",stringStringMap);

    }

    @Override
    public String payBack(String resXml) {
        Map<String, String> stringStringMap = wxPayUtils.payBack(resXml);
        String out_trade_no = stringStringMap.get("out_trade_no");
        if(StringUtils.isEmpty(out_trade_no)){
            return stringStringMap.get("xmlBack");
        }
        OrderEntity orderEntity = new OrderEntity();
        //根据订单号查询订单
        orderEntity.setOCode(out_trade_no);
        OrderEntity order = orderDao.selectOne(new QueryWrapper<OrderEntity>(orderEntity));
        order.setPayStatus(2);
        orderDao.updateById(order);

        //积分

       //Rule rule = ruleDao.selectById(3);
        ShopUserEntity shopUserEntity = shopUserDao.selectById(order.getUId());
        ShareEntity shareEntity = new ShareEntity();
        shareEntity.setUid(shopUserEntity.getId());
        shareEntity.setProductid(order.getCId());
        if(order.getPayType() == 4){
            shareEntity.setShareType(1);//线下课程
        }else{
            shareEntity.setShareType(2);//线上课程
        }

        ShareEntity shareEntity1 = shareDao.selectOne(new QueryWrapper<>(shareEntity));

        ShopUserEntity parent = shopUserDao.selectById(shopUserEntity.getParentId());
        Integer sharepoint = null;
        Integer mepoint = null;
        Integer parentpoint = null;
        if(order.getPayType() == 4){
            CourseEntity courseEntity = courseDao.selectById(order.getCId());
            sharepoint = courseEntity.getSharePoint();
            mepoint = courseEntity.getByPoint();
            parentpoint = courseEntity.getByPointFather();
        }else{
            PayVideoEntity payVideoEntity = payVideoDao.selectById(order.getCId());
            sharepoint = payVideoEntity.getSharePoint();
            mepoint = payVideoEntity.getByPoint();
            parentpoint = payVideoEntity.getByPointFather();
        }
        if(parent!=null){

            parent.setPoint(parent.getPoint().add(new BigDecimal(parentpoint)).setScale(2));
            PointRecordEntity pointRecordEntity = new PointRecordEntity();
            pointRecordEntity.setPonit(parent.getPoint());
            pointRecordEntity.setMessage("下级用户购买商品得"+parentpoint+"积分");
            pointRecordEntity.setCreateTime(new Date());
            pointRecordEntity.setUid(parent.getId());
            pointRecordEntity.setShowStatus(1);
            pointRecordDao.insert(pointRecordEntity);
            shopUserDao.updateById(parent);

        }else if(shareEntity1!=null && parent == null){

            ShopUserEntity shopUserEntity1 = shopUserDao.selectById(shareEntity1.getRemmondid());
            shopUserEntity1.setPoint(shopUserEntity1.getPoint().add(new BigDecimal(sharepoint)).setScale(2));
            shopUserDao.updateById(shopUserEntity1);
            PointRecordEntity pointRecordEntity = new PointRecordEntity();
            pointRecordEntity.setUid(shareEntity1.getId());
            pointRecordEntity.setPonit(shopUserEntity1.getPoint());
            pointRecordEntity.setMessage("分享商品得"+sharepoint+"积分");
            pointRecordEntity.setCreateTime(new Date());
            pointRecordEntity.setShowStatus(1);
            pointRecordDao.insert(pointRecordEntity);
        }




        shopUserEntity.setPoint(shopUserEntity.getPoint().add(new BigDecimal(mepoint)).subtract(order.getPoint()));
        //用户使用余额
        BigDecimal pBalance = order.getPBalance();
        if(pBalance !=null){
            if(pBalance.intValue() !=0){
                shopUserEntity.setBalance(shopUserEntity.getBalance().subtract(pBalance).setScale(2));
            }
        }
        shopUserDao.updateById(shopUserEntity);
        PointRecordEntity pointRecordEntity = new PointRecordEntity();
        pointRecordEntity.setPonit(shopUserEntity.getPoint());
        pointRecordEntity.setUid(shopUserEntity.getId());
        pointRecordEntity.setCreateTime(new Date());
        pointRecordEntity.setShowStatus(1);
        pointRecordEntity.setMessage("消费商品获得"+mepoint+"积分,使用积分"+order.getPoint());
        pointRecordDao.insert(pointRecordEntity);




        //学生档案和机构的联系
        PayVideoEntity payVideoEntity = payVideoDao.selectById(order.getCId());
        CourseEntity courseEntity = courseDao.selectById(order.getCId());

        RecordEntity recordEntity = new RecordEntity();
        if(order.getPayType() == 4){//线下产品
            recordEntity.setBoosId(courseEntity.getOid());
        }else{//线上
            recordEntity.setBoosId(payVideoEntity.getOid());
        }

        recordEntity.setShowStatus(1);
        recordEntity.setUId(order.getUId());
        RecordEntity recordEntity1 = recordDao.selectOne(new QueryWrapper<RecordEntity>(recordEntity));
        if(recordEntity1 == null){
            recordDao.insert(recordEntity);
        }
        //学生课程表
        UserVideo userVideo = new UserVideo();
        userVideo.setShowStatus(1);
        userVideo.setUId(order.getUId());

        userVideo.setVideoId(order.getCId());
        if(order.getPayType() == 4){
            userVideo.setOType(2);
            userVideo.setOId(courseEntity.getOid());
        }
        else {
            userVideo.setOType(1);
            userVideo.setOId(payVideoEntity.getOid());
        }
        userVideoDao.insert(userVideo);
        return stringStringMap.get("xmlBack");
    }
}
