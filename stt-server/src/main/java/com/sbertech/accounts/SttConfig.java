package com.sbertech.accounts;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author mg
 */
@Configuration
@EnableTransactionManagement
public class SttConfig {

    /**
     * Spring's annotation based configuration factory method.
     *
     * @param dataSource A {@code DataSource} instance to be used for entity
     * manager factory.
     * @param jpaVendorAdapter A {@code JpaVendorAdapter} instance to be used
     * for entity manager factory.
     * @return {@code AbstractEntityManagerFactoryBean} instance just
     * constructed.
     */
    @Bean
    public final AbstractEntityManagerFactoryBean entityManagerFactory(
            final DataSource dataSource,
            final JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
                = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create");
        jpaProperties.setProperty("hibernate.hbm2ddl.import_files", "init.sql");
        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setPackagesToScan("com.sbertech.accounts");
        return entityManagerFactoryBean;
    }

    /**
     * Spring's annotation based configuration factory method.
     *
     * @return {@code DataSource} instance.
     */
    @Bean
    public final DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:tcp://localhost/~/sber-tech-test/stt");
        ds.setUsername("sa");
        ds.setPassword("sa");
        ds.setInitialSize(DEFAULT_CONNECTION_POOL_SIZE);
        return ds;
    }

    /**
     * Default size of database connections pool.
     */
    private static final int DEFAULT_CONNECTION_POOL_SIZE = 5;

    /**
     * Spring's annotation based configuration factory method.
     *
     * @return {@code JpaVendorAdapter} instance.
     */
    @Bean
    public final JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.H2);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        return adapter;
    }

    /**
     * Spring's annotation based configuration factory method.
     *
     * @param entityManagerFactory {@code EntityManagerFactory} instance to used
     * for {@code PlatformTransactionManager}
     * @return A {@code PlatformTransactionManager} instance.
     */
    @Bean
    public final PlatformTransactionManager transactionManager(
            final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
