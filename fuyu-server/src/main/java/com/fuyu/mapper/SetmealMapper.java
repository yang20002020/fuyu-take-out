package com.fuyu.mapper;
import com.fuyu.annotation.AutoFill;
import com.fuyu.dto.SetmealPageQueryDTO;
import com.fuyu.entity.Setmeal;
import com.fuyu.enumeration.OperationType;
import com.fuyu.vo.DishItemVO;
import com.fuyu.vo.SetmealVO;
import com.github.pagehelper.Page;
import com.sun.org.apache.xpath.internal.objects.XString;
import com.sun.org.apache.xpath.internal.objects.XStringForChars;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {


    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据id修改套餐
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
//    select   s.*, category_name  from  setmeal s left join category c on  s.category_id = c.id
//    where   category_id=? and status=?  and name=?
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据套餐id查询套餐
     * @param id
     */
    @Select("select * from setmeal where id=#{id}")
     Setmeal getSetMealBySetMealId(Integer id);

    /**
     * 根据套餐id 获取分类名称
     * @param setMealId
     */
    @Select("select c.name categoryName   from setmeal s left join category c on s.category_id = c.id where s.id=#{setMealId}")
    String  getCategoryName(Integer setMealId);
}
