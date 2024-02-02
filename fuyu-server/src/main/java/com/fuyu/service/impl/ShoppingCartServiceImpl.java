package com.fuyu.service.impl;

import com.fuyu.context.BaseContext;
import com.fuyu.dto.ShoppingCartDTO;
import com.fuyu.entity.ShoppingCart;
import com.fuyu.mapper.ShoppingCartMapper;
import com.fuyu.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车业务实现
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //创建实体类shoppingCart，并对实体类进行赋值
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //只能查询自己的购物车数据 userid
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);


        //1.判断当前加入到购物车中的商品是否已经存在  不同的用户拥有自己的购物车数据
        //判断当前商品是否在购物车中
        //要么查到一条数据即只有一个购物车，要么就查不到数据，没有购物车
        if(shoppingCarts!=null&& shoppingCarts.size()==1){
            //如果购物车已经存在，就更新数量，数量加1
            shoppingCart = shoppingCarts.get(0);
            Integer number = shoppingCart.getNumber(); //菜品数量 或者套餐数量 ？？？？
            shoppingCart.setNumber(number+1);
            //数量增加1之后，需要对购物车商品数量进行更新 update shoppingCart set number=? where id=?
            shoppingCartMapper.updateNumberById(shoppingCart); // 根据 购物车id 进行更新
        }

        //2.如果已经存在，只需要将数据添加一

        //3.如果不存在，需要插入一条购物车数据




    }
}
