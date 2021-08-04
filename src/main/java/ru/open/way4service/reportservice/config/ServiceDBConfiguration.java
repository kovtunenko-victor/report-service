package ru.open.way4service.reportservice.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import com.zaxxer.hikari.HikariDataSource;

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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "serviceEntityManager"
                     , transactionManagerRef = "serviceTransactionManager"
                     , basePackages = "ru.open.way4service.reportservice.repositories.service")

public class ServiceDBConfiguration {
    private final PersistenceUnitManager persistenceUnitManager;

    public ServiceDBConfiguration(ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager.getIfAvailable();
    }

    @Bean
    @ConfigurationProperties("service.jpa")
    public JpaProperties serviceJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @ConfigurationProperties("service.datasource")
    public DataSourceProperties serviceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "service.datasource.properties")
    public HikariDataSource serviceDataSource() {
        return serviceDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @PersistenceContext(unitName = "serviceEntityManager")
    public LocalContainerEntityManagerFactoryBean serviceEntityManager(JpaProperties serviceJpaProperties) {
        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(serviceJpaProperties);
        return builder.dataSource(serviceDataSource()).packages("ru.open.way4service.reportservice.models").persistenceUnit("serviceDs").build();
    }

    @Bean
    @PersistenceContext(unitName = "serviceTransactionManager")
    public JpaTransactionManager serviceTransactionManager(EntityManagerFactory serviceEntityManager) {
        return new JpaTransactionManager(serviceEntityManager);
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
