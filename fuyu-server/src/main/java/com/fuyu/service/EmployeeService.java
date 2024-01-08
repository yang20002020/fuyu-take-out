package com.fuyu.service;

import com.fuyu.dto.EmployeeDTO;
import com.fuyu.dto.EmployeeLoginDTO;
import com.fuyu.dto.EmployeePageQueryDTO;
import com.fuyu.entity.Employee;
import com.fuyu.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    Employee getById(Integer id);
}
