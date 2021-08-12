package ru.open.way4service.reportservice.services;

import ru.open.way4service.reportservice.models.ReportConfig;

public interface ReportLoaderService {
    ReportConfig getReportConfig(long reportId);
    ReportConfig getReportConfig(String reportTitle);
}
