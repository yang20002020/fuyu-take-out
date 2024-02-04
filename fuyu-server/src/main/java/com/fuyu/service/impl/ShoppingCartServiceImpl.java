package com.fuyu.service.impl;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fuyu.context.BaseContext;
import com.fuyu.dto.ShoppingCartDTO;
import com.fuyu.entity.Dish;
import com.fuyu.entity.Setmeal;
import com.fuyu.entity.ShoppingCart;
import com.fuyu.mapper.DishMapper;
import com.fuyu.mapper.SetmealMapper;
import com.fuyu.mapper.ShoppingCartMapper;
import com.fuyu.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车业务实现
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {




    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        /*
           类 ShoppingCart
            private Long id;
            //名称
            private String name;
            //用户id
            private Long userId;
            //菜品id
            private Long dishId;
            //套餐id
            private Long setmealId;
            //口味
            private String dishFlavor;
            //数量
            private Integer number;
            //金额
            private BigDecimal amount;
            //图片
            private String image;
            private LocalDateTime createTime;
         */
        //创建实体类shoppingCart，并对实体类进行赋值
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //只能查询自己的购物车数据 userid
        shoppingCart.setUserId(BaseContext.getCurrentId());
        /*
            //用户id
            private Long userId;
            //菜品id
            private Long dishId;
            //套餐id
            private Long setmealId;
            //口味
            private String dishFlavor;
            已赋值
         */
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);

        //1.判断当前加入到购物车中的商品是否已经存在  不同的用户拥有自己的购物车数据
        //判断当前商品是否在购物车中
        //要么查到一条数据即只有一个购物车，要么就查不到数据，没有购物车
        if(shoppingCarts!=null&& shoppingCarts.size()==1){
            //2.如果已经存在，只需要将数据添加一
            //如果购物车已经存在，就更新数量，数量加1
            shoppingCart = shoppingCarts.get(0);
            Integer number = shoppingCart.getNumber(); //菜品数量 或者套餐数量 ？？？？
            shoppingCart.setNumber(number+1);
            //数量增加1之后，需要对购物车商品数量进行更新 update shoppingCart set number=? where id=?
            shoppingCartMapper.updateNumberById(shoppingCart); // 根据 购物车id 进行更新
        }else{
            //3.如果不存在，需要插入一条购物车数据
            //判断当前添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if(dishId!=null){
                //添加到购物车的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                   /*
                        //名称
                        private String name;
                        //金额
                        private BigDecimal amount;
                        //图片
                        private String image;
                  */
            }else{
                //添加到购物车的是套餐 //dishid为空，SetmealId就不为空
                Setmeal setMeal = setmealMapper.getSetMealBySetMealId(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setMeal.getName());
                shoppingCart.setImage(setMeal.getImage());
                shoppingCart.setAmount(setMeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart); //统一插入数据
        }

    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        //根据用户id查询购物车
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        List<ShoppingCart> shoppingCarts=  shoppingCartMapper.list(shoppingCart);
        return shoppingCarts;
    }
}
