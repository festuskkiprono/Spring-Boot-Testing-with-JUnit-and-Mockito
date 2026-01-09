package spring_journey.spring_boot_testing.repository;

import org.assertj.core.api.Assert;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import spring_journey.spring_boot_testing.entity.Employee;

@DataJpaTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;
    //Junit test: Save employee operation
    //given_when_then()

    @DisplayName("Junit test: Save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployeee(){

        //given -precondition/setup
        Employee employee =  Employee.builder()
                .firstName("Festus")
                .lastName("kip")
                .email("fest@email.com")
                .build();
        //when - action to test
        Employee savedEmployee = employeeRepository.save(employee);
        //then -verify output
        assertThat(savedEmployee).isNotNull();
    }
}
