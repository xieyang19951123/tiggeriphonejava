package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.CourseEntity;
import com.xy.tigger.shopvip.entity.OrderEntity;
import com.xy.tigger.shopvip.entity.vo.CourseVo;
import com.xy.tigger.uitls.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CourseService extends IService<CourseEntity> {
    List<CourseVo> getCourseRecommend(Integer cid);


    R getCourseById(Integer id);

    List<CourseVo> getRelevant(Integer id);

    R byCourseOrder(OrderEntity orderEntity, HttpServletRequest httpServletRequest);

    List<CourseVo> getCourseByval(String val);

}
