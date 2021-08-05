package ru.open.way4service.reportservice.repositories.service;

import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ReportConfig;

public interface ServiceRepository {
    ReportConfig getReportById(long id) throws ReportServiceException;
    ReportConfig getReportByTitle(String title) throws ReportServiceException;
}
