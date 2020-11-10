package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.OrganEntity;
import com.xy.tigger.shopvip.entity.PayVideoEntity;
import com.xy.tigger.shopvip.entity.vo.OragnVo;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.uitls.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrganService extends IService<OrganEntity> {
    List<OragnVo> getOrgan(Integer cid);

   R getOrganVideo(Integer oid);

    List<OragnVo> getOrganByid(Integer oid);

    OrganEntity login(ShopUserVip shopUserVip, HttpServletRequest request);

    List<OrganEntity> getMyOrgan(Integer uid);

    List<OragnVo> getOrganCategory(Integer cid);


    List<OragnVo> getOrganVal(String val);

}
