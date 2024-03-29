package com.fuyu.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fuyu.constant.MessageConstant;
import com.fuyu.context.BaseContext;
import com.fuyu.dto.OrdersPaymentDTO;
import com.fuyu.dto.OrdersSubmitDTO;
import com.fuyu.entity.*;
import com.fuyu.exception.AddressBookBusinessException;
import com.fuyu.exception.OrderBusinessException;
import com.fuyu.exception.ShoppingCartBusinessException;
import com.fuyu.mapper.*;
import com.fuyu.service.OrderService;
import com.fuyu.utils.WeChatPayUtil;
import com.fuyu.vo.OrderPaymentVO;
import com.fuyu.vo.OrderSubmitVO;
import com.fuyu.websocket.WebSocketServer;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;


    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        //1.处理各种业务异常（地址薄为空、购物车数据为空）
        //@Select("select * from address_book where id = #{id}")
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            //抛出业务异常
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //查询当前用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            //抛出业务异常
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //2.向订单表插入一条数据
        /*
           Orders

            private Long id;
            //订单号
            private String number;     // String.valueOf(System.currentTimeMillis())
            //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款
            private Integer status;    // Orders.PENDING_PAYMENT
            //下单用户id
            private Long userId;      // userId
            //地址id
            private Long addressBookId;    // ordersSubmitDTO 赋值
            //下单时间
            private LocalDateTime orderTime;   //LocalDateTime.now()
            //结账时间
            private LocalDateTime checkoutTime;
            //支付方式 1微信，2支付宝
            private Integer payMethod;     // ordersSubmitDTO 赋值
            //支付状态 0未支付 1已支付 2退款
            private Integer payStatus;     // Orders.UN_PAID
              //实收金额
              private BigDecimal amount;      // ordersSubmitDTO 赋值
              //备注
               private String remark;       // ordersSubmitDTO 赋值
              //用户名
              private String userName;
             //手机号
             private String phone;         // addressBook.getPhone()
              //地址
              private String address;     // addressBook.getDetail()
              //收货人
              private String consignee;   //  addressBook.getConsignee()
              //订单取消原因
              private String cancelReason;
             //订单拒绝原因
             private String rejectionReason;
             //订单取消时间
             private LocalDateTime cancelTime;
             //预计送达时间
             private LocalDateTime estimatedDeliveryTime;   // ordersSubmitDTO 赋值
             //配送状态  1立即送出  0选择具体时间
             private Integer deliveryStatus;      // ordersSubmitDTO 赋值
            //送达时间
             private LocalDateTime deliveryTime;
             //打包费
             private int packAmount;            // ordersSubmitDTO 赋值
            //餐具数量
            private int tablewareNumber;            // ordersSubmitDTO 赋值
            //餐具数量状态  1按餐量提供  0选择具体数量
            private Integer tablewareStatus;      // ordersSubmitDTO 赋值
         */
        Orders order = new Orders();
        /*
             ordersSubmitDTO

              //地址簿id
              private Long addressBookId;
              //付款方式
               private int payMethod;
               //备注
               private String remark;
              //预计送达时间
              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
              private LocalDateTime estimatedDeliveryTime;
              //配送状态  1立即送出  0选择具体时间
              private Integer deliveryStatus;
              //餐具数量
              private Integer tablewareNumber;
              //餐具数量状态  1按餐量提供  0选择具体数量
              private Integer tablewareStatus;
              //打包费
               private Integer packAmount;
              //总金额
              private BigDecimal amount;
         */
        BeanUtils.copyProperties(ordersSubmitDTO,order);
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));//订单号
        order.setUserId(userId);
        order.setStatus(Orders.PENDING_PAYMENT); //订单状态
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        orderMapper.insert(order);

        //3.向订单明细表插入n条数据  数据由购物车决定
        //订单明细数据
        ArrayList<Object> orderDetailList = new ArrayList<>();
        //遍历
        //cart 购物车;  OrderDetail 订单明细  ？？？
        for(ShoppingCart cart: shoppingCartList){
            OrderDetail orderDetail = new OrderDetail();//订单明细
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(order.getId()); // 给订单明细设置订单id
            orderDetailList.add(orderDetail);  // 订单明细列表
        }
        //向明细表插入n条数据
        orderDetailMapper.insertBatch(orderDetailList);
        //4.清空当前用户的购物数据
        shoppingCartMapper.cleanShoppingCart(userId);
        //5.封装VO返回结果
        /*
             //订单id
             private Long id;
             //订单号
             private String orderNumber;
             //订单金额
              private BigDecimal orderAmount;
             //下单时间
              private LocalDateTime orderTime;
         */
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                                                    .id(order.getId())
                                                    .orderNumber(order.getNumber())
                                                    .orderAmount(order.getAmount())
                                                    .orderTime(order.getOrderTime())
                                                    .build();

        return orderSubmitVO;
    }
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        //////////////////////////////////////////////
        Map map = new HashMap();
        map.put("type", 1);//消息类型，1表示来单提醒
        map.put("orderId", orders.getId());
        map.put("content", "订单号：" + outTradeNo);

        //通过WebSocket实现来单提醒，向客户端浏览器推送消息
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
        ///////////////////////////////////////////////////

    }

    /**
     * 用户催单
     * @param id
     */
    @Override
    public void reminder(Long id) {
        //查询订单是否存在
        Orders orders = orderMapper.getById(id);
        if(orders==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //基于websocket 实现催单
        HashMap hashMap = new HashMap();
        hashMap.put("type",2);//2代表用户催单
        hashMap.put("orderId",id);
        hashMap.put("content","订单号:"+orders.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(hashMap));

    }
}
