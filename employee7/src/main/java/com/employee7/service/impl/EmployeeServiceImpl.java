package com.employee7.service.impl;

import com.employee7.entity.Employee;
import com.employee7.exception.ResourceNotFound;
import com.employee7.payload.EmployeeDto;
import com.employee7.repository.EmployeeRepository;
import com.employee7.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

        Employee employee = mapToEntity(employeeDto);

        //employee.setJoiningDate(employeeDto.getJoiningDate());
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(employeeDto.getJoiningDate());
            employee.setJoiningDate(date);
        } catch (ParseException e) {
            // Handle the exception
        }
        Employee saveEmployee = employeeRepository.save(employee);

        EmployeeDto dto =mapToDto(saveEmployee);

     //dto.setJoiningDate(saveEmployee.getJoiningDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dto.setJoiningDate(dateFormat.format(saveEmployee.getJoiningDate()));
     return dto;
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeDto updateEmployee(long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id). orElseThrow(
                ()-> new ResourceNotFound("Employee not Found with id"+id)
        );
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setDepartment(employeeDto.getDepartment());

        Employee updateEmployee = employeeRepository.save(employee);
        EmployeeDto dto = mapToDto(updateEmployee);
        return dto;
    }

    @Override
    public EmployeeDto getEmployeeById(long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Employee not Found with id" + id)
        );
        EmployeeDto dto = mapToDto(employee);
        return dto;
    }

    @Override
    public List<EmployeeDto> getEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(employee -> mapToDto(employee)).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> getEmployees(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        //PageRequest pageable = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        PageRequest pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Employee> pageEmployees = employeeRepository.findAll(pageable);

        List<Employee> employees = pageEmployees.getContent();
        List<EmployeeDto> employeeDtos = employees.stream().map(employee -> mapToDto(employee)).collect(Collectors.toList());

        return employeeDtos;
    }


    EmployeeDto mapToDto(Employee employee){
        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);
        return dto;
    }

    Employee mapToEntity(EmployeeDto employeeDto){
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        return employee;
    }

}
