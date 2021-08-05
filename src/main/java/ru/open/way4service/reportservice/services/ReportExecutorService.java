package ru.open.way4service.reportservice.services;

import java.util.Map;

import ru.open.way4service.reportservice.errors.ReportServiceException;
import ru.open.way4service.reportservice.models.ReportConfig;

public interface ReportExecutorService {
    public void executeReport(ReportConfig reportConfig, Map<String, Object> poroperties) throws ReportServiceException;
}
