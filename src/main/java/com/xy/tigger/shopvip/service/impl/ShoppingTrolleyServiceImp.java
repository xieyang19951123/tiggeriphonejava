package com.xy.tigger.shopvip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.tigger.shopvip.dao.ParentDao;
import com.xy.tigger.shopvip.dao.ShoppingTrolleyDao;
import com.xy.tigger.shopvip.entity.ParentEntity;
import com.xy.tigger.shopvip.entity.ShoppingTrolley;
import com.xy.tigger.shopvip.entity.vo.ShoppingTrolleyVo;
import com.xy.tigger.shopvip.service.ParentService;
import com.xy.tigger.shopvip.service.ShoppingTrolleyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingTrolleyServiceImp extends ServiceImpl<ShoppingTrolleyDao, ShoppingTrolley> implements ShoppingTrolleyService {

    @Autowired
    private ShoppingTrolleyDao shoppingTrolleyDao;

    @Override
    public void insertShopping(ShoppingTrolley shoppingTrolley) {
        shoppingTrolley.setShowStatus(1);
        List<ShoppingTrolley> shoppingTrolleys = shoppingTrolleyDao.selectList(new QueryWrapper<>(shoppingTrolley));
        if(shoppingTrolleys.size()>0){
            shoppingTrolleys.get(0).setTotal(shoppingTrolleys.get(0).getTotal()+1);
            shoppingTrolleyDao.updateById(shoppingTrolleys.get(0));
        }else{
            shoppingTrolley.setTotal(1);
            shoppingTrolleyDao.insert(shoppingTrolley);
        }
    }

    @Override
    public List<ShoppingTrolleyVo> selectShoppings(Integer id) {
        return shoppingTrolleyDao.selectShoppings(id);
    }

    @Override
    public void updateShopping(ShoppingTrolleyVo shoppingTrolleyVo) {

        ShoppingTrolley shoppingTrolley = new ShoppingTrolley();

        shoppingTrolley.setId(shoppingTrolleyVo.getId());
        shoppingTrolley.setTotal(shoppingTrolleyVo.getTotal());
        shoppingTrolley.setShowStatus(1);
        shoppingTrolley.setPrice(shoppingTrolleyVo.getPrice());
        shoppingTrolley.setProductId(shoppingTrolleyVo.getProductId());
        shoppingTrolley.setUId(shoppingTrolleyVo.getUId());

        shoppingTrolleyDao.updateById(shoppingTrolley);
    }

    @Override
    public void deletedShopping(List<ShoppingTrolleyVo> shoppingTrolleyVos) {
        List<Integer> ids = new ArrayList<>();
        shoppingTrolleyVos.forEach(item->{
            if(item.getId() !=null){
                ids.add(item.getId());
            }
        });
        shoppingTrolleyDao.deleteBatchIds(ids);
    }


}
