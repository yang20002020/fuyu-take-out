package com.fuyu.mapper;
import com.fuyu.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface DishFlavorMapper {

    /**
     *  批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id 删除口味数据
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void deleteByDishId(Long dishId);

    /**
     *根据菜品id集合 删除菜品关联的口味数据，即口味表中的数据
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 更加菜品id 查询口味数据，即口味表中的数据
     * @param DishId
     */
    @Select("select * from dish_flavor where id=#{DishId}")
    List<DishFlavor> getByDishId(Long DishId);


}
