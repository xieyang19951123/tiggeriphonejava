package com.xy.tigger.shopvip.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.tigger.shopvip.entity.PointRecordEntity;
import com.xy.tigger.shopvip.service.PointRecordService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tigger/pointrecord")
public class PointRecordController {

    @Autowired
    private PointRecordService pointRecordService;


    @RequestMapping("list")
    public R list(@RequestParam Integer uid){
        PointRecordEntity pointRecordEntity = new PointRecordEntity();
        pointRecordEntity.setUid(uid);
        List<PointRecordEntity> list = pointRecordService.list(new QueryWrapper<>(pointRecordEntity));
        return R.ok().put("page",list);
    }
}
