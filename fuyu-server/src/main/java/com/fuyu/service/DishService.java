package com.fuyu.service;
import com.fuyu.dto.DishDTO;
import com.fuyu.dto.DishPageQueryDTO;
import com.fuyu.entity.Dish;
import com.fuyu.result.PageResult;
import com.fuyu.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
   public   void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     *  批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);


    /**
     * 根据id查询菜品和口味数据
     * @param id
     * @return
     */
    DishVO getDishByIdWithFlavor(Long id);

    /**
     *  根据id修改菜品基本信息和对应的口味信息
     * @param dishDTO
     */
    void updateDishWithFlavor(DishDTO dishDTO);

 /**
  * 条件查询菜品和口味
  * @param dish
  * @return
  */
 List<DishVO> listWithFlavor(Dish dish);

    /**
     * 菜品起售停售
     * @param status
     * @param id
     */

    void startOrStop(Integer status, Long id);
}
