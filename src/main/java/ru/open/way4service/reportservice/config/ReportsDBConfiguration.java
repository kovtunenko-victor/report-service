package ru.open.way4service.reportservice.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "reportsEntityManager"
                     , transactionManagerRef = "reportsTransactionManager"
                     , basePackages = "ru.open.way4service.reportservice.repositories.reports")

public class ReportsDBConfiguration {
    private final PersistenceUnitManager persistenceUnitManager;

    public ReportsDBConfiguration(ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager.getIfAvailable();
    }

    @Bean
    @ConfigurationProperties("reports.jpa")
    public JpaProperties reportsJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @ConfigurationProperties("reports.datasource")
    public DataSourceProperties reportsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "reports.datasource.properties")
    public HikariDataSource reportsDataSource() {
        return reportsDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @PersistenceContext(unitName = "reportsEntityManager")
    public LocalContainerEntityManagerFactoryBean reportsEntityManager(JpaProperties reportsJpaProperties) {
        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(reportsJpaProperties);
        return builder.dataSource(reportsDataSource()).packages("ru.open.way4service.reportservice.repositories.reports").persistenceUnit("reportsDs").build();
    }

    @Bean
    @PersistenceContext(unitName = "reportsTransactionManager")
    public JpaTransactionManager reportsTransactionManager(EntityManagerFactory reportsEntityManager) {
        return new JpaTransactionManager(reportsEntityManager);
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(jpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(),
                this.persistenceUnitManager);
    }

    private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(jpaProperties.isShowSql());
        if (jpaProperties.getDatabase() != null) {
            adapter.setDatabase(jpaProperties.getDatabase());
        }
        if (jpaProperties.getDatabasePlatform() != null) {
            adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        }
        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        
        return adapter;
    }
}
