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
 * @author mg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AccountsTest.Config.class},
        loader = AnnotationConfigContextLoader.class)
public class AccountsTest extends AbstractJUnit4SpringContextTests {

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
    private AccountsController accounts;

    /**
     * Spring's test mockery.
     *
     * @see MockMvc
     */
    private MockMvc restMock;

    /**
     * Setup of Spring's mockery.
     */
    @Before
    public void setup() {
        restMock = MockMvcBuilders.standaloneSetup(accounts).build();
    }

    @Test
    public void accountsTest() throws Exception {
        restMock.perform(get("/accounts"))
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
        restMock.perform(get("/accounts/{account-number}", "25800002589632588"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"accountNumber\":\"25800002589632588\",\"amount\":3000000,\"description\":\"Deposit of CIA\"}"));

    }

    @Test
    public void absentAccountTest() throws Exception {
        restMock.perform(get("/accounts/{account-number}", "101010101010"))
                .andExpect(status().isNotFound());
    }
}
