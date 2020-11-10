package com.xy.tigger.shopvip.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.InterceptingHttpAccessor;
import org.springframework.web.bind.annotation.*;

import com.xy.tigger.shopvip.entity.AddressEntity;
import com.xy.tigger.shopvip.service.AddressService;
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
@RequestMapping("shopvip/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("shopvip:address:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = addressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("shopvip:address:info")
    public R info(@PathVariable("id") Integer id){
		AddressEntity address = addressService.getById(id);

        return R.ok().put("address", address);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("shopvip:address:save")
    public R save(@RequestBody AddressEntity address){
        address.setShowStatus(1);
		addressService.save(address);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("shopvip:address:update")
    public R update(@RequestBody AddressEntity address){
		addressService.updateById(address);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("shopvip:address:delete")
    public R delete(@RequestBody Integer[] ids){
		addressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    @GetMapping("getAddress")
    public R getAddress(@RequestParam("uid")Integer uid){
        List<AddressEntity> address = addressService.getAddress(uid);
        if(address==null||address.size()<0){
            return R.error();
        }

        return R.ok().put("page",address);
    }
}
