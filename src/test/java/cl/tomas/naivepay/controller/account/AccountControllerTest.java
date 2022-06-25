package cl.tomas.naivepay.controller.account;

import cl.tomas.naivepay.domain.Account;
import cl.tomas.naivepay.service.access.AccessService;
import cl.tomas.naivepay.service.account.AccountService;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mock;

    @MockBean
    private AccountService service;

    private static ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private AccessService accessService;

    private static final File accountsFile = Paths
            .get("src", "test", "resources", "controller", "accounts", "accounts.json").toFile();

    private String adminToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiI" +
            "sInJvbGVzIjpbIjEiXSwiaXNzIjoiL2xvZ2luIiwiYWNjZXNzX2lkIjoxLCJleHAiOjE2NzQ2NTYyMzh" +
            "9.ZgYEQ0xPY7gf0OKUSjEznr6fOqo8MCuQlIDeJw-tIdo";

    private String customerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXN" +
            "0b21lciIsInJvbGVzIjpbIjAiXSwiaXNzIjoiL2xvZ2luIiwiYWNjZXNzX2lkIjoyLCJleHAiOjE2NzQ" +
            "2NjIzMDN9.2NXCpKEPMYApaHpY1KfvpqgNnwY6JdSdN1fR0_1LQYk";

    private static List<Account> accounts;

    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            accounts = mapper.readValue(accountsFile, new TypeReference<List<Account>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(value = "spring")
    void getEmptyAccounts() throws Exception {
        when(service.getAccounts()).thenReturn(new ArrayList<Account>() {
        });
        String url = "/accounts/get";
        MvcResult result = mock.perform(get(url)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String expectedResponse = mapper.writeValueAsString(new ArrayList<Account>() {
        });

        assertEquals(expectedResponse, jsonResponse);
    }

    @Test
    @WithMockUser(value = "spring")
    void getAccounts() throws Exception {
        when(service.getAccounts()).thenReturn(accounts);
        accounts.stream().forEach(account -> System.out.println(account.getAccNum()));
        String url = "/accounts/get";
        MvcResult result = mock.perform(get(url).header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String expectedResponse = mapper.writeValueAsString(accounts);

        assertEquals(expectedResponse, jsonResponse);
    }

    @Test
    @DisplayName("Customer Fetching All Accounts - Should return Forbidden")
    @WithMockUser(value = "spring")
    void shouldReturnForbiddenCustomerFetchingAll() throws Exception {
        when(service.getAccounts()).thenReturn(accounts);
        String url = "/accounts/get";
        mock.perform(get(url).header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.Message").value("You don't have access to this resource"))
                .andReturn();
    }

    @WithMockUser(value = "spring")
    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void getAccountById(int index) throws Exception {
        when(service.getAccountById((long) (index + 1))).thenReturn(accounts.get(index));
        String url = "/accounts/get/" + (index + 1);
        MvcResult result = mock.perform(get(url)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String expectedResponse = mapper.writeValueAsString(accounts.get(index));
        assertEquals(expectedResponse, jsonResponse);
    }

    @WithMockUser(value = "spring")
    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void updateAccount(int index) throws Exception {
        when(service.updateAccount(accounts.get(index))).thenReturn(accounts.get(index));
        String url = "/accounts/update";
        MvcResult result = mock.perform(put(url)
                .content(mapper.writeValueAsString(accounts.get(index)))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String expectedResponse = mapper.writeValueAsString(accounts.get(index));
        assertEquals(expectedResponse, jsonResponse);
    }

    @WithMockUser(value = "spring")
    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void createAccount(int index) throws JsonProcessingException, Exception {
        when(service.createAccount(accounts.get(index))).thenReturn(accounts.get(index));
        String url = "/accounts/create";
        MvcResult result = mock.perform(post(url)
                .content(mapper.writeValueAsString(accounts.get(index)))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String expectedResponse = mapper.writeValueAsString(accounts.get(index));
        assertEquals(expectedResponse, jsonResponse);
    }

    @WithMockUser(value = "spring")
    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void deleteAccount(int index) throws JsonProcessingException, Exception {
        when(service.deleteAccount((long) index + 1)).thenReturn(true);
        String url = "/accounts/delete/" + (index + 1);
        mock.perform(delete(url)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"))
                .andReturn();
    }

    @Test
    void shouldReturnForbiddenDeleteByCustomer() throws Exception {
        String url = "/accounts/delete/1";
        mock.perform(delete(url)
                .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Message").value("You don't have access to this resource"))
                .andReturn();
    }

    @Test
    void shouldReturnBadRequestUpdatingBadBody() throws Exception {
        String url = "/accounts/update";
        mock.perform(put(url)
                .header("Authorization", "Bearer " + customerToken)
                .content("bad content")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andReturn();
    }
}