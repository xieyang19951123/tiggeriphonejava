package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.TryVideoEntity;
import com.xy.tigger.shopvip.entity.vo.CourseVo;
import com.xy.tigger.shopvip.entity.vo.TryVideoVo;

import java.util.List;

public interface TryVideoService extends IService<TryVideoEntity> {
    List<TryVideoVo> getVideo();

    TryVideoVo getTryVideoPayauth(Integer id);

    List<TryVideoVo> getRelevant(String cid);

    List<TryVideoVo> getTryvideo(String val);

}
