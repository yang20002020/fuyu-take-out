package com.fuyu.mapper;

import com.fuyu.annotation.AutoFill;
import com.fuyu.dto.DishPageQueryDTO;
import com.fuyu.entity.Dish;
import com.fuyu.enumeration.OperationType;
import com.fuyu.vo.DishVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据菜品id查询菜品信息
     * @param id
     * @return
     */
    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    /**
     * 根据菜品 id 删除菜品数据
     * @param id
     */
    @Delete("delete  from dish where id= #{id}")
    void deleteById(Long id);

    /**
     * 删除菜品表中的菜品数据
     * @param ids
     */
    //sql : delete from dish where in in (?,?,?)

    void deleteByIds(List<Long> ids);

    /**
     * 根据id 动态修改菜品数据
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateDish(Dish dish);

    /**
     * 动态条件查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);


    /**
     * 根据套餐id查询菜品数据
     * @param setMealId
     * @return
     */
    //select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = ?
    @Select("select  dish.* from dish left join  setmeal_dish on  dish.id = setmeal_dish.dish_id  where  setmeal_dish.setmeal_id=#{setMealId}")
    List<Dish> getDishListBysetMealId(Long setMealId);
}
