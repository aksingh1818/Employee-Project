package com.employee7.controller;

import com.employee7.payload.EmployeeDto;
import com.employee7.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //http://localhost:8080/api/employee
    @PostMapping
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        EmployeeDto dto = employeeService.saveEmployee(employeeDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);//201
    }


    // http://localhost:8080/api/employee/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {

        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("employee is deleted", HttpStatus.OK);//200
    }

    // http://localhost:8080/api/employee/1
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") long id, @RequestBody EmployeeDto employeeDto) {
        EmployeeDto dto = employeeService.updateEmployee(id, employeeDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);//200
    }

    // http://localhost:8080/api/employee/4
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") long id) {
        EmployeeDto dto = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);//200
    }

    // http://localhost:8080/api/employee
    @GetMapping
    public List<EmployeeDto> getEmployee() {
        return employeeService.getEmployee();
    }


    //http://localhost:8080/api/employee/all?pageNo=0&pageSize=3&sortBy=department&sortDir=desc
    //http://localhost:8080/api/employee/all?pageNo=0&pageSize=3&sortBy=department&sortDir=asc
    @GetMapping("/all")
    public List<EmployeeDto> getEmployees(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                 @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                 @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        List<EmployeeDto> employeeDtos=employeeService.getEmployees(pageNo, pageSize, sortBy, sortDir);
        return  employeeDtos;

    }
}