package com.xy.tigger.shopvip.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.xy.tigger.shopvip.entity.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xy.tigger.shopvip.entity.CommentEntity;
import com.xy.tigger.shopvip.service.CommentService;
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
@RequestMapping("shopvip/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("shopvip:comment:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = commentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("shopvip:comment:info")
    public R info(@PathVariable("id") Integer id){
		CommentEntity comment = commentService.getById(id);

        return R.ok().put("comment", comment);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("shopvip:comment:save")
    public R save(@RequestBody CommentEntity comment){
		commentService.save(comment);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("shopvip:comment:update")
    public R update(@RequestBody CommentEntity comment){
		commentService.updateById(comment);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("shopvip:comment:delete")
    public R delete(@RequestBody Integer[] ids){
		commentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @GetMapping("/selectComment")
    public R selectComment(){
        List<CommentVo> commentVos = commentService.getComment();
        if(commentVos==null ||commentVos == null){
            return R.error();
        }
        return  R.ok().put("page",commentVos);
    }

    @GetMapping("selectCommentById")
    public R selectCommentById(@RequestParam("id")Integer id){
        if(id == null){
            return  R.error();
        }
        CommentVo commentVos =  commentService.selectCommentById(id);
        if(commentVos==null){
            return R.error();
        }
        return R.ok().put("page",commentVos);
    }


    @RequestMapping("/getrecommend")
    public  R getrecommend(@RequestParam String cid){
        return commentService.getrecommend(cid);
    }



}
