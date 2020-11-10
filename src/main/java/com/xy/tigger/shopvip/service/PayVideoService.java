package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.PayVideoEntity;
import com.xy.tigger.shopvip.entity.ShopUserEntity;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import com.xy.tigger.uitls.R;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface PayVideoService extends IService<PayVideoEntity> {

    List<PayVideoVo> getVideo(Integer cid);


    List<PayVideoVo> getCorrelation(String cid);

    void payViodeMoney(Integer id, Integer uid, BigDecimal money);

    PayVideoEntity getpayVideo(Integer id);

    List<PayVideoVo> getPayvideomoduo();

    List<PayVideoVo> getvideobyvlue(String val);

    PayVideoVo getPayVideoByid(Integer id);

    R payVideoOrder(BigDecimal point, BigDecimal totalprice, ShopUserEntity user, PayVideoVo payVideoVo, HttpServletRequest request);

    String payBack(String resXml);
}
