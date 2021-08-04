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
@EnableJpaRepositories(entityManagerFactoryRef = "targetEntityManager"
                     , transactionManagerRef = "targetTransactionManager"
                     , basePackages = "ru.open.way4service.reportservice.repositories.target")

public class TargetDBConfiguration {
    private final PersistenceUnitManager persistenceUnitManager;

    public TargetDBConfiguration(ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager.getIfAvailable();
    }

    @Bean
    @ConfigurationProperties("target.jpa")
    public JpaProperties targetJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    @ConfigurationProperties("target.datasource")
    public DataSourceProperties targetDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "target.datasource.properties")
    public HikariDataSource targetDataSource() {
        return targetDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @PersistenceContext(unitName = "targetEntityManager")
    public LocalContainerEntityManagerFactoryBean targetEntityManager(JpaProperties targetJpaProperties) {
        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(targetJpaProperties);
        return builder.dataSource(targetDataSource()).packages("ru.open.way4service.reportservice.models").persistenceUnit("targetDs").build();
    }

    @Bean
    @PersistenceContext(unitName = "targetTransactionManager")
    public JpaTransactionManager targetTransactionManager(EntityManagerFactory targetEntityManager) {
        return new JpaTransactionManager(targetEntityManager);
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
