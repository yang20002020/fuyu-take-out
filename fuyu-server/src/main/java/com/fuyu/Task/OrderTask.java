package com.fuyu.Task;
import com.fuyu.entity.Orders;
import com.fuyu.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 定时任务，定时处理订单状态
 */

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时的订单的方法
     */
    @Scheduled(cron = "0 * * * * ?")//每分钟触发一次
    public void processTimeoutOrder(){
       log.info("定时处理超时订单：{}", LocalDateTime.now());
       //订单超过15分钟
       //select * from orders where status =? and order_time<(当前时间-15分钟)

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if(ordersList!=null && ordersList.size()>0){
            for(Orders orders: ordersList){
               orders.setStatus(Orders.CANCELLED);
               orders.setCancelReason("订单超时，自动取消");
               orders.setCancelTime(LocalDateTime.now());
               orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理“派送中”状态的订单
     */
    @Scheduled(cron = "0 0 1 * * ?") //每天凌晨一点触发一次
    public void processDeliveryOrder(){
        log.info("处理派送中订单：{}", new Date());
        // select * from orders where status = 4 and order_time < 当前时间-1小时
        //当前时间是凌晨一点，减去一小时，就是凌晨零点 time为晚上零点
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if(ordersList != null && ordersList.size() > 0){
            ordersList.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            });
        }
    }
}
