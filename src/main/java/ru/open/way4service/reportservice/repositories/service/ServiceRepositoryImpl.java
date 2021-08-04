package ru.open.way4service.reportservice.repositories.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.Report;

@Repository
public class ServiceRepositoryImpl implements ServiceRepository {
    @PersistenceContext(unitName = "serviceEntityManager")
    private EntityManager entityManager;

    @Override
    public Report getReportById(long id) throws ReportServiceException {
        try {
            TypedQuery<Report> query = entityManager.createQuery("select r from Report r where r.id = ?1", Report.class);
            return query.setParameter(1, id).getSingleResult();
        } catch (Exception ex) {
            throw new ReportServiceException("See nested exception", ex);
        }
    }
    
    @Override
    public Report getReportByTitle(String title) throws ReportServiceException {
        try {
            TypedQuery<Report> query = entityManager.createQuery("select r from Report r where r.title = ?1", Report.class);
            return query.setParameter(1, title).getSingleResult();
        } catch (Exception ex) {
            throw new ReportServiceException("See nested exception", ex);
        }
    }
}
