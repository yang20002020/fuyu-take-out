package com.fuyu.mapper;
import com.fuyu.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /**
     * 向订单表插入数据
     * @param order
     */

    void insert(Orders order);


}
