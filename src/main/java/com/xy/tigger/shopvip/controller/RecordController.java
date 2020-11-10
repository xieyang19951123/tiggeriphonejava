package com.xy.tigger.shopvip.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xy.tigger.shopvip.entity.RecordEntity;
import com.xy.tigger.shopvip.service.RecordService;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:13
 */
@RestController
@RequestMapping("shopvip/record")
public class RecordController {
    @Autowired
    private RecordService recordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("shopvip:record:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = recordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("shopvip:record:info")
    public R info(@PathVariable("id") Integer id){
		RecordEntity record = recordService.getById(id);

        return R.ok().put("record", record);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("shopvip:record:save")
    public R save(@RequestBody RecordEntity record){
		recordService.save(record);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("shopvip:record:update")
    public R update(@RequestBody RecordEntity record){
		recordService.updateById(record);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("shopvip:record:delete")
    public R delete(@RequestBody Integer[] ids){
		recordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @GetMapping("getstudyplan")
    public R getstudyplan(@RequestParam("oid")Integer oid,
                          @RequestParam("uid")Integer uid){
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setShowStatus(1);
        recordEntity.setUId(uid);
        recordEntity.setBoosId(oid);
        BaseMapper<RecordEntity> baseMapper = recordService.getBaseMapper();
        RecordEntity recordEntity1 = baseMapper.selectOne(new QueryWrapper<>(recordEntity));
        return R.ok().put("page",recordEntity1);
    }

    @RequestMapping(value = "getRecold",method = RequestMethod.GET)
    public R getRecold(@RequestParam("id")Integer id){
        List<ShopUserVip> shopUserVips = recordService.selectStudent(id);
        return R.ok().put("page",shopUserVips);
    }
}
