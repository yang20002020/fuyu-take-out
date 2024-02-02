package com.fuyu.mapper;

import com.fuyu.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     *根据菜品id查询套餐id
     * @param dishIds
     */
    //select setmeal_id from setmeal_dish where  dish_id in (1,2,3,4)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入套餐与菜品数据
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id获取 套餐和菜品数据列表
     * @param setMealId
     * @return
     */
    @Select("select *from setmeal_dish  where  setmeal_id=#{setMealId} ")
    List<SetmealDish> getSetmealDishListBySetmealId(Long  setMealId);

    /**
     * 根据套餐id删除 套餐和菜品关系数据
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{setMealId}")
    void deleteBySetMealId(Long  setMealId);
}
