/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sbertech.accounts.integration;

import com.sbertech.accounts.AccountsTestConfig;
import com.sbertech.accounts.web.AccountsController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author MGaynullin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AccountsControllerTest.Config.class},
        loader = AnnotationConfigContextLoader.class)
public class AccountsControllerTest extends AbstractJUnit4SpringContextTests {

    /**
     * Configuration stub for spring <code>SpringJUnit4ClassRunner</code>
     *
     * @see SpringJUnit4ClassRunner
     */
    @Configuration
    public static class Config extends AccountsTestConfig {
    }

    /**
     * An <code>AccountsController</code> autowired instance.
     *
     * @see AccountsController
     */
    @Autowired
    private AccountsController accountsController;

    /**
     * Spring's test mockery.
     *
     * @see MockMvc
     */
    private MockMvc mockMvc;

    /**
     * Setup of Spring's mockery.
     */
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountsController).build();
    }

    @Test
    public void accountsTest() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(""
                        + "["
                        + "{\"accountNumber\":\"15800002589632588\",\"amount\":5000000,\"description\":\"Account of Pizza Hutt\"},"
                        + "{\"accountNumber\":\"25800002589632588\",\"amount\":3000000,\"description\":\"Deposit of CIA\"},"
                        + "{\"accountNumber\":\"35800002589632588\",\"amount\":2000000,\"description\":\"Jurassic park account\"},"
                        + "{\"accountNumber\":\"45800002589632588\",\"amount\":1000000,\"description\":\"University account\"},"
                        + "{\"accountNumber\":\"55800002589632588\",\"amount\":900000,\"description\":\"Inter account\"},"
                        + "{\"accountNumber\":\"65800002589632588\",\"amount\":800000,\"description\":\"FinTech account\"}]"));

    }

    @Test
    public void accountByNumberTest() throws Exception {
        mockMvc.perform(get("/accounts/{account-number}", "25800002589632588"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"accountNumber\":\"25800002589632588\",\"amount\":3000000,\"description\":\"Deposit of CIA\"}"));

    }

    @Test
    public void operationsOfAccountTest() throws Exception {
        mockMvc.perform(post("/operations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/operations/1"));

        mockMvc.perform(get("/operations/{operation-id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{ \"id\": 1, \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"));

        mockMvc.perform(get("/accounts/{account-number}/operations", "25800002589632588"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("["
                        + "{ \"id\": 1, \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"
                        + "]"));

        mockMvc.perform(get("/accounts/{account-number}/operations", "15800002589632588"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("["
                        + "{ \"id\": 1, \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"
                        + "]"));

    }

    @Test
    public void notEnoughAmountTest() throws Exception {
        mockMvc.perform(post("/operations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 10000000 }"))
                .andExpect(status().isConflict());
    }
}
