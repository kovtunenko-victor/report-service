package ru.open.way4service.reportservice.repositories.target;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Repository;
import ru.open.way4service.reportservice.errors.ReportServiceException;


@Repository
public class TargetRepositoryImpl implements TargetRepository {
    @PersistenceContext(unitName = "targetEntityManager")
    private EntityManager entityManager;

    @Override
    public Connection getConnection() {
        try {
            EntityManagerFactoryInfo factoryInfo = (EntityManagerFactoryInfo) entityManager.getEntityManagerFactory();
            return factoryInfo.getDataSource().getConnection();
        } catch (SQLException ex) {
            throw new ReportServiceException(ex);
        }
    }
}
