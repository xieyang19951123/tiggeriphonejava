package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.CommentEntity;
import com.xy.tigger.shopvip.entity.vo.CommentVo;

import java.util.List;

public interface ProductService  extends IService<CommentEntity> {
    List<CommentVo> getProduct(String cid,Integer show,String val);
}
