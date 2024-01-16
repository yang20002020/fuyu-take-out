package com.fuyu.mapper;
import com.fuyu.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DishFlavorMapper {

    /**
     *  批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);
}
