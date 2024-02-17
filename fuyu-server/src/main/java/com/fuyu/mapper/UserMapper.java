package com.fuyu.mapper;
import com.fuyu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public  interface UserMapper {

    /**
     *根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select *from user where openid=#{openid}")
    User getByOpenId(String openid);

    /**
     * 插入数据
     * @param user
     */

    void insert(User user);

    /**
     *
     * @param userId
     * @return
     */
    @Select("select *from user where id=#{userId}")
    User getById(Long userId);
}
