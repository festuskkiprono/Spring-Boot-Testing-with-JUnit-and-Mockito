package spring_journey.spring_boot_testing.integration;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import spring_journey.spring_boot_testing.entity.Employee;
import spring_journey.spring_boot_testing.repository.EmployeeRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;//serialization and deserialization

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        //in order to keep clean setup for each test case
    }
    @Test
    public void givenEmployeeId_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        Employee employee = Employee.builder()
                .firstName("Festus")
                .lastName("kip")
                .email("fest@email.com")
                .build();

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }
    @DisplayName("Integration test for getAllEmployees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder()
                .firstName("Festus")
                .lastName("Kip")
                .email("fest@mail.com")
                .build());
        listOfEmployees.add(Employee.builder()
                .firstName("Ken")
                .lastName("Kip")
                .email("kenny@mail.com")
                .build());

    employeeRepository.saveAll(listOfEmployees);
        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(listOfEmployees.size())));
    }

    @DisplayName("Integration test for getEmployeeById")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Festus")
                .lastName("Kip")
                .email("fest@mail.com")
                .build();
        employeeRepository.save(employee);

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }
    @DisplayName("Integration test for getEmployeeById that returns not found")
    @Test
    void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        long employeeId = 1L;

        mockMvc.perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
    @DisplayName("Integration for Update Employee - Should Return Updated Employee - 200")
    @Test
    public void givenUpdateEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        //given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Festus")
                .lastName("Kip")
                .email("fest@mail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updateEmployeeRequest = Employee.builder()
                .firstName("Fe")
                .lastName("Ke")
                .email("fe@mail.com")
                .build();



        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployeeRequest)));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updateEmployeeRequest.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(updateEmployeeRequest.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(updateEmployeeRequest.getEmail())));
    }
    @DisplayName("Integration test for Update Employee - Should Return Not found - 404")
    @Test
    public void givenUpdateEmployee_whenUpdateEmployee_thenReturnA404() throws Exception {
        //given
        long employeeId = 3L;
        Employee savedEmployee = Employee.builder()
                .firstName("Festus")
                .lastName("Kip")
                .email("fest@mail.com")
                .build();
        employeeRepository.save(savedEmployee);
        Employee updateEmployeeRequest = Employee.builder()
                .firstName("Fe")
                .lastName("Ke")
                .email("fe@mail.com")
                .build();


        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployeeRequest)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("")
    @Test
    public void givenAnEmployeeId_whenDelete_thenReturnA200() throws Exception
    {
        //given -> precondition/setup
        long employeeId = 1L;

        Employee savedEmployee = Employee.builder()
                .firstName("Festus")
                .lastName("Kip")
                .email("fest@mail.com")
                .build();
        employeeRepository.save(savedEmployee);

        //when -> action that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        //then -> verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }


}
