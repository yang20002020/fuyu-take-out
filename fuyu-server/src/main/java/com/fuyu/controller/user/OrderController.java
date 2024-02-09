package com.fuyu.controller.user;
import com.fuyu.dto.OrdersSubmitDTO;
import com.fuyu.result.Result;
import com.fuyu.service.OrderService;
import com.fuyu.vo.OrderSubmitVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@ApiOperation("C端-订单接口")
public class OrderController {


    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     */

    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
       log.info("用户下单{}",ordersSubmitDTO);
       OrderSubmitVO ordersSubmitVO= orderService.submitOrder(ordersSubmitDTO);
       return Result.success(ordersSubmitVO);
    }
}
