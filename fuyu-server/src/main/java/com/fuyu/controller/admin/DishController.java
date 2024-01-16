package com.fuyu.controller.admin;
import com.fuyu.dto.DishDTO;
import com.fuyu.result.Result;
import com.fuyu.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
