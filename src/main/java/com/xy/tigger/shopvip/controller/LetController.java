package com.xy.tigger.shopvip.controller;

import com.xy.tigger.shopvip.dao.LetDao;
import com.xy.tigger.shopvip.entity.LetEntity;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("shop/let")
@RestController
public class LetController {



    @Autowired
    private LetDao letDao;

    @RequestMapping(value = "inval" ,method = RequestMethod.POST)
    public R inval(@RequestBody String inval){
        inval = inval.replaceAll("\"","");
        LetEntity letEntity = new LetEntity();
        letEntity.setId(1);
        letEntity.setVal(inval);
        letDao.updateById(letEntity);
        return R.ok();
    }
    @RequestMapping("select")
    public R  selectLet(){
        LetEntity letEntity = letDao.selectById(1);
        return R.ok().put("page",letEntity);
    }
}
