package com.fuyu.service;
import com.fuyu.dto.DishDTO;

public interface DishService {

    /**
     * 新增菜品
     * @param dishDTO
     */
   public   void saveWithFlavor(DishDTO dishDTO);
}
