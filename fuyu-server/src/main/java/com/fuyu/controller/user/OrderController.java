package com.fuyu.controller.user;
import com.fuyu.dto.OrdersPaymentDTO;
import com.fuyu.dto.OrdersSubmitDTO;
import com.fuyu.result.Result;
import com.fuyu.service.OrderService;
import com.fuyu.vo.OrderPaymentVO;
import com.fuyu.vo.OrderSubmitVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@ApiOperation("C端-订单接口")
public class OrderController {


    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     */

    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
       log.info("用户下单{}",ordersSubmitDTO);
       OrderSubmitVO ordersSubmitVO= orderService.submitOrder(ordersSubmitDTO);
       return Result.success(ordersSubmitVO);
    }

    /**
     * 订单支付
     * 微信小程序申请微信支付
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 用户催单
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("用户催单")
    public  Result reminder(@PathVariable Long  id){

        orderService.reminder(id);
        return Result.success();
    }
}
