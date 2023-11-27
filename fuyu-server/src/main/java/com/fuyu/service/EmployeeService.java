package com.fuyu.service;

import com.fuyu.dto.EmployeeDTO;
import com.fuyu.dto.EmployeeLoginDTO;
import com.fuyu.entity.Employee;

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
}
