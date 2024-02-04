package com.fuyu.mapper;
import com.fuyu.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    /**
     * 动态条件查询购物车
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新商品数量
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id= #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 向购物车中添加菜品或者套餐数据
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name,user_id,dish_id,setmeal_id,dish_flavor,number," +
            "amount,image,create_time)" +
            "values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);
}
