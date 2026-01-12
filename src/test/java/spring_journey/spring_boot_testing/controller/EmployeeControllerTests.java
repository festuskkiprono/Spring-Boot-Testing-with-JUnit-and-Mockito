package spring_journey.spring_boot_testing.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import spring_journey.spring_boot_testing.entity.Employee;
import spring_journey.spring_boot_testing.service.EmployeeService;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EmployeeControllerTests {


    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void givenEmployeeId_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        Employee employee = Employee.builder()
                .firstName("Festus")
                .lastName("kip")
                .email("fest@email.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

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

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(listOfEmployees.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Festus")
                .lastName("Kip")
                .email("fest@mail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("Update Employee - Should Return Updated Employee - 200")
    @Test
    public void givenUpdateEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        //given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Festus")
                .lastName("Kip")
                .email("fest@mail.com")
                .build();

        Employee updateEmployeeRequest = Employee.builder()
                .firstName("Fe")
                .lastName("Ke")
                .email("fe@mail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(savedEmployee));
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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

    @DisplayName("Update Employee - Should Return Not found - 404")
    @Test
    public void givenUpdateEmployee_whenUpdateEmployee_thenReturnA404() throws Exception {
        //given
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Festus")
                .lastName("Kip")
                .email("fest@mail.com")
                .build();

        Employee updateEmployeeRequest = Employee.builder()
                .firstName("Fe")
                .lastName("Ke")
                .email("fe@mail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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
            //Stubbing a method that returns void deleteEmployee()
            willDoNothing().given(employeeService).deleteEmployee(employeeId);

            //when -> action that we are going to test
            ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

            //then -> verify the output
            response.andExpect(status().isOk())
                    .andDo(print());
        }
}