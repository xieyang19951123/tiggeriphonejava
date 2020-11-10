package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.CommentDao;
import com.xy.tigger.shopvip.dao.PayVideoDao;
import com.xy.tigger.shopvip.dao.ProductDao;
import com.xy.tigger.shopvip.entity.CommentEntity;
import com.xy.tigger.shopvip.entity.vo.CommentVo;
import com.xy.tigger.shopvip.service.ProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("productService")
public class ProductServiceImpl  extends ServiceImpl<CommentDao, CommentEntity> implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private PayVideoDao payVideoDao;

    @Override
    public List<CommentVo> getProduct(String cid,Integer show,String val) {
        List<CommentVo> products = productDao.getProducts(show,val).stream().map(item->{
            String s = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list = payVideoDao.selectCategroy(s);
            item.setName(list);
            String id = item.getFjId().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> accessoryEntities = productDao.getFjsrc(id);
            item.setAccessoryEntities(accessoryEntities);
            return item;
        }).collect(Collectors.toList());
        if(!StringUtils.isEmpty(cid)){
            List<CommentVo> collect = products.stream().filter(item -> {
                //List<String> ids = Arrays.asList(item.getCid().replaceAll(" ","").replaceAll("\\(","").replaceAll("\\)","").split(","));
                return item.getCid().contains(cid);
            }).collect(Collectors.toList());
            return collect;
        }
        return products;
    }
}
