package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.CourseDao;
import com.xy.tigger.shopvip.dao.OrganDao;
import com.xy.tigger.shopvip.dao.PayVideoDao;
import com.xy.tigger.shopvip.dao.RecordDao;
import com.xy.tigger.shopvip.entity.OrganEntity;
import com.xy.tigger.shopvip.entity.PayVideoEntity;
import com.xy.tigger.shopvip.entity.RecordEntity;
import com.xy.tigger.shopvip.entity.vo.CourseVo;
import com.xy.tigger.shopvip.entity.vo.OragnVo;
import com.xy.tigger.shopvip.entity.vo.PayVideoVo;
import com.xy.tigger.shopvip.entity.vo.ShopUserVip;
import com.xy.tigger.shopvip.service.OrganService;
import com.xy.tigger.uitls.MD5utils;
import com.xy.tigger.uitls.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganServiceImpl extends ServiceImpl<OrganDao, OrganEntity> implements OrganService {

    @Autowired
    private OrganDao organDao;

    @Autowired
    private PayVideoDao payVideoDao;

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private CourseDao courseDao;

    @Override
    public List<OragnVo> getOrgan(Integer cid) {
        List<OragnVo> organEntities = organDao.selectOrganList(null,null);
        List<OragnVo> collect = organEntities.stream().filter(item->{
            if(cid != 0){

                return  item.getCid().contains(cid.toString());
            }
            return true;
        }).map(item -> {
            item.setPassword(null);
            return item;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public R  getOrganVideo(Integer oid) {
        List<PayVideoVo> payVideoVos = organDao.selectPayvideoByoid(oid);
        List<PayVideoVo> collect = payVideoVos.stream().map(item -> {
            String s1 = item.getCid().replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list = payVideoDao.selectCategroy(s1);
            item.setName(list);
            return item;
        }).collect(Collectors.toList());
        List<CourseVo> courseRecommend = courseDao.getCourseRecommend(null, oid,null);
        return R.ok().put("video",collect).put("course",courseRecommend);
    }

    @Override
    public List<OragnVo> getOrganByid(Integer oid) {
        List<OragnVo> organEntity = organDao.selectOrganList(oid,null);
        organEntity.get(0).setPassword(null);

        return organEntity;
    }

    @Override
    public OrganEntity login(ShopUserVip shopUserVip, HttpServletRequest request) {

        OrganEntity organEntity = new OrganEntity();
        organEntity.setLoginname(shopUserVip.getPsUsername());
        organEntity.setShowStatus(1);
        String md5Str = MD5utils.getMD5Str(shopUserVip.getPsPassword());
        organEntity.setPassword(md5Str);
        OrganEntity organEntity1 = organDao.selectOne(new QueryWrapper<>(organEntity));

        return organEntity1;
    }

    @Override
    public List<OrganEntity> getMyOrgan(Integer uid) {
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setUId(uid);
        recordEntity.setShowStatus(1);
        List<RecordEntity> recordEntities = recordDao.selectList(new QueryWrapper<>(recordEntity));
        List<OrganEntity> organEntities = new ArrayList<>();
        recordEntities.stream().forEach(item->{

            List<OragnVo> oragnVos = organDao.selectOrganList(item.getBoosId(),null);
            if(oragnVos.size()>0){
                organEntities.add(oragnVos.get(0));
            }

        });

        return organEntities;
    }

    @Override
    public List<OragnVo> getOrganCategory(Integer cid) {

        List<OragnVo> oragnVos = organDao.selectOrganList(null,null);
        List<OragnVo> collect = oragnVos.stream().filter(item -> {
            String s = cid.toString();
            if(StringUtils.isEmpty(item.getCid())){
                return false;
            }
            return item.getCid().contains(s);
        }).map(item->{
            String s = item.getCid().replaceAll(" ", "").replaceAll("\\[", "(").replaceAll("]", ")");
            List<String> list = payVideoDao.selectCategroy(s);
            item.setName(list);
            return  item;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<OragnVo> getOrganVal(String val) {
        List<OragnVo> oragnVos = organDao.selectOrganList(null,val);
        List<OragnVo> collect = oragnVos.stream().map(item->{
            String s = item.getCid().replaceAll(" ", "").replaceAll("\\[", "(").replaceAll("]", ")");
            if(!s.equals("()")&& !s.equals("")){

                List<String> list = payVideoDao.selectCategroy(s);
                item.setName(list);
            }
            return  item;
        }).collect(Collectors.toList());
        return collect;
    }
}
