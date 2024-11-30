package com.nhnacademy.minidooray3team.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidooray3team.config.SecurityConfig;
import com.nhnacademy.minidooray3team.dto.AccountInfo;
import com.nhnacademy.minidooray3team.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = LoginController.class)
@Import(SecurityConfig.class)
class LoginControllerTest {

    @MockitoBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUserCredential() throws Exception {
        when(accountService.findByName("admin")).thenReturn(new AccountInfo("admin", "aaaa"));

        MvcResult result = mockMvc.perform(get("/accounts/{username}", "admin"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        AccountInfo accountInfo = objectMapper.readValue(responseContent, AccountInfo.class);

        assertEquals("admin", accountInfo.getUsername());
        assertEquals("aaaa", accountInfo.getPassword());
    }
}
