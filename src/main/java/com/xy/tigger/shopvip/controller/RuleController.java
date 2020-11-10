package com.xy.tigger.shopvip.controller;

import com.xy.tigger.shopvip.dao.RecommendDao;
import com.xy.tigger.shopvip.dao.RuleDao;
import com.xy.tigger.shopvip.entity.Recommend;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tigger/rule")
@RestController
public class RuleController {

    @Autowired
    private RuleDao ruleDao;

    @Autowired
    private RecommendDao recommendDao;

    @RequestMapping(value = "getRule",method = RequestMethod.GET)
    public R getRule(@RequestParam("id")Integer id){
        return R.ok().put("page",ruleDao.selectById(id));
    }

    @RequestMapping(value = "insertRecommend",method = RequestMethod.POST)
    public R insertRecommend(@RequestBody Recommend recommend){
        recommendDao.insert(recommend);
        return R.ok();
    }
}
