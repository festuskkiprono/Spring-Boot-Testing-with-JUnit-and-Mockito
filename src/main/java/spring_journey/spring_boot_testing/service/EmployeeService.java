package spring_journey.spring_boot_testing.service;

import spring_journey.spring_boot_testing.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(long id);

    Employee updateEmployee(Employee updatedEmployeeRequest);
    void deleteEmployee(long id);
}
