package cl.tomas.naivepay.controller.customer;

import cl.tomas.naivepay.infrastructure.models.Customer;
import cl.tomas.naivepay.service.access.AccessService;
import cl.tomas.naivepay.service.customer.CustomerService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mock;

    @MockBean
    private CustomerService service;

    @MockBean
    private AccessService accessService;

    private static ObjectMapper mapper = new ObjectMapper();

    private static final File customersFile = Paths
            .get("src", "test", "resources", "controller", "customers", "customers.json").toFile();

//    private String adminToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiI" +
//            "sInJvbGVzIjpbIjEiXSwiaXNzIjoiL2xvZ2luIiwiYWNjZXNzX2lkIjoxLCJleHAiOjE2NzQ2NTYyMzh" +
//            "9.ZgYEQ0xPY7gf0OKUSjEznr6fOqo8MCuQlIDeJw-tIdo";
//
//    private String customerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXN" +
//            "0b21lciIsInJvbGVzIjpbIjAiXSwiaXNzIjoiL2xvZ2luIiwiYWNjZXNzX2lkIjoyLCJleHAiOjE2NzQ" +
//            "2NjIzMDN9.2NXCpKEPMYApaHpY1KfvpqgNnwY6JdSdN1fR0_1LQYk";
//
//    private static List<Customer> customers;
//
//    @BeforeAll
//    static void setup() {
//        mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//
//        try {
//            customers = mapper.readValue(customersFile, new TypeReference<List<Customer>>() {
//            });
//        } catch (Exception e) {
//            System.out.println("Error: " + e);
//        }
//    }
//
//    @WithMockUser(value = "spring")
//    @Test
//    void getAllCustomers() throws Exception {
//
//        when(service.getCustomers()).thenReturn(customers);
//        MvcResult result = mock.perform(get("/customers/get")
//                .header("Authorization", "Bearer " + adminToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();
//        String jsonResponse = result.getResponse().getContentAsString();
//        String expectedResponse = mapper.writeValueAsString(customers);
//
//        System.out.println("Response: " + jsonResponse);
//        System.out.println("Expected: " + expectedResponse);
//
//        assertEquals(expectedResponse, jsonResponse);
//    }
//
//    @WithMockUser(value = "spring")
//    @ParameterizedTest
//    @ValueSource(ints = { 0, 1, 2, 3 })
//    void getCustomerById(int index) throws Exception {
//        when(service.getById(index + 1)).thenReturn(customers.get(index));
//        MvcResult result = mock.perform(get("/customers/get/" + (index + 1))
//                .header("Authorization", "Bearer " + adminToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();
//        String jsonResponse = result.getResponse().getContentAsString();
//        String expectedResponse = mapper.writeValueAsString(customers.get(index));
//
//        System.out.println("Response: " + jsonResponse);
//        System.out.println("Expected: " + expectedResponse);
//
//        assertEquals(expectedResponse, jsonResponse);
//    }
//
//    @WithMockUser(value = "spring")
//    @ParameterizedTest
//    @ValueSource(ints = { 0, 1, 2, 3 })
//    void createCustomer(int index) throws Exception {
//        when(service.createCustomer(customers.get(index))).thenReturn(customers.get(index));
//        MvcResult result = mock.perform(post("/customers/create")
//                .header("Authorization", "Bearer " + adminToken)
//                .content(mapper.writeValueAsString(customers.get(index)))
//                .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();
//
//        String jsonResponse = result.getResponse().getContentAsString();
//        String expectedResponse = mapper.writeValueAsString(customers.get(index));
//
//        System.out.println("Response: " + jsonResponse);
//        System.out.println("Expected: " + expectedResponse);
//
//        assertEquals(expectedResponse, jsonResponse);
//    }
//
//    @DisplayName("Should Return Bad Request - Create Customer Broken Content ")
//    @WithMockUser(value = "spring")
//    @Test
//    void shouldReturnBadRequestCreating() throws Exception {
//        mock.perform(put("/customers/create")
//                .content("bad content")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .header("Authorization", "Bearer " + adminToken))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
//                .andReturn();
//
//    }
//
//    @WithMockUser(value = "spring")
//    @ParameterizedTest
//    @ValueSource(ints = { 0, 1, 2, 3 })
//    void updateCustomer(int index) throws Exception {
//        when(service.updateCustomer(customers.get(index))).thenReturn(customers.get(index));
//        MvcResult result = mock.perform(put("/customers/update")
//                .content(mapper.writeValueAsString(customers.get(index)))
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .header("Authorization", "Bearer " + adminToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();
//        String jsonResponse = result.getResponse().getContentAsString();
//        String expectedResponse = mapper.writeValueAsString(customers.get(index));
//
//        System.out.println("Response: " + jsonResponse);
//        System.out.println("Expected: " + expectedResponse);
//
//        assertEquals(expectedResponse, jsonResponse);
//    }
//
//    @DisplayName("Should Return Bad Request - Update Customer Broken Content ")
//    @WithMockUser(value = "spring")
//    @Test
//    void shouldReturnBadRequestUpdating() throws Exception {
//        mock.perform(put("/customers/update")
//                .content("bad content")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .header("Authorization", "Bearer " + adminToken))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
//                .andReturn();
//
//    }
//
//    @WithMockUser(value = "spring")
//    @ParameterizedTest
//    @ValueSource(ints = { 0, 1, 2, 3 })
//    void deleteCustomerAsAdmin(int index) throws Exception {
//        when(service.deleteCustomer((long) index + 1)).thenReturn(true);
//        mock.perform(delete("/customers/delete/" + (index + 1))
//                .content(mapper.writeValueAsString(customers.get(index)))
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .header("Authorization", "Bearer " + adminToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(content().string("true"))
//                .andReturn();
//
//    }
//
//    @DisplayName("Should Return Forbbiden - Customer Deleting ")
//    @WithMockUser(value = "spring")
//    @Test
//    void shouldReturnForbiddenCustomerDeleting() throws Exception {
//        when(service.deleteCustomer(1)).thenReturn(true);
//        mock.perform(delete("/customers/delete/1")
//                .content(mapper.writeValueAsString(customers.get(0)))
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .header("Authorization", "Bearer " + customerToken))
//                .andExpect(status().isForbidden())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.Message").value("You don't have access to this resource"))
//                .andReturn();
//
//    }
//
//    @DisplayName("Should Return Forbbiden - Customer Fetching All ")
//    @WithMockUser(value = "spring")
//    @Test
//    void shouldReturnForbiddenCustomerFetchingAll() throws Exception {
//        when(service.deleteCustomer(1)).thenReturn(true);
//        mock.perform(delete("/customers/get")
//                .header("Authorization", "Bearer " + customerToken))
//                .andExpect(status().isForbidden())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.Message").value("You don't have access to this resource"))
//                .andReturn();
//
//    }
}