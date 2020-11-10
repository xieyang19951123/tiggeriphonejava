package com.xy.tigger.shopvip.service.impl;

import com.xy.tigger.shopvip.dao.PayVideoDao;
import com.xy.tigger.shopvip.entity.vo.CommentVo;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.Query;

import com.xy.tigger.shopvip.dao.CommentDao;
import com.xy.tigger.shopvip.entity.CommentEntity;
import com.xy.tigger.shopvip.service.CommentService;


@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private PayVideoDao payVideoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CommentEntity> page = this.page(
                new Query<CommentEntity>().getPage(params),
                new QueryWrapper<CommentEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CommentVo> getComment() {
        List<CommentVo> commentVos = commentDao.selectComment();
        List<CommentVo> collect = commentVos.stream().map(item -> {
            String s = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list = payVideoDao.selectCategroy(s);
            item.setName(list);
            String id = item.getFjId().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> accessoryEntities = commentDao.getFjsrc(id);
            item.setAccessoryEntities(accessoryEntities);
            return item;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public CommentVo selectCommentById(Integer id) {
        CommentVo commentVo = commentDao.selectCommentById(id);
        String fjId = commentVo.getFjId().replaceAll("\\[", "(").replaceAll("]", ")");;
        List<String> accessoryEntities = commentDao.getFjsrc(fjId);
        commentVo.setAccessoryEntities(accessoryEntities);
        return commentVo;
    }

    @Override
    public R getrecommend(String cid) {
        List<CommentVo> commentVos = commentDao.selectComment();
        List<CommentVo> collect = commentVos.stream().filter(item -> {
            List<String> s = Arrays.asList(cid.replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "").split(","));
            ArrayList<Object> objects = new ArrayList<>();
            s.forEach(s1 -> {
                if (item.getCid().contains(s1)) objects.add(s1);
            });
            return objects.size() > 0;
        }).collect(Collectors.toList());
        return R.ok().put("page",collect);
    }


}