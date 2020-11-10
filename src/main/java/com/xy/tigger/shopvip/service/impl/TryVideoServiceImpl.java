package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.CategoryDao;
import com.xy.tigger.shopvip.dao.PayVideoDao;
import com.xy.tigger.shopvip.dao.TryVideoDao;
import com.xy.tigger.shopvip.entity.TryVideoEntity;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import com.xy.tigger.shopvip.entity.vo.TryVideoVo;
import com.xy.tigger.shopvip.service.TryVideoService;
import com.xy.tigger.uitls.VideoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("tryVideoService")
public class TryVideoServiceImpl extends ServiceImpl<TryVideoDao, TryVideoEntity> implements TryVideoService {

    @Autowired
    private TryVideoDao tryVideoDao;

    @Autowired
    private PayVideoDao payVideoDao;

    @Autowired
    private VideoUtils videoUtils;


    @Override
    public List<TryVideoVo> getVideo() {
        List<TryVideoVo> videos = tryVideoDao.getVideo();
        List<TryVideoVo> collect = videos.stream().map(item -> {
            String s = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list = payVideoDao.selectCategroy(s);
            item.setName(list);
            return item;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public TryVideoVo getTryVideoPayauth(Integer id) {
        TryVideoEntity tryVideoEntity =  tryVideoDao.selectById(id);
        String videoVoucher = videoUtils.getVideoVoucher(tryVideoEntity.getVideoId());
        TryVideoVo tryVideoVo = new TryVideoVo();
        tryVideoVo.setPlayAuth(videoVoucher);
        tryVideoVo.setCid(tryVideoEntity.getCid());
        tryVideoVo.setIntroduce(tryVideoEntity.getIntroduce());
        tryVideoVo.setVideoId(tryVideoEntity.getVideoId());
        return tryVideoVo;
    }

    @Override
    public List<TryVideoVo> getRelevant(String cid) {
        String s = cid.replaceAll("\\[", "(").replaceAll("]", ")").replaceAll(" ","");
        List<String> list = Arrays.asList(s.substring(1, s.length()).substring(0, s.length() - 2).split(","));//list 化

        List<TryVideoVo> tryVideoEntities = tryVideoDao.getVideoRelevant();
        List<TryVideoVo> collect = tryVideoEntities.stream().filter(item -> {
            String str =  item.getCid().replaceAll(" ","");
            List<String> list1 = Arrays.asList(str.substring(1, str.length()).substring(0,str.length() - 2).split(","));

            List<String> collect1 = list1.stream().filter(li ->list.contains(li)).collect(Collectors.toList());

            return collect1.size()>0;
        }).map(item->{
            String s1 = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list1 = payVideoDao.selectCategroy(s1);
            item.setName(list1);
            return item;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<TryVideoVo> getTryvideo(String val) {
        List<TryVideoVo> videos = tryVideoDao.getVideoAll(val);
        List<TryVideoVo> collect = videos.stream().map(item -> {
            String s = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list = payVideoDao.selectCategroy(s);
            item.setName(list);
            return item;
        }).collect(Collectors.toList());
        return collect;
    }


}
