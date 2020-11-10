package com.xy.tigger.shopvip.service.impl;

import com.xy.tigger.shopvip.dao.RecommendDao;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.Query;

import com.xy.tigger.shopvip.dao.RecordDao;
import com.xy.tigger.shopvip.entity.RecordEntity;
import com.xy.tigger.shopvip.service.RecordService;


@Service("recordService")
public class RecordServiceImpl extends ServiceImpl<RecordDao, RecordEntity> implements RecordService {

    @Autowired
    private RecordDao recordDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RecordEntity> page = this.page(
                new Query<RecordEntity>().getPage(params),
                new QueryWrapper<RecordEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ShopUserVip>  selectStudent(Integer id) {
       List<ShopUserVip> shopUserVips =  recordDao.selectAllStudent(id);
       return  shopUserVips;
    }

}