package com.xy.tigger.shopvip.controller;

import com.xy.tigger.shopvip.entity.TryVideoEntity;
import com.xy.tigger.shopvip.entity.vo.TryVideoVo;
import com.xy.tigger.shopvip.service.TryVideoService;
import com.xy.tigger.uitls.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shop/tryvideo")
public class TryVideoController {

    @Autowired
    private TryVideoService tryVideoService;

    @GetMapping("getTryVideoPayauth")
    public R getTryVideoPayauth(@RequestParam("id")Integer id){
        TryVideoVo tryVideoVo = tryVideoService.getTryVideoPayauth(id);

        if(tryVideoVo== null){
            R.error("获取凭证失败");
        }
        return R.ok().put("page",tryVideoVo);
    }

    //获取相关推荐；
    @GetMapping("/getRelevant")
    public R getRelevant(@RequestParam("cid")String cid){
        List<TryVideoVo> tryVideoVo = tryVideoService.getRelevant(cid);
        return R.ok().put("page",tryVideoVo);
    }

    //获取所有的试看视频
    @GetMapping("/gettryvideo")
    public R getTryvideo(){
        return  R.ok().put("page",tryVideoService.getTryvideo(null));
    }

}
