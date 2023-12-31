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

    /**
     *启用禁用员工账号
     * @param status
     * @param id
     *
     */

    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用员工账号")
  //   http://localhost/api/employee/status/0?id=13
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用员工账号，{},{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success(); // success() 返回 result
    }
//    public static <T> Result<T> success() {
//        Result<T> result = new Result<T>();
//        result.code = 1;
//        return result;
//    }
//  code 编码：1成功，0和其它数字为失败

    /**
     *根据id查询用户信息
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户信息")
   public  Result<Employee> getById(@PathVariable Integer id){
      Employee employee = employeeService.getById(id);
      return Result.success(employee); // result.code = 1;  result.data = employee;
   }

   /*
        public static <T> Result<T> success(T object) {
            Result<T> result = new Result<T>();
            result.data = object;
            result.code = 1;
            return result;
        }
    */

    /**
     * 更新修改员工信息
     */
    @PutMapping
    @ApiOperation("更新修改员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
         log.info("更新修改员工信息:{}",employeeDTO);
         employeeService.update(employeeDTO);
         return Result.success();

    }


}
