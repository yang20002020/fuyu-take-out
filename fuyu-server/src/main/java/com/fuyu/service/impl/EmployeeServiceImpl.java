package com.fuyu.service.impl;

import com.alibaba.druid.support.spring.stat.BeanTypeAutoProxyCreator;
import com.fuyu.constant.MessageConstant;
import com.fuyu.constant.PasswordConstant;
import com.fuyu.constant.StatusConstant;
import com.fuyu.context.BaseContext;
import com.fuyu.dto.EmployeeDTO;
import com.fuyu.dto.EmployeeLoginDTO;
import com.fuyu.entity.Employee;
import com.fuyu.exception.AccountLockedException;
import com.fuyu.exception.AccountNotFoundException;
import com.fuyu.exception.PasswordErrorException;
import com.fuyu.mapper.EmployeeMapper;
import com.fuyu.service.EmployeeService;
import jdk.net.SocketFlow;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对前端传过来的明文密码进行md5 加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        /*
                Employee

                private Long id;
                private String username;
                private String name;
                private String phone;
                private String sex;
                private String idNumber;


                private static final long serialVersionUID = 1L;
                private String password;
                private Integer status;
                //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                private LocalDateTime createTime;
                //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                private LocalDateTime updateTime;
                private Long createUser;
                private Long updateUser;
         */

        //对象属性拷贝
        /*
            private Long id;
            private String username;
            private String name;
            private String phone;
            private String sex;
            private String idNumber;
         */
        BeanUtils.copyProperties(employeeDTO,employee);

        /*
           private static final long serialVersionUID = 1L;
                private String password;
                private Integer status;
                //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                private LocalDateTime createTime;
                //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                private LocalDateTime updateTime;
                private Long createUser;
                private Long updateUser;
         */
        //设置密码 ，默认密码123456
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
        employee.setPassword(md5DigestAsHex);
        employee.setStatus(StatusConstant.ENABLE);
        // LocalDateTime  //localDateTime : 2023-11-27T00:32:14.206
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //TODO 后期需要修改为当前登录用户的id
        employee.setCreateUser(10L);
        employee.setUpdateUser(10L);

        employeeMapper.insert(employee);
    }

}
