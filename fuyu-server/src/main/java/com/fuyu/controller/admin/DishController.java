package com.fuyu.controller.admin;
import com.fuyu.dto.DishDTO;
import com.fuyu.dto.DishPageQueryDTO;
import com.fuyu.result.PageResult;
import com.fuyu.result.Result;
import com.fuyu.service.DishService;
import com.fuyu.vo.DishVO;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
}
