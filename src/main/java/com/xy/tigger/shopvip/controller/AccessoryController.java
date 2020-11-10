package com.xy.tigger.shopvip.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xy.tigger.shopvip.entity.AccessoryEntity;
import com.xy.tigger.shopvip.service.AccessoryService;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:14
 */
@Slf4j
@RestController
@RequestMapping("shopvip/accessory")
public class AccessoryController {
    @Autowired
    private AccessoryService accessoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("shopvip:accessory:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = accessoryService.queryPage(params);
        log.error("cuowo");
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("shopvip:accessory:info")
    public R info(@PathVariable("id") Integer id){
		AccessoryEntity accessory = accessoryService.getById(id);

        return R.ok().put("accessory", accessory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("shopvip:accessory:save")
    public R save(@RequestBody AccessoryEntity accessory){
		accessoryService.save(accessory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("shopvip:accessory:update")
    public R update(@RequestBody AccessoryEntity accessory){
		accessoryService.updateById(accessory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("shopvip:accessory:delete")
    public R delete(@RequestBody Integer[] ids){
		accessoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
