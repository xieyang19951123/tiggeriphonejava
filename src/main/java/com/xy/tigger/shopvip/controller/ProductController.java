package com.xy.tigger.shopvip.controller;


import com.xy.tigger.shopvip.entity.vo.CommentVo;
import com.xy.tigger.shopvip.service.CommentService;
import com.xy.tigger.shopvip.service.ProductService;
import com.xy.tigger.uitls.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shop/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/getProducts")
    public List<CommentVo> getProducts(){
        List<CommentVo> commentVos = productService.getProduct(null,1,null);
        return commentVos;
    }


    @GetMapping("getproductbycategoryid")
    public R getproductbycategoryid(@RequestParam("cid")String cid){
        List<CommentVo> commentVos = productService.getProduct(cid,null,null);
        return R.ok().put("page",commentVos);
    }

    @GetMapping("getproductbycategoryval")
    public R getproductbycategoryval(@RequestParam("val")String val){
        List<CommentVo> commentVos = productService.getProduct(null,null,val);
        return R.ok().put("page",commentVos);
    }
}
