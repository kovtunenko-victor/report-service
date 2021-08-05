package ru.open.way4service.reportservice.services;

import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ReportConfig;

public interface ReportLoaderService {
    ReportConfig getReportConfig(long reportId) throws ReportServiceException;
    ReportConfig getReportConfig(String reportTitle) throws ReportServiceException;
}
