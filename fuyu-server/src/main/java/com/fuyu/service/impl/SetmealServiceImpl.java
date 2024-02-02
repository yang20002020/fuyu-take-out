package com.fuyu.service.impl;
import com.fuyu.constant.MessageConstant;
import com.fuyu.constant.StatusConstant;
import com.fuyu.dto.SetmealDTO;
import com.fuyu.dto.SetmealPageQueryDTO;
import com.fuyu.entity.Dish;
import com.fuyu.entity.Setmeal;
import com.fuyu.entity.SetmealDish;
import com.fuyu.exception.DeletionNotAllowedException;
import com.fuyu.exception.SetmealEnableFailedException;
import com.fuyu.mapper.DishMapper;
import com.fuyu.mapper.SetmealDishMapper;
import com.fuyu.mapper.SetmealMapper;
import com.fuyu.result.PageResult;
import com.fuyu.service.SetmealService;
import com.fuyu.vo.DishItemVO;
import com.fuyu.vo.SetmealVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //向套餐表插入数据
        setmealMapper.insert(setmeal);

        //获取生成套餐的id
        Long setmealId = setmeal.getId();
        //获取  套餐菜品关系  列表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        setmealDishes.forEach(
                setmealDish -> {
                    //对每一个套餐菜品关系表  设置套餐id
                    setmealDish.setSetmealId(setmealId);
                }
        );

        setmealDishMapper.insertBatch(setmealDishes);



    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
//        select   s.*, category_name  from  setmeal s left join category c on  s.category_id = c.id
//        where   category_id=? and status=?  and name=?
        Page<SetmealVO> page= setmealMapper.pageQuery(setmealPageQueryDTO);
        return  new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据套餐id 查询套餐
     * @param setMealId
     * @return
     */
    @Override
    public SetmealVO getSetMealBySetMealId(Long setMealId) {

        //1.创建 setmealVO对象
        SetmealVO setmealVO = new SetmealVO();
        //2.获取套餐 根据套餐id
        Setmeal setMeal = setmealMapper.getSetMealBySetMealId(setMealId);
        BeanUtils.copyProperties(setMeal,setmealVO);
        //3.获取套餐和菜品数据列表 根据套餐id
        List<SetmealDish> setmealDishList = setmealDishMapper.getSetmealDishListBySetmealId(setMealId);
        setmealVO.setSetmealDishes(setmealDishList);
        //4.获取分类名称
        String categoryName=  setmealMapper.getCategoryName(setMealId);
        setmealVO.setCategoryName(categoryName);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //1、修改套餐表，执行update
        setmealMapper.update(setmeal);

        //获取套餐id
        Long setmealId = setmealDTO.getId();

        //删除套餐id 对应的套餐与菜品表
        //2、删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
        setmealDishMapper.deleteBySetMealId(setmealId);

        //套餐中修改的数据，对应的是setmealDishe 套餐菜品表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        setmealDishes.forEach(
                 setmealDish -> {
                     setmealDish.setSetmealId(setmealId);
                 }
         );

        //3、重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert
         setmealDishMapper.insertBatch(setmealDishes);

    }

    /**
     * 起售停售套餐
     * @param status
     */
    @Override
    public void updateStatus(Integer status,Long id) {
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        if(status== StatusConstant.ENABLE){
            //select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = ?
            List<Dish> dishes = dishMapper.getDishListBysetMealId(id);
            if(dishes!=null && dishes.size()>0){
                dishes.forEach(
                        dish -> {
                            if(dish.getStatus()==StatusConstant.DISABLE){
                                throw  new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                            }
                        }
                );
            }


        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .build();

        setmealMapper.update(setmeal);
    }

    /**
     * 根据套餐id 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatchByIds(List<Long> ids) {
        //起售中的套餐不能删除
        ids.forEach(
                id->{
                    Setmeal setMeal = setmealMapper.getSetMealBySetMealId(id);
                    if(StatusConstant.ENABLE==setMeal.getStatus()){
                        throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
                    }
                }
        );

        //删除套餐表中的数据
        setmealMapper.deleteBatch(ids);
        //删除套餐菜品关系表中的数据
        setmealDishMapper.deleteBatchBySetMealId(ids);

    }


}
