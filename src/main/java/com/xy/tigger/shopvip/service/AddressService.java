package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.shopvip.entity.AddressEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:14
 */
public interface AddressService extends IService<AddressEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<AddressEntity> getAddress(Integer uid);
}

