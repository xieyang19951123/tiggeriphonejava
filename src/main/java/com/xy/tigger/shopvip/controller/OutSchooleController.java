package com.xy.tigger.shopvip.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.tigger.shopvip.entity.OutschooleEntity;
import com.xy.tigger.shopvip.service.OutSchooleService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/tigger/outschoole")
public class OutSchooleController {

    @Autowired
    private OutSchooleService outSchooleService;


    @RequestMapping("save")
    public R save(@RequestBody OutschooleEntity outschooleEntity){
        outSchooleService.save(outschooleEntity);
        if(outschooleEntity.getId() == null){
            return R.error("保存失败");
        }
        return R.ok("保存成功");
    }

    @RequestMapping("edit")
    public  R edit(@RequestBody OutschooleEntity outschooleEntity){
        outSchooleService.updateById(outschooleEntity);
        return R.ok();
    }

    @RequestMapping("deleted")
    public R deleted(@RequestBody Integer[] ids){
        outSchooleService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("list")
    public R list(@RequestParam Integer cid){
        OutschooleEntity outschooleEntity = new OutschooleEntity();
        outschooleEntity.setCId(cid);
        List<OutschooleEntity> list = outSchooleService.list(new QueryWrapper<>(outschooleEntity));
        return R.ok().put("page",list);
    }

    @RequestMapping("getoutschoole")
    public  R getoutschoole(@RequestParam Integer id){
        OutschooleEntity byId = outSchooleService.getById(id);
        return R.ok().put("page",byId);
    }
}
