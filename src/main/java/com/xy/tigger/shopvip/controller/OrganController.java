package com.xy.tigger.shopvip.controller;

import com.xy.tigger.shopvip.entity.OrganEntity;
import com.xy.tigger.shopvip.entity.PayVideoEntity;
import com.xy.tigger.shopvip.entity.vo.OragnVo;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import com.xy.tigger.shopvip.service.OrganService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("shopvip/organ")
public class OrganController {

    @Autowired
    private OrganService organService;

    @GetMapping("organ/{cid}")
    public  R getOrgan(@PathVariable Integer cid){
       List<OragnVo> organEntities =  organService.getOrgan(cid);
       return R.ok().put("page",organEntities);
    }

    @GetMapping("getOrganVideo")
    public R getOrganVideo(@RequestParam("oid")Integer oid){
        if(oid == null){
            return R.error("oid为空");
        }
        return organService.getOrganVideo(oid);
    }


    @GetMapping("getOrganByid")
    public R getOrganByid(@RequestParam("oid")Integer oid){
        if(oid == null){
            return R.error("oid为空");
        }
        List<OragnVo> organEntity = organService.getOrganByid(oid);
        return R.ok().put("page",organEntity);
    }

    @GetMapping("getMyorgan")
    public R getMyorgan(@RequestParam("uid")Integer uid){
        List<OrganEntity> organEntities = organService.getMyOrgan(uid);
        return  R.ok().put("page",organEntities);
    }


    @RequestMapping(value = "getOrganCategory",method = RequestMethod.GET)
    public R getOrganCategory(@RequestParam("cid")Integer cid){
        List<OragnVo> oragnVos = organService.getOrganCategory(cid);
        return R.ok().put("page",oragnVos);
    }

}
