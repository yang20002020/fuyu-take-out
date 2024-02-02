package com.fuyu.service;
import com.fuyu.dto.SetmealDTO;
import com.fuyu.dto.SetmealPageQueryDTO;
import com.fuyu.entity.Setmeal;
import com.fuyu.result.PageResult;
import com.fuyu.vo.DishItemVO;
import com.fuyu.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);


    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     * @param setMealId
     * @return
     */

    SetmealVO getSetMealBySetMealId(Long setMealId);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);
}
