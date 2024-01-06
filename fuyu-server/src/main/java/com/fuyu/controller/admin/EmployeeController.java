package com.fuyu.controller.admin;

import com.fuyu.constant.JwtClaimsConstant;
import com.fuyu.dto.EmployeeDTO;
import com.fuyu.dto.EmployeeLoginDTO;
import com.fuyu.dto.EmployeePageQueryDTO;
import com.fuyu.entity.Employee;
import com.fuyu.properties.JwtProperties;
import com.fuyu.result.PageResult;
import com.fuyu.result.Result;
import com.fuyu.service.EmployeeService;
import com.fuyu.utils.JwtUtil;
import com.fuyu.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags="员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value="员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody  EmployeeDTO employeeDTO){
        System.out.println("当前线程的id: "+Thread.currentThread().getId());
        log.info("新增员工：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return   Result.success();
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */

    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> pageQuery (EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为:{}",employeePageQueryDTO);
        PageResult pageResult= employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);

    }
}
