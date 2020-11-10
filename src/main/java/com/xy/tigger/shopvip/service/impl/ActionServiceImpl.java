package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.tigger.shopvip.dao.ChildDao;
import com.xy.tigger.shopvip.dao.SignDao;
import com.xy.tigger.shopvip.dao.StayplanDao;
import com.xy.tigger.shopvip.entity.Child;
import com.xy.tigger.shopvip.entity.SignEntity;
import com.xy.tigger.shopvip.entity.StayplanEntity;
import com.xy.tigger.shopvip.service.ActionService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

@Service
public class ActionServiceImpl implements ActionService {

    @Autowired
    private SignDao signDao;

    @Autowired
    private ChildDao childDao;

    @Autowired
    private StayplanDao stayplanDao;

    @Override
    public R action(Integer uid) {
        String str ="";
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SignEntity signEntity = new SignEntity();
        signEntity.setUId(uid);
        signEntity.setShowStatus(1);
        signEntity.setSignTime(format);
        SignEntity signEntity1 = signDao.selectOne(new QueryWrapper<>(signEntity));
        if(signEntity1==null){

            if(str.equals("")){

                str+="今天未打卡";
            }else {
                str+=",今天未打卡";
            }
        }
        Child child = new Child();
        child.setUid(uid);
        List<Child> children = childDao.selectList(new QueryWrapper<>(child));
        if(children.size() == 0){
            if(str.equals("")){

                str +="请填写档案";
            }else {
                str +=",请填写档案";
            }
        }
        StayplanEntity stayplanEntity = new StayplanEntity();
        stayplanEntity.setUid(uid);
        List<StayplanEntity> stayplanEntities = stayplanDao.selectList(new QueryWrapper<>(stayplanEntity));
        if(stayplanEntities.size() == 0){
            if(str.equals("")){

                str +="请填写学习计划";
            }else {
                str +=",请填写学习计划";
            }
        }


        return R.ok(str);
    }
}
