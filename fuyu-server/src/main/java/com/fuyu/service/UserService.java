package com.fuyu.service;

import com.fuyu.dto.UserLoginDTO;
import com.fuyu.entity.User;

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
