package spring_journey.spring_boot_testing.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.ResultActions;
import spring_journey.spring_boot_testing.entity.Employee;
import spring_journey.spring_boot_testing.exception.ResourceNotFoundException;
import spring_journey.spring_boot_testing.repository.EmployeeRepository;
import spring_journey.spring_boot_testing.service.impl.EmployeeServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    @BeforeEach
    public void setUp(){
         employee =  Employee.builder()
                .id(1L)
                .firstName("Festus")
                .lastName("kip")
                .email("fest@email.com")
                .build();
    }
    @DisplayName("JUnitest for save employee method")
        @Test
        public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject()
        {

            //given -> precondition/setup
            //we need to provide a stubs for the two methods defined in saveEmployee method in EmployeeServiceImpl
            //Stubs for the two methods
            BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
            BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
            //when -> action that we are going to test
            Employee savedEmployee = employeeService.saveEmployee(employee);
            //then -> verify the output
            Assertions.assertThat(savedEmployee).isNotNull();
        }

    @DisplayName("JUnitest for save employee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException()
    {
        //given -> precondition/setup
        //we need to provide a stubs for the two methods defined in saveEmployee method in EmployeeServiceImpl
        //Stubs for the two methods
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        //when -> action that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,() -> employeeService.saveEmployee(employee));
        //then -> verify the output
        verify(employeeRepository,never()).save(any(Employee.class));
    }
    @DisplayName("JUnit test for get all  method")
        @Test
        public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList()
        {
            Employee employee2 =  Employee.builder()
                    .id(2L)
                    .firstName("Ken")
                    .lastName("kip")
                    .email("kenny@email.com")
                    .build();
            //given -> precondition/setup
            //stub method call
            BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee,employee2));
            //when -> action that we are going to test
            List<Employee> employeeList= employeeService.getAllEmployees();
            //then -> verify the output
            Assertions.assertThat(employeeList).isNotNull();
            Assertions.assertThat(employeeList.size()).isEqualTo(2);
        }

    @DisplayName("JUnit test for getAll employees method negative scenario")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmployeeList()
    {
        Employee employee2 =  Employee.builder()
                .id(2L)
                .firstName("Ken")
                .lastName("kip")
                .email("kenny@email.com")
                .build();
        //given -> precondition/setup
        //stub method call
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when -> action that we are going to test
        List<Employee> employeeList= employeeService.getAllEmployees();
        //then -> verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("Test Get employee by id method")
        @Test
        public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject()
        {
            //given -> precondition/setup
            BDDMockito.given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
            //when -> action that we are going to test
            Optional<Employee> optionalEmployee = employeeService.getEmployeeById(1L);
            Employee savedEmployee = optionalEmployee.get();
            //then -> verify the output
            Assertions.assertThat(savedEmployee).isNotNull();
        }
        @DisplayName("JUnit test for update employee method")
            @Test
            public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee()
            {
                //given -> precondition/setup
                BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
                employee.setEmail("fret@gnail.com");
                employee.setFirstName("Felix");
                //when -> action that we are going to test
                Employee updatedEmployee = employeeService.updateEmployee(employee);
                //then -> verify the output
                Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("fret@gnail.com");
                Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Felix");
            }

            @DisplayName("Juint test for delete employee method")
                @Test
                public void givenEmployeeId_whenDeleteEmployee_thenNothing()
                {
                    //given -> precondition/setup
                    long id = employee.getId();
                    BDDMockito.willDoNothing().given(employeeRepository).deleteById(id);

                    //when -> action that we are going to test
                    employeeService.deleteEmployee(id);
                    //then -> verify the output
                    verify(employeeRepository,times(1)).deleteById(id);
                }



}
