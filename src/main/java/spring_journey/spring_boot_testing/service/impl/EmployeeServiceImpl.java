package spring_journey.spring_boot_testing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring_journey.spring_boot_testing.entity.Employee;
import spring_journey.spring_boot_testing.exception.ResourceNotFoundException;
import spring_journey.spring_boot_testing.repository.EmployeeRepository;
import spring_journey.spring_boot_testing.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
   @Autowired
   private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
            this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> saveEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(saveEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee with email " + employee.getEmail() + " already exists");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }


    @Override
    public Employee updateEmployee(Employee updatedEmployeeRequest) {
        return employeeRepository.save(updatedEmployeeRequest);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }

}
