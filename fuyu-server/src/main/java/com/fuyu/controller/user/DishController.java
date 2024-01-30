package com.fuyu.controller.user;

import com.fuyu.constant.StatusConstant;
import com.fuyu.entity.Dish;
import com.fuyu.result.Result;
import com.fuyu.service.DishService;
import com.fuyu.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
    /*
        // 没有添加redis时的逻辑
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
        List<DishVO> list = dishService.listWithFlavor(dish);
        return Result.success(list);

     */
        //构造redis中的key，规则：dish_分类id
        String key = "dish_" + categoryId;

        //查询redis中是否存在菜品数据
         //如果存在
      List<DishVO>  list=  (List<DishVO>) redisTemplate.opsForValue().get(key);
      if(list!=null && list.size()>0){
          //如果存在，直接返回，无须查询数据库
         return Result.success(list);
      }
     /***********没有添加redis时的逻辑***********/
        // 如果不存在
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
        list = dishService.listWithFlavor(dish);
    /****************************************/
        //如果不存在，查询数据库，将查询到的数据放入redis中
        redisTemplate.opsForValue().set(key,list);

        return Result.success(list);

    }

}
