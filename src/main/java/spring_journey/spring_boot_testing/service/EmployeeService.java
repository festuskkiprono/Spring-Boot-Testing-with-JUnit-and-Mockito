package spring_journey.spring_boot_testing.service;

import spring_journey.spring_boot_testing.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(long id);
    Employee updateEmployee(Employee updatedEmployeeRequest);
    void deleteEmployee(long id);
}
