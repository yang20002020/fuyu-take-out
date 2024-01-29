package com.fuyu.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fuyu.constant.MessageConstant;
import com.fuyu.dto.UserLoginDTO;
import com.fuyu.entity.User;
import com.fuyu.exception.LoginFailedException;
import com.fuyu.mapper.UserMapper;
import com.fuyu.properties.WeChatProperties;
import com.fuyu.service.UserService;
import com.fuyu.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl  implements UserService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //1.调用微信用户接口，获取微信用户的openid
        String openid = getOpenid(userLoginDTO.getCode());
        //2.判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //3. 判断当前用户是否为新用户
         User user= userMapper.getByOpenId(openid);
        //4. 如果是新用户，自动完成注册
            if(user==null){
                 user=User.builder()
                          .openid(openid)
                          .createTime(LocalDateTime.now())
                          .build();
                 userMapper.insert(user);
            }
        //5. 返回这个用户对象
        return user;
    }



    /**
     * 调用微信接口服务，获取微信用户的openid
     * @param code 授权码
     * @return
     */
    private String getOpenid(String code){
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
