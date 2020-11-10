package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.ShareDao;
import com.xy.tigger.shopvip.entity.ShareEntity;
import com.xy.tigger.shopvip.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareServiceImpl extends ServiceImpl<ShareDao, ShareEntity> implements ShareService {

    @Autowired
    private ShareDao shareDao;
}
