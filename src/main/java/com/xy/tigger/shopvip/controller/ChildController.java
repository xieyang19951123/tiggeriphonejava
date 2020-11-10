package com.xy.tigger.shopvip.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.Child;
import com.xy.tigger.shopvip.service.ChildService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RequestMapping("tigger/child")
@RestController
public class ChildController {

    @Autowired
    private ChildService childService;

    @RequestMapping(value = "insert",method = RequestMethod.POST)
    public R insert(@RequestBody Child child){
        child.setShowStatus(1);
        childService.save(child);
        return R.ok().put("page",child);
    };

    @RequestMapping("getlist")
    public R get(@RequestParam("uid")Integer uid){
        BaseMapper<Child> baseMapper = childService.getBaseMapper();
        Child child = new Child();
        child.setUid(uid);
        return R.ok().put("page",baseMapper.selectList(new QueryWrapper<>(child)));

    }

    @RequestMapping("edit")
    public R edit(@RequestBody Child child){
        childService.updateById(child);
        return R.ok();
    }

    @RequestMapping(value = "info/{id}",method = RequestMethod.GET)
    public R info(@PathVariable("id")Integer id){
        return R.ok().put("page",childService.getById(id));
    }


    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public  R deleted(@RequestBody Integer[] ids){

        childService.getBaseMapper().deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
