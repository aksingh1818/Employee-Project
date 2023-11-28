package com.employee7.service;

import com.employee7.payload.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    void deleteEmployee(long id);

    EmployeeDto updateEmployee(long id, EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(long id);


    List<EmployeeDto> getEmployee();

    List<EmployeeDto> getEmployees(int pageNo, int pageSize, String sortBy, String sortDir);
}
