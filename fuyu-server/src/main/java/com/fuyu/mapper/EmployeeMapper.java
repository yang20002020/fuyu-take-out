package com.fuyu.mapper;

import com.fuyu.annotation.AutoFill;
import com.fuyu.dto.EmployeeDTO;
import com.fuyu.dto.EmployeePageQueryDTO;
import com.fuyu.entity.Employee;
import com.fuyu.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee
     */
    @AutoFill(value= OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 编辑员工信息 更新
     * @param employee
     */
    @AutoFill(value= OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据id查询用户信息
     * @param id
     */
    @Select("select * from employee where id=#{id}")
     Employee getById(Integer id);
}
