package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.Query;

import com.xy.tigger.shopvip.dao.AddressDao;
import com.xy.tigger.shopvip.entity.AddressEntity;
import com.xy.tigger.shopvip.service.AddressService;
import sun.awt.windows.WWindowPeer;


@Service("addressService")
public class AddressServiceImpl extends ServiceImpl<AddressDao, AddressEntity> implements AddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AddressEntity> page = this.page(
                new Query<AddressEntity>().getPage(params),
                new QueryWrapper<AddressEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<AddressEntity> getAddress(Integer uid) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setShowStatus(1);
        addressEntity.setUId(uid);
        List<AddressEntity> addressEntities = baseMapper.selectList(new QueryWrapper<AddressEntity>(addressEntity));
        return addressEntities;
    }

}