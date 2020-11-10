package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.vo.CommentVo;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.shopvip.entity.CommentEntity;
import com.xy.tigger.uitls.R;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:13
 */
public interface CommentService extends IService<CommentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CommentVo> getComment();


    CommentVo selectCommentById(Integer id);

    R getrecommend(String cid);

}

