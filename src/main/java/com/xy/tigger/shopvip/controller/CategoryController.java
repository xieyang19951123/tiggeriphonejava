package com.xy.tigger.shopvip.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xy.tigger.shopvip.entity.CategoryEntity;
import com.xy.tigger.shopvip.service.CategoryService;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:14
 */
@RestController
@RequestMapping("shopvip/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("shopvip:category:list")
    public R list(@RequestParam Integer type){
       // PageUtils page = categoryService.queryPage(params);
        List<CategoryEntity> page = categoryService.selectCategory(type);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("shopvip:category:info")
    public R info(@PathVariable("id") Integer id){
		CategoryEntity category = categoryService.getById(id);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("shopvip:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("shopvip:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("shopvip:category:delete")
    public R delete(@RequestBody Integer[] ids){
		categoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
