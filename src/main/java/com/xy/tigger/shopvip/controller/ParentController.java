package com.xy.tigger.shopvip.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.tigger.shopvip.entity.ParentEntity;
import com.xy.tigger.shopvip.service.ParentService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("tigger/parent")
public class ParentController {

    @Autowired
    private ParentService parentService;

    @RequestMapping("/list")
    public R list(Integer uid){
        ParentEntity parentEntity = new ParentEntity();
        parentEntity.setUid(uid);
        List<ParentEntity> list = parentService.list(new QueryWrapper<>(parentEntity));
        return R.ok().put("page",list);
    }

    @RequestMapping("save")
    public R save(@RequestBody ParentEntity parentEntity){
        parentService.save(parentEntity);

        if(parentEntity.getId() == null){
            return R.error("保存失败");
        }
        return R.ok();
    }

    @RequestMapping("edit")
    public R edit(@RequestBody ParentEntity parentEntity){
        parentService.updateById(parentEntity);

        return R.ok();
    }

    @RequestMapping("deleted")
    public R deleted(@RequestBody Integer[] ids){
        parentService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("getbyid")
    public R getbyid(@RequestParam Integer id){
        ParentEntity byId = parentService.getById(id);
        return R.ok().put("page",byId);
    }
}
