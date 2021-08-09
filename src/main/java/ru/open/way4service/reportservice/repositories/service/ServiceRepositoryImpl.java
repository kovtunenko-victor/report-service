package ru.open.way4service.reportservice.repositories.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ReportConfig;

@Repository
public class ServiceRepositoryImpl implements ServiceRepository {
    @PersistenceContext(unitName = "serviceEntityManager")
    private EntityManager entityManager;

    @Override
    public ReportConfig getReportById(long id) throws ReportServiceException {
        try {
            TypedQuery<ReportConfig> query = entityManager.createQuery("select r from ReportConfig r where r.id = ?1", ReportConfig.class);
            return query.setParameter(1, id).getSingleResult();
        } catch (Exception ex) {
            throw new ReportServiceException("See nested exception", ex);
        }
    }
    
    @Override
    public ReportConfig getReportByTitle(String title) throws ReportServiceException {
        try {
            TypedQuery<ReportConfig> query = entityManager.createQuery("select r from ReportConfig r where r.title = ?1", ReportConfig.class);
            return query.setParameter(1, title).getSingleResult();
        } catch (Exception ex) {
            throw new ReportServiceException("See nested exception", ex);
        }
    }
}
