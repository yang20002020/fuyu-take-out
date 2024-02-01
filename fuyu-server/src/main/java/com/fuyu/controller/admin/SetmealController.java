package com.fuyu.controller.admin;

import com.fuyu.dto.SetmealDTO;
import com.fuyu.dto.SetmealPageQueryDTO;
import com.fuyu.result.PageResult;
import com.fuyu.result.Result;
import com.fuyu.service.SetmealService;
import com.fuyu.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */

    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐:{}",setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return  Result.success();

    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult= setmealService.pageQuery(setmealPageQueryDTO);
        return  Result.success(pageResult);
    }

    /**
     * 根据套餐id查询套餐，用于修改页面回显数据
     * @param setMealId
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据套餐id查询套餐")
    public Result<SetmealVO> getSetMealById(@PathVariable("id") Integer setMealId){

         log.info("根据套餐id查询套餐{}",setMealId);
         SetmealVO setmealVO=  setmealService.getSetMealBySetMealId(setMealId);
         return Result.success(setmealVO);
    }



}
