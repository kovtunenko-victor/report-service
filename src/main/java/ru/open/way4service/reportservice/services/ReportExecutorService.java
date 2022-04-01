package ru.open.way4service.reportservice.services;

import java.util.Map;
import java.util.concurrent.Future;

import ru.open.way4service.reportservice.models.ReportConfig;

public interface ReportExecutorService <T> {
    public Future<T> executeReport(long requestNumber, ReportConfig reportConfig, Map<String, Object> properties);
}
