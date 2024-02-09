package com.fuyu.mapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;


@Mapper
public interface OrderDetailMapper {

    /**
     * 向明细表插入n条数据
     * @param orderDetailList
     */

    void insertBatch(ArrayList<Object> orderDetailList);
}
