package com.xy.tigger.shopvip.controller;

import com.xy.tigger.shopvip.entity.ShareEntity;
import com.xy.tigger.shopvip.service.ShareService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tigger/share")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @RequestMapping("save")
    public R insertShare(@RequestBody ShareEntity shareEntity){
        shareService.save(shareEntity);
        if(shareEntity.getId()==null){
            return R.error("插入失败");
        }
        return R.ok();
    }
}
