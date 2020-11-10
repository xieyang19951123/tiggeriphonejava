package com.xy.tigger.shopvip.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.shopvip.entity.ShoppingTrolley;
import com.xy.tigger.shopvip.entity.vo.CommentVo;
import com.xy.tigger.shopvip.entity.vo.ShoppingTrolleyVo;
import com.xy.tigger.shopvip.service.ShoppingTrolleyService;
import com.xy.tigger.uitls.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/shop/shopping")
@RestController
public class ShoppingTrolleyController {

    @Autowired
    private ShoppingTrolleyService shoppingTrolleyService;


    //加入购物车
    @PostMapping("insertShopping")
    public R insertShopping(@RequestBody ShoppingTrolley shoppingTrolley){
        if(shoppingTrolley == null){
            return  R.ok();
        }
        shoppingTrolleyService.insertShopping(shoppingTrolley);
        return R.ok();
    }

    //获取购物车
    @GetMapping("selectShoppings")
    public R selectShoppings(@RequestParam("id")Integer id){
        if(id ==null){
            return R.error();
        }
        List<ShoppingTrolleyVo> shoppingTrolleyVos = shoppingTrolleyService.selectShoppings(id);


        return R.ok().put("page",shoppingTrolleyVos);
    }

    //更新购物车
    @PostMapping("updateShopping")
    public R updateShopping(@RequestBody ShoppingTrolleyVo shoppingTrolleyVo){
        shoppingTrolleyService.updateShopping(shoppingTrolleyVo);
        return R.ok();
    }

    @PostMapping("deletedShopping")
    public R deletedShopping(@RequestBody String str){
        if(StringUtils.isEmpty(str)){
            return R.error();
        }

        String ids = JSON.parseObject(str).getString("ids");
        List<ShoppingTrolleyVo> shoppingTrolleyVos = JSONArray.parseArray(ids, ShoppingTrolleyVo.class);
        shoppingTrolleyService.deletedShopping(shoppingTrolleyVos);
        System.out.println(shoppingTrolleyVos);
        return R.ok();
    }


}
