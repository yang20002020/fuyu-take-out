package com.fuyu.service;
import com.fuyu.dto.OrdersSubmitDTO;
import com.fuyu.vo.OrderSubmitVO;

public interface OrderService {

    /**
     *  提交订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
