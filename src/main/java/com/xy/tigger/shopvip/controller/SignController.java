package com.xy.tigger.shopvip.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.dao.RuleDao;
import com.xy.tigger.shopvip.dao.ShopUserDao;
import com.xy.tigger.shopvip.entity.Rule;
import com.xy.tigger.shopvip.entity.ShopUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xy.tigger.shopvip.entity.SignEntity;
import com.xy.tigger.shopvip.service.SignService;
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
@RequestMapping("shopvip/sign")
public class SignController {
    @Autowired
    private SignService signService;

    @Autowired
    private RuleDao ruleDao;

    @Autowired
    private ShopUserDao shopUserDao;



    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("shopvip:sign:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = signService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("shopvip:sign:info")
    public R info(@PathVariable("id") Integer id){
		SignEntity sign = signService.getById(id);

        return R.ok().put("sign", sign);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("shopvip:sign:save")
    public R save(@RequestBody SignEntity sign){
		signService.save(sign);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("shopvip:sign:update")
    public R update(@RequestBody SignEntity sign){
		signService.updateById(sign);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("shopvip:sign:delete")
    public R delete(@RequestBody Integer[] ids){
		signService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    //进行签到
    @RequestMapping("sgin/{uid}")
    public R sign(@PathVariable Integer uid){
        if(uid == null){
            return R.error("签到失败");
        }
        SignEntity signEntity = new SignEntity();
        signEntity.setShowStatus(1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        signEntity.setSignTime(format);
        signEntity.setUId(uid);
        BaseMapper<SignEntity> baseMapper = signService.getBaseMapper();
        SignEntity signEntity1 = baseMapper.selectOne(new QueryWrapper<>(signEntity));
        if(signEntity1!= null){
            return R.ok("已签到");
        }
        Rule rule = ruleDao.selectById(4);
        ShopUserEntity shopUserEntity = shopUserDao.selectById(uid);
        shopUserEntity.setPoint(shopUserEntity.getPoint().add(rule.getPoint()));
        shopUserDao.updateById(shopUserEntity);
        baseMapper.insert(signEntity);
        return R.ok("签到成功,获得"+rule.getPoint()+"积分");
    }

}
