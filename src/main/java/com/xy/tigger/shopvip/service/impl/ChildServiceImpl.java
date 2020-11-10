package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.ChildDao;
import com.xy.tigger.shopvip.entity.Child;
import com.xy.tigger.shopvip.service.ChildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChildServiceImpl extends ServiceImpl<ChildDao, Child> implements ChildService {

    @Autowired
    private  ChildDao childDao;

}
