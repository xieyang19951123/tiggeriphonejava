package com.xy.tigger.shopvip.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.uitls.PageUtils;
import com.xy.tigger.uitls.Query;

import com.xy.tigger.shopvip.dao.CategoryDao;
import com.xy.tigger.shopvip.entity.CategoryEntity;
import com.xy.tigger.shopvip.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> selectCategory(Integer type) {
        CategoryEntity categoryEntity1 = new CategoryEntity();
        categoryEntity1.setAStatus(type);
        categoryEntity1.setShowStatus(1);
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<>(categoryEntity1));

        List<CategoryEntity> collect = categoryEntities.stream().filter(categoryEntity -> {
            return categoryEntity.getParentId() == 0;
        }).map(muen -> {
            muen.setChidrens(getChildrens(muen, categoryEntities));
            return muen;
        }).collect(Collectors.toList());
        return collect;
    }

    public  List<CategoryEntity> getChildrens(CategoryEntity in,List<CategoryEntity> all){
        List<CategoryEntity> collect = all.stream().filter(categoryEntity -> {
            return in.getId() == categoryEntity.getParentId();
        }).map(muen -> {
            muen.setChidrens(getChildrens(muen, all));
            return muen;
        }).collect(Collectors.toList());

        return collect;
    }

}