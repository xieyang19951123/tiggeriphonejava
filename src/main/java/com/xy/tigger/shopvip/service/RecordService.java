package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.shopvip.entity.RecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:13
 */
public interface RecordService extends IService<RecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ShopUserVip> selectStudent(Integer id);
}

