package com.sbertech.accounts.integration;

import com.sbertech.accounts.AccountsTestConfig;
import com.sbertech.accounts.web.AccountsController;
import com.sbertech.accounts.web.TransfersController;
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
@ContextConfiguration(classes = {TransfersTest.Config.class},
        loader = AnnotationConfigContextLoader.class)
public class TransfersTest extends AbstractJUnit4SpringContextTests {

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
     * An <code>TransfersController</code> autowired instance.
     *
     * @see TransfersController
     */
    @Autowired
    private TransfersController transfers;

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
        restMock = MockMvcBuilders.standaloneSetup(transfers, accounts).build();
    }

    @Test
    public void emptyAccountOperationsTest() throws Exception {
        restMock.perform(get("/accounts/{account-number}/operations", "65800002589632588"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("[]"));
    }
    
    @Test
    public void transferTest() throws Exception {
        restMock.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/transfers/1"));

        restMock.perform(get("/transfers/{transfer-id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{ \"id\": 1, \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"));

        restMock.perform(get("/accounts/{account-number}/operations", "25800002589632588"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("["
                        + "{ \"id\": 1, \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"
                        + "]"));

        restMock.perform(get("/accounts/{account-number}/operations", "15800002589632588"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("["
                        + "{ \"id\": 1, \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"
                        + "]"));
    }

    @Test
    public void transferFromAbsentAccountTest() throws Exception {
        restMock.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"10101010101010\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void transferFromNullAccountTest() throws Exception {
        restMock.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": null, \"toAccount\": \"15800002589632588\", \"amount\": 250 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void transferToAbsentAccountTest() throws Exception {
        restMock.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": \"10101010101010\", \"amount\": 250 }"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void transferToNullAccountTest() throws Exception {
        restMock.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": null, \"amount\": 250 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void transferFromToSameAccountTest() throws Exception {
        restMock.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": \"25800002589632588\", \"amount\": 250 }"))
                .andExpect(status().isExpectationFailed());
    }

    @Test
    public void transferZeroAmountTest() throws Exception {
        restMock.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 0 }"))
                .andExpect(status().isExpectationFailed());
    }

    @Test
    public void transferNullAmountTest() throws Exception {
        restMock.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": null }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void notEnoughAmountTest() throws Exception {
        restMock.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 10000000 }"))
                .andExpect(status().isConflict());
    }

    @Test
    public void absentTransferTest() throws Exception {
        restMock.perform(get("/transfers/{transfer-id}", 101010101010L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void absentAccountTransfersTest() throws Exception {
        restMock.perform(get("/accounts/{account-number}/operations", "10101010101010"))
                .andExpect(status().isNotFound());
    }
}
