package com.xy.tigger.shopvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.tigger.shopvip.entity.ShoppingTrolley;
import com.xy.tigger.shopvip.entity.vo.CommentVo;
import com.xy.tigger.shopvip.entity.vo.ShoppingTrolleyVo;

import java.util.List;

public interface ShoppingTrolleyService  extends IService<ShoppingTrolley> {
    void insertShopping(ShoppingTrolley shoppingTrolley);

    List<ShoppingTrolleyVo> selectShoppings(Integer id);

    void updateShopping(ShoppingTrolleyVo shoppingTrolleyVo);

    void deletedShopping(List<ShoppingTrolleyVo> shoppingTrolleyVos);
}
