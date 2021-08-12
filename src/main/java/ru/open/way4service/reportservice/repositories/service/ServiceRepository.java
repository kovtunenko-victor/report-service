package ru.open.way4service.reportservice.repositories.service;

import ru.open.way4service.reportservice.models.ReportConfig;

public interface ServiceRepository {
    ReportConfig getReportById(long id);
    ReportConfig getReportByTitle(String title);
}
