package spring_journey.spring_boot_testing.repository;

import org.assertj.core.api.Assert;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import spring_journey.spring_boot_testing.entity.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;
    //Junit test: Save employee operation
    //given_when_then()

    Employee employee;
    @BeforeEach
    public void setup() {
         employee =  Employee.builder()
                .firstName("Festus")
                .lastName("kip")
                .email("fest@email.com")
                .build();

    }
    @DisplayName("Junit test: Save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnEmployee(){

        //given -precondition/setup
       /* Employee employee =  Employee.builder()
                .firstName("Festus")
                .lastName("kip")
                .email("fest@email.com")
                .build();*/
        //when - action to test
        Employee savedEmployee = employeeRepository.save(employee);
        //then -verify output
        assertThat(savedEmployee).isNotNull();
    }


    @DisplayName("Get all employees test")
    @Test
    public void giveAllEmployees_whenFindAll_thenReturnAllEmployeesInaList(){



        employeeRepository.save(employee);
        Employee employee2 =  Employee.builder()
                .firstName("Lucy")
                .lastName("Ch")
                .email("lc@email.com")
                .build();
        employeeRepository.save(employee2);

        List<Employee> employees  = employeeRepository.findAll();
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    @DisplayName("Get employee by id operation")
        @Test
        public void given_whenFindById_thenReturnEmployee()
        {
            //given -> precondition/setup
           /* Employee employee =  Employee.builder()
                    .firstName("Festus")
                    .lastName("kip")
                    .email("fest@email.com")
                    .build();*/
            employeeRepository.save(employee);

            //when -> action that we are going to test
            var employeeFromDB = employeeRepository.findById(employee.getId());
            //then -> verify the output
            assertThat(employeeFromDB).isNotNull();

        }
        @DisplayName("Get employee by email test")
            @Test
            public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject()
            {
                //given -> precondition/setup

                employeeRepository.save(employee);
                //when -> action that we are going to test
                var  employeeFromDB = employeeRepository.findByEmail(employee.getEmail());
                //then -> verify the output
                assertThat(employeeFromDB).isNotNull();

            }

            @DisplayName("")
                @Test
                public void givenEmployeeObject_whenUpdated_thenReturnUpdatedEmployee()
                {
                    //given -> precondition/setup

                    //when - action to test
                    Employee savedEmployee = employeeRepository.save(employee);
                    var employeeFromDB = employeeRepository.findById(savedEmployee.getId()).get();
                    employeeFromDB.setEmail("fest@maill.com");
                    employeeRepository.save(employeeFromDB);

                    Employee updatedEmployee = employeeRepository.findById(savedEmployee.getId()).get();
                    //then -> verify the output
                    assertThat(updatedEmployee.getEmail()).isEqualTo("fest@maill.com");
                }

    @DisplayName("")
        @Test
        public void givenAnEmployee_whenDeleted_thenReturnAnEmptyEmployeeObject()
        {
            //given -> precondition/setup

            Employee savedEmployee = employeeRepository.save(employee);
            //when -> action that we are going to test
            employeeRepository.delete(savedEmployee);
            Optional<Employee> employeeFromDB = employeeRepository.findById(savedEmployee.getId());
            //then -> verify the output
            assertThat(employeeFromDB).isEmpty();
        }
        //TESTS FOR JPQL DEFINED QUERY with index:
    @DisplayName("Test for JPQL Defined query with index parameter")
        @Test
        public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnTheEmployeeObject()
        {
            //given -> precondition/setup

            Employee savedEmployee = employeeRepository.save(employee);
            String firstName ="Festus";
            String lastName ="kip";
            //when -> action that we are going to test
            Employee employeeFromDB = employeeRepository.findByJPQL(firstName,lastName);
            //then -> verify the output
            assertThat(employeeFromDB).isNotNull();
            //assertThat(employeeFromDB).isEqualTo(savedEmployee);
        }

    //TESTS FOR JPQL DEFINED QUERY with index:
    @DisplayName("Test for JPQL Defined query with named parameter")
    @Test
    public void givenFirstNameAndLastNameAsNamedParam_whenFindByJPQL_thenReturnTheEmployeeObject()
    {
        //given -> precondition/setup

        Employee savedEmployee = employeeRepository.save(employee);
        String firstName ="Festus";
        String lastName ="kip";
        //when -> action that we are going to test
        Employee employeeFromDB = employeeRepository.findByJPQLNamedParams(firstName,lastName);
        //then -> verify the output
        assertThat(employeeFromDB).isNotNull();
        //assertThat(employeeFromDB).isEqualTo(savedEmployee);
    }
}
