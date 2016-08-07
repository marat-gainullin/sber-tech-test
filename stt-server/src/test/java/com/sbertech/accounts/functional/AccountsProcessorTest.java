package com.sbertech.accounts.functional;

import com.sbertech.accounts.AccountsTestConfig;
import com.sbertech.accounts.web.AccountsController;
import com.sbertech.accounts.web.TransfersController;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author mg
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AccountsProcessorTest.Config.class},
        loader = AnnotationConfigContextLoader.class)
public class AccountsProcessorTest extends AbstractJUnit4SpringContextTests {

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

    @Test(timeout = 120000L)
    public void transfersConcurrentTest() throws Exception {
        for (int i = 0; i < 100; i++) {
            final CyclicBarrier barrier = new CyclicBarrier(2);
            Thread tStrait = new Thread(() -> {
                try {
                    barrier.await(); // Bariier here to increase a deadlock probability in case of bad locks.
                    restMock.perform(post("/transfers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"fromAccount\": \"25800002589632588\", \"toAccount\": \"15800002589632588\", \"amount\": 250 }"))
                            .andExpect(status().isCreated());
                } catch (Exception ex) {
                    Logger.getLogger(AccountsProcessorTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            Thread tInverse = new Thread(() -> {
                try {
                    barrier.await(); // Bariier here to increase a deadlock probability in case of bad locks.
                    restMock.perform(post("/transfers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"fromAccount\": \"15800002589632588\", \"toAccount\": \"25800002589632588\", \"amount\": 250 }"))
                            .andExpect(status().isCreated());
                } catch (Exception ex) {
                    Logger.getLogger(AccountsProcessorTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            tStrait.start();
            tInverse.start();
            tStrait.join();
            tInverse.join();
            restMock.perform(get("/accounts/{account-number}", "25800002589632588"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(content().json("{\"accountNumber\":\"25800002589632588\",\"amount\":3000000,\"description\":\"Deposit of CIA\"}"));
            restMock.perform(get("/accounts/{account-number}", "15800002589632588"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(content().json("{\"accountNumber\":\"15800002589632588\",\"amount\":5000000,\"description\":\"Account of Pizza Hutt\"}"));
        }
    }
}
