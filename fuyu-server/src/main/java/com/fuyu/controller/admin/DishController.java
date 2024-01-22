package com.fuyu.controller.admin;
import com.fuyu.dto.DishDTO;
import com.fuyu.dto.DishPageQueryDTO;
import com.fuyu.result.PageResult;
import com.fuyu.result.Result;
import com.fuyu.service.DishService;
import com.fuyu.vo.DishOverViewVO;
import com.fuyu.vo.DishVO;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

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
       return Result.success();
    }
}