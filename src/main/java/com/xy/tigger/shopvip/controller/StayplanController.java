package com.xy.tigger.shopvip.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.tigger.shopvip.entity.StayplanEntity;
import com.xy.tigger.shopvip.service.StayplanService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("tigger/stayplan")
public class StayplanController {

    @Autowired
    private StayplanService stayplanService;


    @RequestMapping("list")
    public R list(Integer uid){

        StayplanEntity stayplanEntity = new StayplanEntity();
        stayplanEntity.setUid(uid);
        List<StayplanEntity> list = stayplanService.list(new QueryWrapper<>(stayplanEntity));
        return R.ok().put("page",list);
    }

    @RequestMapping("edit")
    public R edit(@RequestBody StayplanEntity stayplanEntity ){
        stayplanService.updateById(stayplanEntity);
        return R.ok();
    }

    @RequestMapping("save")
    public R save(@RequestBody StayplanEntity stayplanEntity){
        stayplanService.save(stayplanEntity);
        if(stayplanEntity.getId() == null){
            return R.error("保存失败");
        }
        return R.ok();
    }

    @RequestMapping("deleted")
    public R deleted(@RequestBody Integer[] ids){
        stayplanService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("getbyid")
    public  R getbyid(@RequestParam Integer id){
        StayplanEntity byId = stayplanService.getById(id);
        return R.ok().put("page",byId);
    }
}
