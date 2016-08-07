package com.sbertech.accounts.functional;

import com.sbertech.accounts.AccountsTestConfig;
import com.sbertech.accounts.model.AccountsProcessor;
import com.sbertech.accounts.web.AccountsController;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;

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
    private AccountsProcessor accountsProcessor;

    /**
     * Spring's test mockery.
     *
     * @see MockMvc
     */
    private MockMvc mockMvc;

    @Test
    public void accountsTest() throws IOException {
    }
}
