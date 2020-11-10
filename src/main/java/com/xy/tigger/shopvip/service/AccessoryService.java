package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.AccessoryEntity;
import com.xy.tigger.uitls.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:14
 */
public interface AccessoryService extends IService<AccessoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

