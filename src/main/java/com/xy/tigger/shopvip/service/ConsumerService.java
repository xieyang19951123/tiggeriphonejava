package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.R;

import javax.servlet.http.HttpServletRequest;

public interface ConsumerService  {

    /**
     * 用户进行注册
     */
    public R regist(ShopUserVip shopUserVip, HttpServletRequest request);
}
