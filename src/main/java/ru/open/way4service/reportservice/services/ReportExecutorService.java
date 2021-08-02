package ru.open.way4service.reportservice.services;

import java.util.Map;

import ru.open.way4service.reportservice.errors.ReportServiceException;

public interface ReportExecutorService {
    public void executeReport(Map<String, String> poroperties) throws ReportServiceException;
}
