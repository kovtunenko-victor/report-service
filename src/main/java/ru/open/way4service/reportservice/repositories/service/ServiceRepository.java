package ru.open.way4service.reportservice.repositories.service;

import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.Report;

public interface ServiceRepository {
    Report getReportById(long id) throws ReportServiceException;
    Report getReportByTitle(String title) throws ReportServiceException;
}
