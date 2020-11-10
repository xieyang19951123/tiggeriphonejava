package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.SignDao;
import com.xy.tigger.shopvip.entity.SignEntity;
import com.xy.tigger.shopvip.service.SignService;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("signService")
public class SignServiceImpl extends ServiceImpl<SignDao, SignEntity> implements SignService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SignEntity> page = this.page(
                new Query<SignEntity>().getPage(params),
                new QueryWrapper<SignEntity>()
        );

        return new PageUtils(page);
    }

}