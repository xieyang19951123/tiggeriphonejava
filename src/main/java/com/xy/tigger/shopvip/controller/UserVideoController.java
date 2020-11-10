package com.xy.tigger.shopvip.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.tigger.shopvip.dao.CourseDao;
import com.xy.tigger.shopvip.entity.UserVideo;
import com.xy.tigger.shopvip.entity.vo.CourseVo;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import com.xy.tigger.shopvip.service.PayVideoService;
import com.xy.tigger.shopvip.service.UserVideoService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("shop/uservideo")
public class UserVideoController {

    @Autowired
    private UserVideoService userVideoService;

    @Autowired
    private PayVideoService payVideoService;


    @Autowired
    private CourseDao courseDao;


    @GetMapping("getUserVideoByid")
    public R getUserVideoByid(@RequestParam("uid")Integer uid,
                              @RequestParam("cid")Integer cid
                              ){
        if(uid == null || cid == null){
            return R.error("参数为空");
        }
        BaseMapper<UserVideo> baseMapper = userVideoService.getBaseMapper();
        UserVideo userVideo = new UserVideo();
        userVideo.setUId(uid);
        userVideo.setVideoId(cid);
        userVideo.setShowStatus(1);
        UserVideo userVideo1 = baseMapper.selectOne(new QueryWrapper<>(userVideo));
        return R.ok().put("page",userVideo1);
    }

    @GetMapping("getUserVideoByuid")
    public R getUserVideoByuid(@RequestParam("uid")Integer uid){
        if(uid == null){
            return R.error("参数为空");
        }
        UserVideo userVideo = new UserVideo();
        userVideo.setUId(uid);
        BaseMapper<UserVideo> baseMapper = userVideoService.getBaseMapper();
        List<UserVideo> userVideo1 = baseMapper.selectList(new QueryWrapper<>(userVideo));
        List<PayVideoVo> payVideoVos = new ArrayList<>();
        ArrayList<Object> courses = new ArrayList<>();
        userVideo1.stream().forEach(item->{
            if(item.getOType()==1){
                PayVideoVo payVideoByid = payVideoService.getPayVideoByid(item.getVideoId());
                if(payVideoByid!=null){

                    payVideoVos.add( payVideoByid);
                }
            }
            if(item.getOType()==2){
                List<CourseVo> courseRecommend = courseDao.getCourseRecommend(item.getVideoId(), null,null);
                courses.add(courseRecommend.get(0));
            }
        });
        return R.ok().put("page",payVideoVos).put("class",courses);
    }
}
