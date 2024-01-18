package com.fuyu.service;
import com.fuyu.dto.DishDTO;
import com.fuyu.dto.DishPageQueryDTO;
import com.fuyu.result.PageResult;

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
}
