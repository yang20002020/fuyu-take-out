package com.fuyu.controller.user;
import com.fuyu.dto.ShoppingCartDTO;
import com.fuyu.entity.ShoppingCart;
import com.fuyu.result.Result;
import com.fuyu.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * 购物车
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端-购物车相关接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
      @PostMapping("/add")
      @ApiOperation("添加购物车")
      public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
         log.info("添加购物车:{}",shoppingCartDTO);

         shoppingCartService.addShoppingCart(shoppingCartDTO);

          return Result.success();
      }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> getShoppingCartList(){
        log.info("查看购物车");
        List<ShoppingCart> shoppingCarts=shoppingCartService.showShoppingCart();
        return Result.success(shoppingCarts);
    }

}
