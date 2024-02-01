package com.fuyu.service.impl;
import com.fuyu.dto.SetmealDTO;
import com.fuyu.dto.SetmealPageQueryDTO;
import com.fuyu.entity.Setmeal;
import com.fuyu.entity.SetmealDish;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
