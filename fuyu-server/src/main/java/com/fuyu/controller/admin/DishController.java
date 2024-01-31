package com.fuyu.controller.admin;
import com.fuyu.dto.DishDTO;
import com.fuyu.dto.DishPageQueryDTO;
import com.fuyu.entity.Dish;
import com.fuyu.result.PageResult;
import com.fuyu.result.Result;
import com.fuyu.service.DishService;
import com.fuyu.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody  DishDTO dishDTO){
      log.info("新增菜品:{}",dishDTO);
      dishService.saveWithFlavor(dishDTO); //后续步骤开发
        //清理缓存数据
         String key= "dish_"+dishDTO.getCategoryId();
         cleanCache(key);

        return  Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO){

        log.info("菜品分页查询:{}",dishPageQueryDTO);
        PageResult  pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品:{}",ids);
        dishService.deleteBatch(ids);

        //清理缓存数据  将所有的菜品缓存清理掉，所有以dish_开头的key
        cleanCache("dish_*");

        return Result.success();
    }


    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO= dishService.getDishByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 根据id修改菜品基本信息和对应的口味信息
     * @param dishDTO
     */
    @PutMapping
    @ApiOperation("修改菜品数据")
    public Result  update(@RequestBody DishDTO dishDTO){
       log.info("修改菜品数据:{}",dishDTO);
       dishService.updateDishWithFlavor(dishDTO);
        //清理缓存数据  将所有的菜品缓存清理掉，所有以dish_开头的key
        cleanCache("dish_*");
       return Result.success();
    }

    /**
     * 菜品起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("菜品起售停售")
    public Result startOrStop(@PathVariable Integer status ,Long id){

         dishService.startOrStop(status,id);
        //清理缓存数据  将所有的菜品缓存清理掉，所有以dish_开头的key
        cleanCache("dish_*");
         return Result.success();

    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
         log.info("根据分类id查询菜品{}",categoryId);
        List<Dish> list =  dishService.getDishListByCategoryId(categoryId);
         return Result.success(list);
    }

    /**
     * 清理缓存
     * @param pattern
     */

    private  void  cleanCache(String pattern ){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
