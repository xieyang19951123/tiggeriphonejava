package com.xy.tigger.shopvip.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.Query;

import com.xy.tigger.shopvip.dao.AccessoryDao;
import com.xy.tigger.shopvip.entity.AccessoryEntity;
import com.xy.tigger.shopvip.service.AccessoryService;


@Service("accessoryService")
public class AccessoryServiceImpl extends ServiceImpl<AccessoryDao, AccessoryEntity> implements AccessoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AccessoryEntity> page = this.page(
                new Query<AccessoryEntity>().getPage(params),
                new QueryWrapper<AccessoryEntity>()
        );

        return new PageUtils(page);
    }

}