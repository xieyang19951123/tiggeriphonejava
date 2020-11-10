package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.UserVideoDao;
import com.xy.tigger.shopvip.entity.UserVideo;
import com.xy.tigger.shopvip.service.UserVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserVideoServiceImpl extends ServiceImpl<UserVideoDao, UserVideo> implements UserVideoService {


    @Autowired
    private UserVideoDao userVideoDao;
}
