package com.fuyu.service;
import com.fuyu.dto.OrdersPaymentDTO;
import com.fuyu.dto.OrdersSubmitDTO;
import com.fuyu.vo.OrderPaymentVO;
import com.fuyu.vo.OrderSubmitVO;

public interface OrderService {

    /**
     *  提交订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 用户催单
     * @param id
     */
    void reminder(Long id);
}
