package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.ParentDao;
import com.xy.tigger.shopvip.entity.ParentEntity;
import com.xy.tigger.shopvip.service.ParentService;
import org.springframework.stereotype.Service;

@Service
public  class ParentServiceImpl extends ServiceImpl<ParentDao, ParentEntity> implements ParentService {
}
