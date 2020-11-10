package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.shopvip.entity.SignEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:13
 */
public interface SignService extends IService<SignEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

