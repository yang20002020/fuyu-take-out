package com.fuyu.service.impl;
import com.fuyu.constant.MessageConstant;
import com.fuyu.constant.StatusConstant;
import com.fuyu.dto.DishDTO;
import com.fuyu.dto.DishPageQueryDTO;
import com.fuyu.entity.Dish;
import com.fuyu.entity.DishFlavor;
import com.fuyu.entity.SetmealDish;
import com.fuyu.exception.DeletionNotAllowedException;
import com.fuyu.mapper.DishFlavorMapper;
import com.fuyu.mapper.DishMapper;
import com.fuyu.mapper.SetmealDishMapper;
import com.fuyu.result.PageResult;
import com.fuyu.service.DishService;
import com.fuyu.vo.DishVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {


    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    @Autowired
    private SetmealDishMapper setmealDishMapper;



    /**
     *  新增菜品和对应的口味
     * @param dishDTO
     */
    @Override
    @Transactional  // 多表数据查询操作，需要事务注解
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish =new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //第一张表
        //向菜品表插入一条数据
        dishMapper.insert(dish);

        //第二张表
        //获取inser语句生成的主键值
        Long dishId = dish.getId();
        // 每一个菜品dish有多种 口味DishFlavor
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //先给dishid赋值，之后插入数据的时候就能赋值
        if(flavors!=null && flavors.size()>0){
           flavors.forEach(
                dishFlavor -> {
                    // 每一个菜品口味DishFlavor对象 都对应一个菜品id dishId
                    // 每一个菜品id 可以有多种口味；多种口味对应一种菜品id
                    dishFlavor.setDishId(dishId);
                }
           );
            //向口味表插入n条数据 批量插入
          dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
         PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
         Page<DishVO> page= dishMapper.pageQuery(dishPageQueryDTO);
         return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
      ids.forEach(
              id->{
                  //根据主键查询菜品
                  Dish dish= dishMapper.getById(id);
                  //判断 当前要删除的菜品状态是否为起售中
                  if(dish.getStatus()== StatusConstant.ENABLE){
                      //如果是起售中，抛出业务异常
                      throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
                  }
                  //判断当前要删除的菜品是否被套餐关联了
                   List<Long> listId=setmealDishMapper.getSetmealIdsByDishIds(ids);
                  if(listId.size()>0 && listId!=null){
                      throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
                  }
              }
      );

//      ids.forEach( id->{
//          //删除菜品表中的菜品数据
//          dishMapper.deleteById(id);
//          //删除菜品关联的口味数据，即口味表中的数据
//          dishFlavorMapper.deleteByDishId(id);
//      });

        //删除菜品表中的菜品数据
        // sql : delete from dish where in in (?,?,?)
        dishMapper.deleteByIds(ids);

        //删除菜品关联的口味数据，即口味表中的数据
        // delete from dish_flavor  where dish_id in (?,?,?)
        dishFlavorMapper.deleteByDishIds(ids);

    }
}
