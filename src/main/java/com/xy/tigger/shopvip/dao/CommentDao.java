package com.xy.tigger.shopvip.dao;

import com.xy.tigger.shopvip.entity.CommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.entity.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-19 21:49:13
 */
@Mapper
public interface CommentDao extends BaseMapper<CommentEntity> {

    List<CommentVo> selectComment();

    List<String> getFjsrc(String id);

    CommentVo selectCommentById(Integer id);
}
